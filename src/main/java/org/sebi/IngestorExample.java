package org.sebi;
import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.ollama.OllamaChatLanguageModel;
import io.quarkiverse.langchain4j.ollama.OllamaEmbeddingModel;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class IngestorExample {
     /**
     * The embedding store (the database).
     * The bean is provided by the quarkus-langchain4j-redis extension.
     */
    @Inject
    PgVectorEmbeddingStore store;

    /**
     * The embedding model (how the vector of a document is computed).
     * The bean is provided by the LLM (like openai) extension.
     */
    EmbeddingModel embeddingModel;

  public IngestorExample() {
    embeddingModel = OllamaEmbeddingModel.builder()
        .model("phi")
        .timeout(Duration.ofMinutes(2))
        .build();
  }

  public void ingest(@Observes StartupEvent event) {
        System.out.printf("Ingesting documents...%n");
        var path = new File("src/main/resources/catalog").toPath();
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(path, new TextDocumentParser(StandardCharsets.UTF_8));
        var ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(500, 0))
                .build();
        ingestor.ingest(documents); 
        System.out.printf("Ingested %d documents.%n", documents.size());
    }
}
