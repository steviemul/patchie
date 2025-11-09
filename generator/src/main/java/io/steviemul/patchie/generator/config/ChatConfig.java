package io.steviemul.patchie.generator.config;

import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

  private final OllamaApi ollamaApi;
  private final String defaultChatModelName;
  private final String chatBaseUrl;

  public ChatConfig(
      @Value("${spring.ai.chat.model}") String defaultChatModelName,
      @Value("${spring.ai.chat.base-url}") String chatBaseUrl) {

    this.ollamaApi = OllamaApi.builder().baseUrl(chatBaseUrl).build();

    this.chatBaseUrl = chatBaseUrl;
    this.defaultChatModelName = defaultChatModelName;
  }

  private final ModelManagementOptions modelManagementOptions =
      ModelManagementOptions.builder().pullModelStrategy(PullModelStrategy.WHEN_MISSING).build();

  @Bean
  public String chatBaseUrl() {
    return chatBaseUrl;
  }

  @Bean
  public ToolCallingManager toolCallingManager() {
    return ToolCallingManager.builder().build();
  }

  @Bean
  public String defaultChatModelName() {
    return defaultChatModelName;
  }

  @Bean
  public OllamaApi ollamaApi() {
    return ollamaApi;
  }

  @Bean
  public OllamaChatModel ollamaChatModel() {

    OllamaOptions ollamaOptions = OllamaOptions.builder().model(defaultChatModelName).build();

    return OllamaChatModel.builder()
        .ollamaApi(ollamaApi)
        .defaultOptions(ollamaOptions)
        .modelManagementOptions(modelManagementOptions)
        .build();
  }
}
