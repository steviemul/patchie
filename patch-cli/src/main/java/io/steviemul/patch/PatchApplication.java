package io.steviemul.patch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class PatchApplication implements CommandLineRunner, ExitCodeGenerator {

    private final IFactory factory;
    private final PatchCommand patchCommand;
    private int exitCode;

    public PatchApplication(IFactory factory, PatchCommand patchCommand) {
        this.factory = factory;
        this.patchCommand = patchCommand;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(PatchApplication.class, args)));
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(patchCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
