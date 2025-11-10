# Patchie CLI Application

A Demo Spring Boot CLI application using Picocli for command-line interface and Spring AI integration.

## Project Structure

```
patch/
├── patch-cli/          # Main CLI application module
├── pipeline /          # Runs steps end to end
├── resolver /          # Extracts code context
├── context/            # Context handling module
├── parser/             # Results parsing module  
└── generator/          # Patch generation module
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
Usage: patch [-hvV] [-f=<resultsFile>] [-l=<limit>] [-o=<output>]
             [-p=<provider>] [-r=<root>]
Patch CLI application with Spring Boot and AI integration
  -f, --file=<resultsFile>   Location of results file.
  -h, --help                 Show this help message and exit.
  -l, --limit=<limit>        Maximum number of chat calls to make, defaults to
                               unlimited.
  -o, --output=<output>      Location to output responses to, defaults to patches
                               directory.
  -p, --provider=<provider>  The chat provider to use, one of OLLAMA, OPENAI,
                               GEMINI, ANTHROPIC. Defauls to OLLAMA.
  -r, --root=<root>          Location of source code, defaults to CWD.
  -v, --verbose              Enable verbose output.
  -V, --version              Print version information and exit.
```

## Environment Variables

The following environment variables may be set
- PATCHIE_CHAT_BASE_URL : baseUrl of the chat provider specified.
- PATCHIE_CHAT_MODEL : chat model to use for the chat provider.
- PATCHIE_CHAT_API_KEY : API Key for the chat provider used.

## Chat Providers

Defaults are favoured to keep costs to a minimum.

As a result, the default chat provider used is **OLLAMA**. 
Use the compose file in the docker directory to start up a local instance.

When using a non ollama chat provider, the default chat model chosen is generally the
least expensive model for that provider. These may be updated using the environment variables.

### Chat Provider Defaults

The following defaults are set for the available chat providers and may be overwritten using the 
environment variables detailed above.

#### OLLAMA 
- Base URL : http://localhost:11434
- Model : **qwen2.5-coder:7b**
- See https://ollama.com/search for available models, use coder models like.
  - qwen2.5-coder:7b
  - mistral:7b
  - gemma2:9b

#### OpenAI
- Model : **gpt-5-mini**
- API Key : Set API Key env var to your Open AI Api Key
- See https://platform.openai.com/docs/models for available models.

#### Gemini
- Model : **gemini-2.5-flash**
- API Key : Set API Key env var to your Gemini Api Key
- See https://ai.google.dev/gemini-api/docs/models for available models.

#### Anthropic
- Model : **claude-haiku-4-5**
- API Key : Set API Key env var to your Anthropic Api Key
- See https://docs.claude.com/en/docs/about-claude/models/overview for available models.
