package io.steviemul.patchie.pipeline;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.generator.PatchContextProvider;
import io.steviemul.patchie.parser.SarifContextProvider;
import io.steviemul.patchie.resolver.CodeContextProvider;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PipelineRunner {

  private final PatchContextProvider patchContextProvider;

  public void run(String resultsLocation, String sourceCodeRoot, String outputLocation) {

    SarifContextProvider sarifContextProvider =
        SarifContextProvider.builder().resultsLocation(resultsLocation).build();

    List<AggregatedContext> context = sarifContextProvider.getInitialContext();

    CodeContextProvider codeContextProvider =
        CodeContextProvider.builder().sourceCodeRoot(sourceCodeRoot).build();

    context = context.stream().map(codeContextProvider::addContext).toList();

    context = context.stream().limit(1).map(patchContextProvider::addContext).toList();

    context.stream().filter(c -> c.getPatch() != null).forEach(c -> outputPatch(c, outputLocation));
  }

  private void outputPatch(AggregatedContext context, String outputLocation) {

    Path patchFile = Path.of(outputLocation, "test.patch");

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
}
