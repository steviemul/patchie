package io.steviemul.patchie.generator;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.generator.template.TemplateReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PatchGenerator {

  public static final String RULE_ID = "ruleId";
  public static final String MESSAGE = "message";
  public static final String SNIPPET = "snippet";
  public static final String CODE = "code";
  public static final String FILE = "file";

  private final ChatClient chatClient;
  private final PromptTemplate codeFixPrompt;
  private final AtomicInteger patchNumber = new AtomicInteger(1);

  private final ExecutorService executor = Executors.newFixedThreadPool(2);

  public PatchGenerator(ChatModel chatModel) {
    chatClient = ChatClient.builder(chatModel).build();

    codeFixPrompt = TemplateReader.getCodeFixTemplate();
  }

  public Future<AggregatedContext> generatePatch(
      AggregatedContext aggregatedContext, String outputLocation) {

    try {
      return executor.submit(() -> createPatch(aggregatedContext, outputLocation));
    } catch (Exception e) {
      log.error("Error retrieving patch", e);
      throw new RuntimeException("Error retrieving patch", e);
    }
  }

  private AggregatedContext createPatch(
      AggregatedContext aggregatedContext, String outputLocation) {
    Map<String, Object> templateContext = getTemplateContext(aggregatedContext);

    log.info(
        "Generating patch to fix issue '{}' on line {} in file '{}'",
        aggregatedContext.getRuleInformation().getRuleId(),
        aggregatedContext.getResultLocation().getStartLine(),
        aggregatedContext.getResultLocation().getFile());

    String patch = getChatResponse(templateContext);

    aggregatedContext.setPatch(patch);

    outputPatch(aggregatedContext, outputLocation);

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

  private void outputPatch(AggregatedContext context, String outputLocation) {

    Path patchFile = Path.of(outputLocation, getPatchFileName());

    try {
      if (patchFile.getParent().toFile().mkdirs()) {
        log.info("Created patch output folder {}", patchFile.getParent());
      }

      Files.writeString(patchFile, context.getPatch(), StandardCharsets.UTF_8);

      log.info("Created patch output file {}", patchFile);
    } catch (Exception e) {
      log.error("Unable to create patch file", e);
    }
  }

  private String getPatchFileName() {
    return String.format("%04d-diff.patch", patchNumber.getAndIncrement());
  }
}
