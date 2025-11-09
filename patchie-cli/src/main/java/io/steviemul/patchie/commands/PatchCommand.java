package io.steviemul.patchie.commands;

import io.steviemul.patchie.pipeline.PipelineConfig;
import io.steviemul.patchie.pipeline.PipelineRunner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;

@Component
@Command(
    name = "patch",
    description = "Patch CLI application with Spring Boot and AI integration",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class PatchCommand implements Callable<Integer> {

  private final PipelineRunner runner;

  private static final String DEFAULT_OUTPUT = "patches";
  private static final String DEFAULT_RESULTS_FILE = "sarif.json";

  @CommandLine.Spec private CommandLine.Model.CommandSpec spec;

  @Option(
      names = {"-v", "--verbose"},
      description = "Enable verbose output")
  private boolean verbose;

  @Option(
      names = {"-f", "--file"},
      description = "Location of results file, defaults to sarif.json")
  private String resultsFile;

  @Option(
      names = {"-r", "--root"},
      description = "Location of source code, defaults to CWD")
  private String root;

  @Option(
      names = {"-o", "--output"},
      description = "Location to output patches to, defaults to patches directory")
  private String output;

  @Option(
      names = {"-l", "--limit"},
      description = "Maximum number of patches to generate, defaults to unlimited")
  private int limit = -1;

  public PatchCommand(PipelineRunner runner) {
    this.runner = runner;
  }

  @Override
  public Integer call() {

    checkInput();

    PipelineConfig config =
        PipelineConfig.builder()
            .root(root)
            .resultsFile(resultsFile)
            .outputLocation(output)
            .maximumPatches(limit)
            .build();

    runner.run(config);

    return 0;
  }

  private void checkInput() {

    if (!isBlank(resultsFile)) {
      if (!Files.exists(Paths.get(resultsFile))) {
        throw new ParameterException(spec.commandLine(), "Specified results file does not exist");
      }
    } else {
      if (!Files.exists(Paths.get(DEFAULT_RESULTS_FILE))) {
        throw new ParameterException(spec.commandLine(), "Default results file does not exist");
      }

      resultsFile = DEFAULT_RESULTS_FILE;
    }

    if (isBlank(root)) {
      root = Paths.get(".").toAbsolutePath().toString();
    }

    if (isBlank(output)) {
      output = Paths.get(".", DEFAULT_OUTPUT).toAbsolutePath().toString();
    }
  }

  private boolean isBlank(String string) {
    return string == null || string.trim().isEmpty();
  }
}
