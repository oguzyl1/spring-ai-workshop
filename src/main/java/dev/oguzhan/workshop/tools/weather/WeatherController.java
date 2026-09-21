package dev.oguzhan.workshop.tools.weather;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final ChatClient chatClient;
    private final WeatherTools weatherTools;

    public WeatherController(ChatClient.Builder builder, WeatherTools weatherTools) {
        this.chatClient = builder.build();
        this.weatherTools = weatherTools;
    }

    /*
     * Kullanıcı hava tahmini veya aktif hava uyarısı hakkında doğal
     * dilde bir soru gönderir.
     *
     * Model, WeatherTools içindeki tool açıklamalarını inceleyerek
     * hangi tool'un çağrılması gerektiğine karar verir.
     */
    @GetMapping
    public String getAlerts(@RequestParam String message) {
        return chatClient.prompt()
                .tools(weatherTools)
                .user(message)
                .call()
                .content();
    }
}