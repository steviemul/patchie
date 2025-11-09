package io.steviemul.patchie.pipeline;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.generator.PatchGenerator;
import io.steviemul.patchie.parser.SarifContextProvider;
import io.steviemul.patchie.resolver.CodeContextProvider;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PipelineRunner {

  private final PatchGenerator patchGenerator;

  public void run(PipelineConfig config) {

    log.info("Reading results file {}", config.getResultsFile());

    SarifContextProvider sarifContextProvider =
        SarifContextProvider.builder().resultsLocation(config.getResultsFile()).build();

    List<AggregatedContext> context = sarifContextProvider.getInitialContext();

    log.info("Found {} issues", context.size());

    CodeContextProvider codeContextProvider =
        CodeContextProvider.builder().sourceCodeRoot(config.getRoot()).build();

    context = context.stream().map(codeContextProvider::addContext).toList();

    Stream<AggregatedContext> patchContextStream =
        context.stream().filter(c -> c.getCode() != null);

    if (config.getMaximumPatches() > 0) {
      patchContextStream = patchContextStream.limit(config.getMaximumPatches());
    }

    try {
      List<Future<AggregatedContext>> futures =
          patchContextStream
              .map(c -> patchGenerator.generatePatch(c, config.getOutputLocation()))
              .toList();

      List<AggregatedContext> results =
          futures.stream().map(this::getContext).flatMap(Optional::stream).toList();

      log.info("Generated {} issues", results.size());
    } catch (Exception e) {
      log.error("Error retrieving results", e);
    }
  }

  private Optional<AggregatedContext> getContext(Future<AggregatedContext> future) {

    try {
      return Optional.of(future.get());
    } catch (Exception e) {
      log.error("Error retrieving result", e);
    }

    return Optional.empty();
  }
}
