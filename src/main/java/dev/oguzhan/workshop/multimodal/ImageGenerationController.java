package dev.oguzhan.workshop.multimodal;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/image/generator")
public class ImageGenerationController {

    private final ImageModel imageModel;

    public ImageGenerationController(ImageModel imageModel) {
        /*
         * Spring AI, application.yaml içindeki Google GenAI image ayarlarına göre
         * uygun ImageModel implementasyonunu otomatik olarak oluşturur.
         *
         * Controller doğrudan Google SDK'sına değil, Spring AI'nin ortak
         * ImageModel abstraction'ına bağımlıdır.
         */
        this.imageModel = imageModel;
    }

    @GetMapping("/generate-image")
    public Map<String, Object> generateImage(@RequestParam(defaultValue = "Dağların üzerinde güzel bir gün batımı") String prompt) {
        /*
         * ImagePrompt, modele gönderilecek metinsel görsel üretim isteğini temsil eder.
         * Model ve boyut gibi varsayılan seçenekler application.yaml üzerinden gelir.
         *
         * call() image generation isteğini senkron olarak modele gönderir.
         *
         * ImageResponse bir veya daha fazla ImageGeneration içerebilir.
         * Gemini çıktısı URL yerine Base64 image data döndürebileceği için
         * burada tüm response'u döndürerek örneği provider bağımsız tutuyoruz.
         */
        ImagePrompt imagePrompt = new ImagePrompt(prompt);
        ImageResponse imageResponse = imageModel.call(imagePrompt);
        return Map.of("prompt", prompt, "response", imageResponse
        );
    }
}