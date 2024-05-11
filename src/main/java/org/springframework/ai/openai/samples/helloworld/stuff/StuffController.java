package org.springframework.ai.openai.samples.helloworld.stuff;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StuffController {

	private final ChatClient chatClient;

	@Value("classpath:/docs/wikipedia-curling.md")
	private Resource docsToStuffResource;

	@Value("classpath:/prompts/qa-prompt.st")
	private Resource qaPromptResource;

	@Autowired
	public StuffController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/ai/stuff")
	public Completion completion(
			// Request parameter 'message' with a default value
			@RequestParam(value = "message", defaultValue = "Which athletes won the mixed doubles gold medal in curling at the 2022 Winter Olympics?'") String message,
			// Request parameter 'stuffIt' with a default value of 'false'
			@RequestParam(value = "addContext", defaultValue = "false") boolean addContext) {

		// Create a prompt template with 'qaPromptResource' lookup
		PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource);
		// Create a map to store the 'question' and 'context' values for the prompt template
		Map<String, Object> map = new HashMap<>();
		// Add the 'message' parameter to the map as 'question'
		map.put("question", message);

		// Check if 'stuffIt' is true or not and set 'context' value in the map accordingly
		if (addContext) {
			map.put("context", docsToStuffResource);
		}
		else {
			map.put("context", "");
		}

		// Create a prompt with the map values
		Prompt prompt = promptTemplate.create(map);

		/**
		 * Call the 'chatClient' with the created prompt and get the result
		 * The 'call' method is supposed to generate an answer for the provided prompt
		 */
		Generation generation = chatClient.call(prompt).getResult();

		/**
		 * Return a new 'Completion' object with the content of the generation's output
		 * The 'Completion' record primarily holds the final completion derived from the AI model
		 */
		return new Completion(generation.getOutput().getContent());
	}

}
