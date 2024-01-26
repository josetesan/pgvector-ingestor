package org.sebi;

import java.time.Duration;
import java.util.List;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import io.quarkiverse.langchain4j.ollama.OllamaEmbeddingModel;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("retrieve")
public class RetrieverResource {
     private final EmbeddingStoreRetriever retriever;

    RetrieverResource(PgVectorEmbeddingStore store) {
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
            .model("phi")
            .timeout(Duration.ofMinutes(2))
            .build();
        retriever = EmbeddingStoreRetriever.from(store, embeddingModel, 20);
    }

    @GET
    public List<TextSegment> findRelevant() {
        return retriever.findRelevant("Elite");
    }
}
