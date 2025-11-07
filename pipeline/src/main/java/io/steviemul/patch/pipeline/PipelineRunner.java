package io.steviemul.patch.pipeline;

import io.steviemul.patch.context.AggregatedContext;
import io.steviemul.patch.generator.PatchContextProvider;
import io.steviemul.patch.parser.SarifContextProvider;
import io.steviemul.patch.resolver.CodeContextProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PipelineRunner {

  private final PatchContextProvider patchContextProvider;

  public void run(
      String resultsLocation,
      String sourceCodeRoot,
      String outputLocation
  ) {

    SarifContextProvider sarifContextProvider = SarifContextProvider.builder()
        .resultsLocation(resultsLocation)
        .build();

    List<AggregatedContext> context = sarifContextProvider.getInitialContext();

    CodeContextProvider codeContextProvider = CodeContextProvider.builder()
        .sourceCodeRoot(sourceCodeRoot)
        .build();

    context = context.stream().map(codeContextProvider::addContext).toList();

    context = context.stream().map(patchContextProvider::addContext).toList();

    context.forEach(c -> outputPatch(c, outputLocation));
  }

  private void outputPatch(AggregatedContext context, String outputLocation) {

    Path patchFile = Path.of(outputLocation, "test.patch");

    try {
      Files.writeString(patchFile, context.getPatch(), StandardCharsets.UTF_8);
    }
    catch (Exception e) {
      log.error("Unable to create patch file", e);
    }
  }
}
