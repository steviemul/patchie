package io.steviemul.patchie.commands;

import io.steviemul.patchie.pipeline.PipelineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Component
@Command(
    name = "patch",
    description = "Patch CLI application with Spring Boot and AI integration",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class PatchCommand implements Callable<Integer> {

  private final PipelineRunner runner;

  @Option(
      names = {"-v", "--verbose"},
      description = "Enable verbose output")
  private boolean verbose;

  @Option(
      names = {"-f", "--file"},
      description = "Location of results file")
  private String resultsFile;

  @Option(
      names = {"-r", "--root"},
      description = "Location of source code")
  private String root;

  @Option(
      names = {"-o", "--output"},
      description = "Location to output patches to")
  private String output;

  public PatchCommand(PipelineRunner runner) {
    this.runner = runner;
  }

  @Override
  public Integer call() {

    runner.run(resultsFile, root, output);

    return 0;
  }
}
