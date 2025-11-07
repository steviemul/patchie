# Patchie CLI Application

A Spring Boot CLI application using Picocli for command-line interface and Spring AI integration.

## Project Structure

```
patch/
├── patch-cli/          # Main CLI application module
├── context/            # Context handling module
├── parser/             # Code parsing module  
└── generator/          # Code generation module
```

## Features

- **Spring Boot Integration**: Full Spring context with dependency injection
- **Picocli CLI**: Command-line interface with help, options, and arguments
- **Spring AI Ready**: Configured for Spring AI integration
- **Modular Architecture**: Multi-module Maven project structure

## Dependencies

- Spring Boot 3.5.3
- Picocli 4.7.5 with Spring Boot integration
- Spring AI 1.0.1 (ready for integration)
- Java 21

## Building

```bash
# Build all modules
mvn clean compile

# Package standalone JAR
mvn clean package
```

## Running

### Using Maven
```bash
# Show help
mvn spring-boot:run -Dspring-boot.run.arguments="--help"

# Run with verbose flag and files
mvn spring-boot:run -Dspring-boot.run.arguments="--verbose file1.txt file2.txt"
```

### Using Standalone JAR
```bash
# Show help
java -jar patch-cli/target/patch-cli-1.0-SNAPSHOT.jar --help

# Run with arguments
java -jar patch-cli/target/patch-cli-1.0-SNAPSHOT.jar --verbose myfile.java
```

## Usage

```
Usage: patch [-hvV] [<files>...]
Patch CLI application with Spring Boot and AI integration
      [<files>...]   Input files or arguments
  -h, --help         Show this help message and exit.
  -v, --verbose      Enable verbose output
  -V, --version      Print version information and exit.
```

## Development

The application is set up with:
- Spring Boot DevTools for hot reloading during development
- Proper exit code handling for CLI usage
- Logging configuration optimized for CLI output
- Annotation processing for Picocli code generation