package dev.oguzhan.workshop.multimodal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image/detection")
public class ImageDetectionController {

    private final ChatClient chatClient;
    @Value("classpath:/images/image1.jpg")
    Resource imageResource;


    public ImageDetectionController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/image-to-text")
    public String imageToText() {
        return chatClient.prompt()
                .user(u-> {
                    u.text("Lütfen bana ilgili resimde ne gördüğünü açıklar mısın?");
                    u.media(MimeTypeUtils.IMAGE_JPEG, imageResource);
                })
                .call()
                .content();
    }

}
