package io.steviemul.patchie.resolver;

import io.steviemul.patchie.context.AggregatedContext;
import io.steviemul.patchie.context.ContextProvider;
import io.steviemul.patchie.context.ResultLocation;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Builder
@Slf4j
public class CodeContextProvider implements ContextProvider {

  private static final int LINES_AROUND = 5;
  private final String sourceCodeRoot;

  @Override
  public AggregatedContext addContext(AggregatedContext aggregatedContext) {

    if (aggregatedContext.getResultLocation() != null) {
      String code = getSourceCode(aggregatedContext.getResultLocation());

      aggregatedContext.setCode(code);
    }

    return aggregatedContext;
  }

  private String getSourceCode(ResultLocation resultLocation) {

    try {
      Path sourceCodePath = Path.of(sourceCodeRoot, resultLocation.getFile());

      if (Files.exists(sourceCodePath)) {
        List<String> lines = Files.readAllLines(sourceCodePath, StandardCharsets.UTF_8);

        log.info(
            "Adding source code context for file at line : [{}, {}]",
            resultLocation.getFile(),
            resultLocation.getStartLine());

        return getLines(lines, resultLocation.getStartLine(), resultLocation.getEndLine());
      } else {
        log.error("Source code not found: {}", sourceCodePath);
      }
    } catch (Exception e) {
      log.error("Error reading source code file: {}", sourceCodeRoot, e);
    }

    return null;
  }

  private String getLines(List<String> lines, int startLine, int endLine) {

    int start = Math.max((startLine - LINES_AROUND), 0);
    int end = Math.min((endLine + LINES_AROUND), lines.size());

    return String.join("\n", lines.subList(start, end));
  }
}
