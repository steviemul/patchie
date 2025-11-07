package io.steviemul.patch;

import io.steviemul.patch.service.PatchService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Component
@Command(
    name = "patch", 
    description = "Patch CLI application with Spring Boot and AI integration",
    mixinStandardHelpOptions = true,
    version = "1.0"
)
public class PatchCommand implements Callable<Integer> {

    private final PatchService patchService;

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    private boolean verbose;

    @Parameters(index = "0..*", description = "Input files or arguments")
    private String[] files;

    public PatchCommand(PatchService patchService) {
        this.patchService = patchService;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Patch CLI Application Started!");
        
        if (verbose) {
            System.out.println("Verbose mode enabled");
        }
        
        if (files != null && files.length > 0) {
            System.out.println("Processing files:");
            for (String file : files) {
                System.out.println("  - " + file);
                // Use injected Spring service
                patchService.processPatch(file);
            }
        } else {
            System.out.println("No files provided. Use --help for usage information.");
        }
        
        return 0; // Success exit code
    }
}