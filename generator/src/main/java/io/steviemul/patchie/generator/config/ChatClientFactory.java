package io.steviemul.patchie.generator.config;

import io.steviemul.patchie.generator.constant.ChatProvider;
import io.steviemul.patchie.generator.exception.ChatApiException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.ollama.management.PullModelStrategy;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Builder
@Slf4j
public class ChatClientFactory {

  private static final String DEFAULT_OLLAMA_CHAT_BASE_URL = "http://localhost:11434";
  private static final String DEFAULT_OLLAMA_CHAT_MODEL = "qwen2.5-coder:7b";

  private static final String DEFAULT_OPENAI_CHAT_MODEL = "gpt-5-mini";

  private static final String DEFAULT_GEMINI_CHAT_MODEL = "gemini-2.5-flash";
  private static final String DEFAULT_GEMINI_CHAT_BASE_URL =
      "https://generativelanguage.googleapis.com/v1beta/openai/";

  private static final String DEFAULT_ANTHROPIC_CHAT_MODEL = "claude-haiku-4-5";

  private final ModelManagementOptions ollamaModelOptions =
      ModelManagementOptions.builder().pullModelStrategy(PullModelStrategy.WHEN_MISSING).build();

  private final String chatModelName;
  private final String chatBaseUrl;
  private final String chatApiKey;

  public ChatClient getChatClient(ChatProvider chatProvider) {

    return switch (chatProvider) {
      case OLLAMA -> getOllamaChatClient();
      case OPENAI -> getOpenAIChatClient();
      case GEMINI -> getGeminiChatClient();
      case ANTHROPIC -> getAnthropicChatClient();
    };
  }

  private ChatClient getOllamaChatClient() {

    String baseUrl = getOrDefault(chatBaseUrl, DEFAULT_OLLAMA_CHAT_BASE_URL);
    String model = getOrDefault(chatModelName, DEFAULT_OLLAMA_CHAT_MODEL);

    log.info("Creating Ollama Chat Client with url {} and model {}", baseUrl, model);

    OllamaApi ollamaApi = OllamaApi.builder().baseUrl(baseUrl).build();

    OllamaOptions ollamaOptions = OllamaOptions.builder().model(chatModelName).build();

    OllamaChatModel chatModel =
        OllamaChatModel.builder()
            .ollamaApi(ollamaApi)
            .defaultOptions(ollamaOptions)
            .modelManagementOptions(ollamaModelOptions)
            .build();

    return ChatClient.builder(chatModel).build();
  }

  private ChatClient getOpenAIChatClient() {
    String apiKey = getPresentApiKey(ChatProvider.OPENAI);
    String model = getOrDefault(chatModelName, DEFAULT_OPENAI_CHAT_MODEL);

    log.info("Creating OpenAI Chat Client for model {}", model);

    OpenAiApi openAiApi = OpenAiApi.builder().apiKey(apiKey).build();

    OpenAiChatOptions chatOptions = OpenAiChatOptions.builder().model(model).build();

    OpenAiChatModel chatModel =
        OpenAiChatModel.builder().openAiApi(openAiApi).defaultOptions(chatOptions).build();

    return ChatClient.builder(chatModel).build();
  }

  private ChatClient getGeminiChatClient() {
    String apiKey = getPresentApiKey(ChatProvider.GEMINI);
    String baseUrl = getOrDefault(chatBaseUrl, DEFAULT_GEMINI_CHAT_BASE_URL);
    String model = getOrDefault(chatModelName, DEFAULT_GEMINI_CHAT_MODEL);

    log.info("Creating Gemini Chat Client for model {}", model);

    OpenAiApi openAiApi = OpenAiApi.builder().apiKey(apiKey).baseUrl(baseUrl).build();

    OpenAiChatOptions chatOptions = OpenAiChatOptions.builder().model(model).build();

    OpenAiChatModel chatModel =
        OpenAiChatModel.builder().openAiApi(openAiApi).defaultOptions(chatOptions).build();

    return ChatClient.builder(chatModel).build();
  }

  private ChatClient getAnthropicChatClient() {

    String apiKey = getPresentApiKey(ChatProvider.ANTHROPIC);
    String model = getOrDefault(chatModelName, DEFAULT_ANTHROPIC_CHAT_MODEL);

    log.info("Creating Anthropic Chat Client for model {}", model);

    AnthropicApi anthropicApi = AnthropicApi.builder().apiKey(apiKey).build();

    AnthropicChatOptions chatOptions =
        AnthropicChatOptions.builder().model(model).maxTokens(500).build();

    AnthropicChatModel chatModel =
        AnthropicChatModel.builder().anthropicApi(anthropicApi).defaultOptions(chatOptions).build();

    return ChatClient.builder(chatModel).build();
  }

  private String getPresentApiKey(ChatProvider chatProvider) {

    if (StringUtils.hasText(chatApiKey)) {
      return chatApiKey;
    }

    throw new ChatApiException("No API key present for provider " + chatProvider);
  }

  private String getOrDefault(String value, String defaultValue) {
    return StringUtils.hasText(value) ? value : defaultValue;
  }
}
