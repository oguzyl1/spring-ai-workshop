package dev.oguzhan.workshop.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    @GetMapping("/chat")
    public String chat() {
        return chatClient.prompt()
                .user("Bana java hakkında ilginç bir gerçek söyle")
                .call()
                .content();

    }


    @GetMapping(value = "/stream")
    public Flux<String> stream() {
        return chatClient.prompt()
                .user("""
                        Yakın zamanda İstanbul'daki işim için taşınmam gerekecek.
                        Bana uygun fiyatlı ev bulabileceğim tam olarak 5 bölge söyle.
                        """)
                .stream()
                .content()
                .delayElements(Duration.ofMillis(250));
    }


    @GetMapping("/joke")
    public ChatResponse joke(){
        return chatClient
                .prompt("bana köpeklerle alakalı bir şaka yap")
                .call()
                .chatResponse();
    }


}