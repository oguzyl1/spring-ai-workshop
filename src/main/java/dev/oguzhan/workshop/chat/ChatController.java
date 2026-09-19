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
        /*
         * Spring Boot, yapılandırılmış ChatModel üzerinden bir ChatClient.Builder bean'i oluşturur.
         * Builder kullanarak provider/model ayarları uygulanmış ChatClient instance'ını elde ederiz.
         */
        this.chatClient = builder.build();
    }


    @GetMapping("/chat")
    public String chat() {
        /*
         * call() senkron çağrı modelini seçer.
         * content() ise model cevabından yalnızca üretilen metni döndürür.
         */
        return chatClient.prompt()
                .user("Bana java hakkında ilginç bir gerçek söyle")
                .call()
                .content();

    }


    @GetMapping(value = "/stream")
    public Flux<String> stream() {
        /*
         * stream() cevabı tek seferde beklemek yerine Flux<String> olarak parça parça döndürür.
         * delayElements() yalnızca streaming davranışını gözle görmek için eklenmiştir;
         * gerçek uygulamada yapay gecikme eklemek genellikle istenmez.
         */
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
        /*
         * content() yerine chatResponse() kullandığımızda yalnızca metni değil,
         * generation ve token kullanımı gibi ek metadata içeren tam ChatResponse nesnesini alırız.
         */
        return chatClient
                .prompt()
                .user("bana köpeklerle alakalı bir şaka yap")
                .call()
                .chatResponse();
    }


}