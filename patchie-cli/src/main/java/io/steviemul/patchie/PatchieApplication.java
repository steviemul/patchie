package io.steviemul.patchie;

import io.steviemul.patchie.commands.PatchCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class PatchieApplication implements CommandLineRunner, ExitCodeGenerator {

  private final IFactory factory;
  private final PatchCommand patchCommand;
  private int exitCode;

  public PatchieApplication(IFactory factory, PatchCommand patchCommand) {
    this.factory = factory;
    this.patchCommand = patchCommand;
  }

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(PatchieApplication.class, args)));
  }

  @Override
  public void run(String... args) {
    exitCode = new CommandLine(patchCommand, factory).execute(args);
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }
}
