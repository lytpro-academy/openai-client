package org.springframework.ai.openai.samples.helloworld.bikeInfo;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log
public class BikeInfoService {

	@Value("classpath:/data/bikes.json")
	private Resource bikesResource;

	@Value("classpath:/prompts/system-qa.st")
	private Resource systemBikePrompt;

	private final ChatClient chatClient;
	private final EmbeddingClient embeddingClient;

	public BikeInfoService(ChatClient chatClient, EmbeddingClient embeddingClient) {
		this.chatClient = chatClient;
		this.embeddingClient = embeddingClient;
	}

	public AssistantMessage retrieve(String message) {

		/**
		 *  Step 1 - Load the JSON document as a list of 'Document' objects
		 *  Create a JsonReader with specified fields to read from
		 *  Invoke the get() method of the JsonReader to retrieve list of 'Document' objects from the JSON data
		 */
		log.info("Loading JSON as Documents");
		JsonReader jsonReader = new JsonReader(bikesResource, "name", "price", "shortDescription", "description");
		List<Document> documents = jsonReader.get();
		log.info("Loading JSON as Documents");

		/**
		 *  Step 2 - Create embeddings for the retrieved documents and save them to a vector store
		 *  Create a 'VectorStore' object using 'SimpleVectorStore' with the 'embeddingClient'
		 *  Add the documents to the vector store
		 */
		log.info("Creating Embeddings...");
		VectorStore vectorStore = new SimpleVectorStore(embeddingClient);
		vectorStore.add(documents);
		log.info("Embeddings created.");

		/**
		 *  Step 3 - Retrieve documents similar to the user message
		 *  Search the vectorStore for documents that are similar to the user's message
		 */
		log.info("Retrieving relevant documents");
		List<Document> similarDocuments = vectorStore.similaritySearch(message);
		log.info(String.format("Found %s relevant documents.", similarDocuments.size()));

		/**
		 *  Step 4 - Embed the retrieved documents into a 'SystemMessage' using the 'system-qa.st' prompt template
		 *  Create a 'UserMessage' object with the original user message
		 */
		Message systemMessage = getSystemMessage(similarDocuments);
		UserMessage userMessage = new UserMessage(message);

		/**
		 *  Step 5 - Generate the AI model's response
		 *  Create a 'Prompt' object containing the system and user messages
		 *  Ask the 'chatClient' to generate a response based on the created prompt
		 */
		//
		log.info("Asking AI model to reply to question.");
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		log.info(prompt.toString());

		ChatResponse chatResponse = chatClient.call(prompt);
		log.info("AI responded.");

		log.info(chatResponse.getResult().getOutput().getContent());
		// Return the output of the chat response
		return chatResponse.getResult().getOutput();
	}

	// This helper method creates a 'SystemMessage' from the list of similar documents
	private Message getSystemMessage(List<Document> similarDocuments) {
		/**
		 * Collect the content of all similar documents and join it into a single string
		 * Create a 'SystemPromptTemplate' with the 'systemBikePrompt'
		 * Create a 'Message' from the 'SystemPromptTemplate' using the combined document content
		 */
		String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemBikePrompt);
        return systemPromptTemplate.createMessage(Map.of("documents", documents));
	}

}
