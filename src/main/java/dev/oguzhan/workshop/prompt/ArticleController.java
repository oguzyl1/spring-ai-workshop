package dev.oguzhan.workshop.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(ChatClient.Builder builder) {
        /*
         * Spring AI'nin oluşturduğu Builder üzerinden ChatClient üretiyoruz.
         * Bu yaklaşım, model/provider konfigürasyonunun Spring tarafından yönetilmesini sağlar.
         */
        this.chatClient = builder.build();
    }

    @GetMapping("/post/new")
    public String newPost(@RequestParam(value = "topic", defaultValue = "JDK Virtual Threads") String topic) {
        /*
         * System prompt ile modelin nasıl bir içerik üretmesi gerektiğini belirliyoruz:
         * uzunluk, yapı, ton, stil ve çıktı formatı gibi kurallar burada tanımlanıyor.
         *
         * Bu kurallar her kullanıcı isteğinde sabit kalırken,
         * konu bilgisi user prompt üzerinden dinamik olarak geliyor.
         */
        var system = """
                Blog Yazısı Oluşturma Kuralları:
                
                1. Uzunluk ve Amaç: Genel okuyucu kitlesini bilgilendiren ve ilgisini çeken 500 kelimelik blog yazıları oluştur.
                
                2. Yapı:
                   - Giriş: Okuyucunun ilgisini çek ve konunun neden önemli olduğunu açıkla
                   - Ana Bölüm: Destekleyici kanıtlar ve örneklerle 3 ana noktayı geliştir
                   - Sonuç: Temel çıkarımları özetle ve okuyucuyu harekete geçirecek bir çağrı ekle
                
                3. İçerik Gereksinimleri:
                   - Gerçek dünya uygulamalarına veya vaka çalışmalarına yer ver
                   - Uygun olduğunda ilgili istatistikleri veya verileri kullan
                   - Faydaları ve olası etkileri teknik olmayan okuyucular için açık şekilde anlat
                
                4. Üslup ve Stil:
                   - Bilgilendirici ancak sohbet havasında bir dil kullan
                   - Yetkin ve güvenilir bir anlatımı korurken anlaşılır bir dil kullan
                   - Metni alt başlıklar ve kısa paragraflarla böl
                
                5. Yanıt Formatı: Önerilen bir başlıkla birlikte, doğrudan yayınlanmaya hazır eksiksiz bir blog yazısı oluştur.
                """;

        /*
         * Prompt template içinde {topic} isimli bir placeholder kullanıyoruz.
         * param() ile request'ten gelen gerçek değeri bu alana yerleştiriyoruz.
         *
         * Bu yaklaşım string birleştirmeye göre daha okunabilir ve
         * prompt büyüdüğünde yönetmesi daha kolaydır.
         */
        return chatClient.prompt()
                .user(u-> {
                    u.text("Bana {topic} hakkında bir blog yazısı oluştur");
                    u.param("topic", topic);

                })
                .system(system)
                .call()
                .content();
    }
}
