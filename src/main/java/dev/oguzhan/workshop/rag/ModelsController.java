package dev.oguzhan.workshop.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
public class ModelsController {


    private final ChatClient chatClient;

    public ModelsController(ChatClient.Builder builder, VectorStore vectorStore) {

        /*
         * QuestionAnswerAdvisor temel bir RAG akışı sağlar.
         *
         * Kullanıcı sorusu geldiğinde vector store üzerinde benzerlik araması
         * yapar, ilgili Document chunk'larını bulur ve bu içerikleri
         * chat modeline gönderilen prompt'a context olarak ekler.
         */
        QuestionAnswerAdvisor questionAnswerAdvisor =
                QuestionAnswerAdvisor
                        .builder(vectorStore)
                        .build();

        this.chatClient = builder
                .defaultAdvisors(questionAnswerAdvisor)
                .build();
    }

    @GetMapping("/models")
    public Models faq(@RequestParam(value = "message", defaultValue = "Bana OpenAI'ın tüm modellerinin bir listesini, bağlam pencereleri (context window) ile birlikte ver") String message) {

        /*
         * Akış:
         *
         * 1. Kullanıcı sorusu embedding'e çevrilir.
         * 2. Vector store'da benzer chunk'lar aranır.
         * 3. Bulunan içerikler prompt'a context olarak eklenir.
         * 4. Gemini bu context ve kullanıcı sorusuna göre cevap üretir.
         * 5. Cevap Models tipine dönüştürülür.
         */
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(Models.class,
                        spec -> spec
                                .useProviderStructuredOutput()
                                .validateSchema()
                );
    }


}
