package io.steviemul.patch.commands;

import io.steviemul.patch.pipeline.PipelineRunner;
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

  @Override
  public Integer call() {

    PipelineRunner runner = PipelineRunner.builder()
        .resultsLocation(resultsFile)
        .sourceCodeRoot(root)
        .outputLocation(output)
        .build();

    runner.run();

    return 0;
  }
}
