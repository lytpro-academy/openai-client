package org.springframework.ai.openai.samples.helloworld.output;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@Log
public class OutputParserController {

	private final ChatClient chatClient;

	@GetMapping("/ai/output")
	public ActorsFilms generate(@RequestParam(value = "actor", defaultValue = "Jeff Bridges") String actor) {
		/**
		 * An instance of `BeanOutputParser` is created.
		 * This will parse the output to the `ActorsFilms` class.
		 */
		var outputParser = new BeanOutputParser<>(ActorsFilms.class);

		String format = outputParser.getFormat();
		log.info("format: " + format);

		/**
		 * A message template for the user is created.
		 * It uses multi-line string feature of Java, which is also known as Text Blocks.
		 * This message will ask to generate the filmography for a specific actor. The format and actor's name will be added dynamically.
		 */
		String userMessage = """
				Generate the filmography for the actor {actor}.
				{format}
				""";
		/**
		 * `PromptTemplate` class instance is created, initializing it with our `userMessage` and a map
		 *  that links "actor" and "format" placeholders to their respective variable values.
		 */
		PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("actor", actor, "format", format));
		// A `Prompt` instance is then created using the template.
		Prompt prompt = promptTemplate.create();

		// A `chatClient` is used to send this `Prompt` and it returns a `Generation` object which preserves the result of our chat prompt.
		Generation generation = chatClient.call(prompt).getResult();

		// Finally, it returns the content of the chat output parsed as `ActorsFilms` class instance.
		return outputParser.parse(generation.getOutput().getContent());
	}

}
