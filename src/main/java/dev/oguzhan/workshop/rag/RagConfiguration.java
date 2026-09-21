package dev.oguzhan.workshop.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);
    private static final String VECTOR_STORE_FILE = "vectorstore.json";

    @Value("classpath:/data/models.json")
    private Resource modelsResource;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {

        /*
         * SimpleVectorStore, workshop/demo amacıyla kullanılan basit bir vector store'dur.
         *
         * EmbeddingModel ise metinleri sayısal vektörlere dönüştürmek için kullanılır.
         * Bu projede Google GenAI embedding modeli Spring AI tarafından otomatik
         * olarak oluşturulup buraya inject edilir.
         */
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            /*
             * Daha önce oluşturulmuş embedding'leri tekrar hesaplamak yerine
             * local JSON dosyasından vector store'u yüklüyoruz.
             */
            log.info("Mevcut vector store yükleniyor: {}", vectorStoreFile.getAbsolutePath());
            vectorStore.load(vectorStoreFile);

        } else {
            /*
             * İlk çalıştırmada kaynak dosya okunur, parçalara ayrılır,
             * embedding'leri oluşturulur ve vector store'a eklenir.
             */
            log.info("Yeni vector store oluşturuluyor: {}", vectorStoreFile.getAbsolutePath());
            TextReader textReader = new TextReader(modelsResource);
            textReader.getCustomMetadata().put("filename", "models.json");
            List<Document> documents = textReader.get();

            /*
             * Spring AI 2.x'te builder kullanımı tercih edilir.
             * TokenTextSplitter büyük metinleri embedding için daha küçük
             * chunk'lara ayırır.
             */
            TokenTextSplitter textSplitter = TokenTextSplitter.builder().build();
            List<Document> chunks = textSplitter.apply(documents);
            /*
             * add() sırasında her chunk EmbeddingModel'e gönderilir.
             * Dönen sayısal vektörler SimpleVectorStore içerisinde tutulur.
             */
            vectorStore.add(chunks);
            /*
             * Demo amacıyla vector store'u local JSON dosyasına kaydediyoruz.
             * Böylece her application restart'ta tekrar embedding üretmeyiz.
             */
            vectorStore.save(vectorStoreFile);
        }
        return vectorStore;
    }

    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        return path.resolve(VECTOR_STORE_FILE).toFile();
    }
}