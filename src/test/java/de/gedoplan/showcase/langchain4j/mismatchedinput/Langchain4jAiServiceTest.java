package de.gedoplan.showcase.langchain4j.mismatchedinput;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import java.time.Duration;

@QuarkusTest
class Langchain4jAiServiceTest {

  private static final String PROMPT = "Please write a 50 line long poem about Quarkus and repeat the whole poem three times.";

  @Inject
  @ConfigProperty(name = "de.gedoplan.ollama.base-url")
  String ollamaBaseUrl;

  @Test
  void testMistral() {
    testAiService("mistral:7b", false);
  }

  @Test
  void testMistralStreaming() {
    testAiService("mistral:7b", true);
  }

  @Test
  void testLlama() {
    testAiService("llama3.2:3b", false);
  }

  @Test
  void testLlamaStreaming() {
    testAiService("llama3.2:3b", true);
  }

  @Test
  void testGemma() {
    testAiService("gemma2:2b", false);
  }

  @Test
  void testGemmaStreaming() {
    testAiService("gemma2:2b", true);
  }

  private void testAiService(String modelName, boolean streaming) {
    Langchain4jAiService langchain4jAiService = AiServices.builder(Langchain4jAiService.class)
        .streamingChatLanguageModel(buildStreamingChatLanguageModel(modelName))
        .chatLanguageModel(buildChatLanguageModel(modelName))
        .build();

    if (streaming) {
      TokenStream streamingAnswer = langchain4jAiService.chatStreaming(PROMPT);
      streamingAnswer.onNext(
          token -> System.out.print(token)
      ).onComplete(
          message -> System.out.println("DONE")
      ).onError(
          throwable -> System.out.println("ERROR: " + throwable.getMessage())
      );
    } else {
      String answer = langchain4jAiService.chat(PROMPT);
      System.out.println(answer);
    }
  }

  private OllamaChatModel buildChatLanguageModel(String modelName) {
    return OllamaChatModel.builder()
        .baseUrl(ollamaBaseUrl)
        .modelName(modelName)
        .logRequests(true).logResponses(true)
        .timeout(Duration.ofSeconds(240))
        .build();
  }

  private OllamaStreamingChatModel buildStreamingChatLanguageModel(String modelName) {
    return OllamaStreamingChatModel.builder()
        .baseUrl(ollamaBaseUrl)
        .modelName(modelName)
        .logRequests(true).logResponses(true)
        .timeout(Duration.ofSeconds(240))
        .build();
  }
}