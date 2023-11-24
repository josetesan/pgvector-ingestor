package org.sebi;

import java.util.List;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import io.quarkiverse.langchain4j.pgvector.PgVectorEmbeddingStore;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("retrieve")
public class RetrieverResource {
     private final EmbeddingStoreRetriever retriever;

    RetrieverResource(PgVectorEmbeddingStore store, EmbeddingModel model) {
        retriever = EmbeddingStoreRetriever.from(store, model, 20);
    }

    @GET
    public List<TextSegment> findRelevant() {
        return retriever.findRelevant("Elite");
    }
}
