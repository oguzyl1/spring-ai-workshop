package dev.oguzhan.workshop.tools.datetime;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tools")
public class DateTimeController {

    private final ChatClient chatClient;

    public DateTimeController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    /*
     * DateTimeTools, yalnızca bu request için modele kullanılabilir
     * tool'lar arasında eklenir.
     *
     * Model kullanıcı sorusunu değerlendirir ve güncel tarih/saat
     * bilgisine ihtiyaç duyduğunu anlarsa getCurrentTime() metodunu
     * çağırmaya karar verebilir.
     */
    @GetMapping
    public String tools(){
        return chatClient.prompt()
                .user("yarının tarihi nedir?")
                .tools(new DateTimeTools())
                .call()
                .content();
    }
}
