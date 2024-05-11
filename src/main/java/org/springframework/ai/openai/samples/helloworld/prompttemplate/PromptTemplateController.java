package org.springframework.ai.openai.samples.helloworld.prompttemplate;

import lombok.extern.java.Log;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log
public class PromptTemplateController {

	private final ChatClient chatClient;

	/**
	 * Use the Spring @Value annotation to inject a Resource object that references a Static Resource.
	 * This particular resource is a prompts file: joke-prompt.st.
	 */
	@Value("classpath:/prompts/joke-prompt.st")
	private Resource jokeResource;

	@Autowired
	public PromptTemplateController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/ai/prompt")
	public AssistantMessage completion(
			@RequestParam(value = "adjective", defaultValue = "funny") String adjective,
			@RequestParam(value = "topic", defaultValue = "cows") String topic) {
		// Create an instance of PromptTemplate using the jokeResource
		PromptTemplate promptTemplate = new PromptTemplate(jokeResource);

		/**
		 * Use the create method of PromptTemplate to create an instance of Prompt using
		 * a Map object that maps "adjective" and "topic" to their respective values taken from the query.
		 */
		Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));
		log.info(prompt.toString());
		/**
		 * Use the chatClient to call the AI assistant with your generated prompt.
		 * Extract the result of the call and get the output, then return it.
		 */
		return chatClient.call(prompt).getResult().getOutput();
	}

}
