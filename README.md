# quarkus-langchain4j-mismatchedinput-reproducer

Simple project to reproduce MismatchedInputException when using specific LLMs with Ollama with the Quarkus-Langchain4j Integration.

## Langchain4jAiServiceTest

The tests in this class use the Langchain4j Ollama implementation.
All tests work for me.

## QuarkusAiServiceTest

The tests in this class use the Quarkus-Langchain4j Ollama implementation.
All non-streaming tests work for me.
From the streaming tests only the mistral test case works for me, llama3.2 and gemma2 fail with the mentioned MismatchedInputException.