package org.springframework.ai.openai.samples.helloworld.simple;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "Open API Client", description = "Test OPEN API client")
public class SimpleAiController {

	private final ChatClient chatClient;

	@GetMapping("/ai/simple")
	public Map<String, String> completion(
			// Request parameter 'message' with a default value of "Tell me a joke"
			@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {

		/**
		 * Call the 'chatClient' with the 'message' parameter and get the AI generated response
		 * The result is returned as a Map with key 'generation' and the value as the response from the AI
		 */
		return Map.of("generation", chatClient.call(message));
	}
}
