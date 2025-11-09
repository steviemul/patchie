package io.steviemul.patchie.generator;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.context.ContextProvider;
import io.steviemul.patchie.generator.template.TemplateReader;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PatchContextProvider implements ContextProvider {

  public static final String RULE_ID = "ruleId";
  public static final String MESSAGE = "message";
  public static final String SNIPPET = "snippet";
  public static final String CODE = "code";
  public static final String FILE = "file";

  private final ChatClient chatClient;
  private final PromptTemplate codeFixPrompt;

  public PatchContextProvider(ChatModel chatModel) {
    chatClient = ChatClient.builder(chatModel).build();

    codeFixPrompt = TemplateReader.getCodeFixTemplate();
  }

  @Override
  public AggregatedContext addContext(AggregatedContext aggregatedContext) {

    Map<String, Object> templateContext = getTemplateContext(aggregatedContext);

    log.info(
        "Generating patch to fix issue '{}' on line {} in file '{}'",
        aggregatedContext.getRuleInformation().getRuleId(),
        aggregatedContext.getResultLocation().getStartLine(),
        aggregatedContext.getResultLocation().getFile());

    String patch = getChatResponse(templateContext);

    aggregatedContext.setPatch(patch);

    return aggregatedContext;
  }

  private String getChatResponse(Map<String, Object> templateContext) {
    String prompt = codeFixPrompt.render(templateContext);

    return chatClient.prompt(prompt).call().content();
  }

  private Map<String, Object> getTemplateContext(AggregatedContext aggregatedContext) {

    return Map.of(
        RULE_ID,
        aggregatedContext.getRuleInformation().getRuleId(),
        MESSAGE,
        aggregatedContext.getRuleInformation().getDescription(),
        SNIPPET,
        aggregatedContext.getResultLocation().getSnippet(),
        CODE,
        aggregatedContext.getCode(),
        FILE,
        aggregatedContext.getResultLocation().getFile());
  }
}
