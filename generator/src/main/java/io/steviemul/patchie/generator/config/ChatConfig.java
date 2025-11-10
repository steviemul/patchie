package io.steviemul.patchie.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

  private final String chatModelName;
  private final String chatBaseUrl;
  private final String chatApiKey;

  public ChatConfig(
      @Value("${spring.ai.chat.model}") String chatModelName,
      @Value("${spring.ai.chat.base-url}") String chatBaseUrl,
      @Value("${spring.ai.chat.api-key:}") String chatApiKey) {

    this.chatModelName = chatModelName;
    this.chatBaseUrl = chatBaseUrl;
    this.chatApiKey = chatApiKey;
  }

  @Bean
  public ChatClientFactory chatClientFactory() {
    return new ChatClientFactory(chatModelName, chatBaseUrl, chatApiKey);
  }
}
