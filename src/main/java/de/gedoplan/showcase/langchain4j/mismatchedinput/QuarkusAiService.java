package de.gedoplan.showcase.langchain4j.mismatchedinput;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface QuarkusAiService {
  String chat(@UserMessage String message);
  Multi<String> chatStreaming(@UserMessage String message);
}
