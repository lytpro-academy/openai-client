package org.springframework.ai.openai.samples.helloworld.bikeInfo.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.samples.helloworld.bikeInfo.BikeInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BikeInfoConfiguration {

	@Bean
	public BikeInfoService bikeInfoService(ChatClient chatClient, EmbeddingClient embeddingClient) {
		return new BikeInfoService(chatClient, embeddingClient);
	}

}
