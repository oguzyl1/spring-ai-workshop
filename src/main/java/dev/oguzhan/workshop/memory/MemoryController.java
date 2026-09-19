package dev.oguzhan.workshop.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
public class MemoryController {

    private final ChatClient chatClient;

    public MemoryController(ChatClient.Builder builder, ChatMemory chatMemory) {
        /*
         * MessageChatMemoryAdvisor, aynı conversationId'ye ait önceki mesajları
         * ChatMemory üzerinden alıp yeni prompt'a otomatik olarak ekler.
         *
         * Spring AI varsayılan olarak bir ChatMemory bean'i sağlar.
         * Varsayılan yapı in-memory çalıştığı için uygulama yeniden başlatıldığında
         * konuşma hafızası kaybolur.
         */
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping
    public String memory(@RequestParam String message,
                         @RequestParam(defaultValue = "default-conversation") String conversationId) {

        /*
         * Spring AI 2.x ile conversationId her memory çağrısında açıkça verilmelidir.
         *
         * Aynı conversationId ile yapılan çağrılar aynı konuşma bağlamını paylaşır.
         * Farklı conversationId değerleri birbirinden bağımsız konuşmalar oluşturur.
         *
         * Buradaki sabit default değer yalnızca workshop/demo amacıyla kullanılıyor.
         * Gerçek uygulamada conversationId genellikle backend tarafından kullanıcı,
         * session veya gerçek bir conversation kaydı üzerinden üretilir.
         */
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

}
