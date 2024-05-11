package org.springframework.ai.openai.samples.helloworld.roles;

import lombok.extern.java.Log;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Log
public class RoleController {

	private final ChatClient chatClient;

	@Value("classpath:/prompts/system-message.st")
	private Resource systemResource;

	@Autowired
	public RoleController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/ai/roles")
	public AssistantMessage generate(
			// 'message' request parameter with a default value
			@RequestParam(value = "message", defaultValue = "Tell me about three famous pirates from the Golden Age " +
					"of Piracy and why they did.  Write at least a sentence for each pirate.") String message,
			// 'name' request parameter with a default value of "Bob"
			@RequestParam(value = "name", defaultValue = "Bob") String name,
			// 'voice' request parameter with a default value of "pirate"
			@RequestParam(value = "voice", defaultValue = "pirate") String voice) {

		// Create a 'UserMessage' object using 'message' request parameter
		UserMessage userMessage = new UserMessage(message);

		// Create a system prompt template with the 'systemResource' lookup
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);

		// Create a 'Message' object using 'systemPromptTemplate' and map of 'name' and 'voice' parameters
		Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

		// Create a 'Prompt' object using the list of 'userMessage' and 'systemMessage'
		Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
		log.info("prompt for roles"+ prompt);

		// Call the 'chatClient' with the generated prompt and return the result
		return chatClient.call(prompt).getResult().getOutput();
	}

}
