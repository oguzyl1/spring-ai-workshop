package dev.oguzhan.workshop.tools.action;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskManagementController {

    private final ChatClient chatClient;
    private final TaskManagementTools taskManagerTools;

    public TaskManagementController(ChatClient.Builder builder, TaskManagementTools taskManagerTools) {
        this.chatClient = builder.build();
        this.taskManagerTools = taskManagerTools;
    }

    /*
     * Kullanıcının doğal dilde verdiği talep modele gönderilir.
     *
     * Model, isteğin içeriğine göre createTask, updateStatus veya
     * assignTask tool'larından hangisinin çağrılması gerektiğine
     * ve hangi argümanların kullanılacağına karar verir.
     */
    @GetMapping
    public String createTask(@RequestParam String message) {
        return chatClient.prompt()
                .tools(taskManagerTools)
                .user(message)
                .call()
                .content();
    }

}
