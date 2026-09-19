package dev.oguzhan.workshop.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acme")
public class AcmeBankController {

    private final ChatClient chatClient;

    public AcmeBankController(ChatClient.Builder builder) {
        /*
         * ChatClient.Builder, Spring AI tarafından auto-configuration ile hazırlanır.
         * Builder kullanarak provider/model ayarları uygulanmış bir ChatClient instance'ı oluştururuz.
         * Böylece Google GenAI client'ını manuel olarak oluşturmak zorunda kalmayız.
         */
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        /*
         * System prompt, modelin rolünü ve davranış sınırlarını tanımlar.
         * Burada modeli yalnızca bankacılık konularında cevap veren bir müşteri hizmetleri
         * asistanı gibi davranmaya yönlendiriyoruz.
         */
        var systemInstructions = """
                Sen AcmeBank için bir müşteri servis asistanısın.
                Sadece şunları konuşabilirsin:
                - Hesap bakiyeleri ve işlemler
                - Şube konumları ve çalışma saatleri
                - Genel bankacılık hizmetleri
                
                Başka herhangi bir şey sorulursa şu yanıtı ver: "Yalnızca bankacılık ile ilgili sorularda yardımcı olabilirim"
                """;

        /*
         * prompt() yeni bir prompt isteği başlatır.
         * user() kullanıcının mesajını,
         * system() ise modele uygulanacak üst seviye talimatları ekler.
         * call() isteği senkron olarak modele gönderir.
         * content() ise model cevabının yalnızca metin içeriğini döndürür.
         */
        return chatClient.prompt()
                .user(message)
                .system(systemInstructions)
                .call()
                .content();
    }
}
