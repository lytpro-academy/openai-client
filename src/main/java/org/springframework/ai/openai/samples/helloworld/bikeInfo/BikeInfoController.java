package org.springframework.ai.openai.samples.helloworld.bikeInfo;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BikeInfoController {

	private final BikeInfoService bikeInfoService;

	@GetMapping("/ai/bikeInfo")
	public AssistantMessage generate(
			// Request parameter 'message' with a default value of "What bike is good for city commuting?"
			@RequestParam(value = "message", defaultValue = "What bike is good for city commuting?") String message) {
		/**
		 *  Call the 'retrieve' method of 'bikeInfo' with the 'message' parameter
		 *  The method is expected to return an 'AssistantMessage' object as response
		 *  The 'AssistantMessage' object is then returned as the result of this API endpoint
		 */
		return bikeInfoService.retrieve(message);
	}

}
