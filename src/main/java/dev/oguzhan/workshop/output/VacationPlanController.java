package dev.oguzhan.workshop.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vacation")
public class VacationPlanController {

    private final ChatClient chatClient;

    public VacationPlanController(ChatClient.Builder builder) {
        /*
         * Spring AI tarafından auto-configure edilen ChatClient.Builder üzerinden
         * yapılandırılmış ChatClient instance'ını oluşturuyoruz.
         */
        this.chatClient = builder.build();
    }

    @GetMapping("/unstructured")
    public String unstructured() {
        /*
         * content() kullanıldığında model cevabını ham metin olarak alırız.
         * Uygulamanın bu metindeki alanları ayrıca parse etmesi gerekir.
         */
        return chatClient.prompt()
                .user(""" 
                        Muğlaya bir gezi planı yapmak istiyorum.
                        Bana yapılacak şeyler listesi verir misin?
                        """)
                .call()
                .content();
    }

    @GetMapping("/structured")
    public Itinerary structured() {
        /*
         * entity() model çıktısını doğrudan Java tipine dönüştürür.
         *
         * useProviderStructuredOutput():
         * JSON schema'yı Gemini API'ye göndererek yapının provider seviyesinde
         * uygulanmasını sağlar.
         *
         * validateSchema():
         * Dönen cevabı schema'ya göre doğrular. Geçersiz çıktı gelirse
         * Spring AI modeli hatayı düzeltecek şekilde tekrar çağırabilir.
         */
        return chatClient.prompt()
                .user(""" 
                        Muğlaya bir gezi planı yapmak istiyorum.
                        Bana yapılacak şeyler listesi verir misin?
                        """)
                .call()
                .entity(Itinerary.class,
                        spec -> spec
                                .useProviderStructuredOutput()
                                .validateSchema()
                );
    }
}
