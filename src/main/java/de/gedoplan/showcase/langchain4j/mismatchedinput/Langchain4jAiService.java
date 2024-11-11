package de.gedoplan.showcase.langchain4j.mismatchedinput;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import io.smallrye.mutiny.Multi;

public interface Langchain4jAiService {
  String chat(@UserMessage String message);
  TokenStream chatStreaming(@UserMessage String message);
}
