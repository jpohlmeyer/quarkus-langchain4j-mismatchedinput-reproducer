package de.gedoplan.showcase.langchain4j.mismatchedinput;

import io.quarkiverse.langchain4j.QuarkusAiServicesFactory;
import io.quarkiverse.langchain4j.ollama.OllamaChatLanguageModel;
import io.quarkiverse.langchain4j.ollama.OllamaStreamingChatLanguageModel;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import java.time.Duration;

@QuarkusTest
class QuarkusAiServiceTest {

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
    QuarkusAiService quarkusAiService = QuarkusAiServicesFactory.QuarkusAiServices.builder(QuarkusAiService.class)
        .chatLanguageModel(buildChatLanguageModel(modelName))
        .streamingChatLanguageModel(buildStreamingChatLanguageModel(modelName))
        .build();

    if (streaming) {
      Multi<String> streamingAnswer = quarkusAiService.chatStreaming(PROMPT);
      streamingAnswer.subscribe().asStream().forEach(System.out::print);
    } else {
      String answer = quarkusAiService.chat(PROMPT);
      System.out.println(answer);
    }
  }

  private OllamaChatLanguageModel buildChatLanguageModel(String modelName) {
    return OllamaChatLanguageModel.builder()
        .baseUrl(ollamaBaseUrl)
        .model(modelName)
        .logRequests(true).logResponses(true)
        .timeout(Duration.ofSeconds(240))
        .build();
  }

  private OllamaStreamingChatLanguageModel buildStreamingChatLanguageModel(String modelName) {
    return OllamaStreamingChatLanguageModel.builder()
        .baseUrl(ollamaBaseUrl)
        .model(modelName)
        .logRequests(true).logResponses(true)
        .timeout(Duration.ofSeconds(240))
        .build();
  }
}