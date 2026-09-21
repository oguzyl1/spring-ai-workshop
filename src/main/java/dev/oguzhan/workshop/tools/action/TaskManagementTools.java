package dev.oguzhan.workshop.tools.action;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaskManagementTools {

    public record TaskResult(
            Long taskId,
            String title,
            String status,
            String assignee,
            String message
    ) {
    }

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public record Task(
            Long id,
            String title,
            String description,
            String assignee,
            TaskStatus status
    ) {
    }

    /*
     * Demo amacıyla görevler bellekte tutuluyor.
     *
     * ConcurrentHashMap aynı anda birden fazla request geldiğinde
     * temel thread-safety sağlar. Gerçek uygulamada bunun yerine
     * repository/database kullanılır.
     */
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    /*
     * AtomicLong, eş zamanlı request'lerde benzersiz task ID üretmek için
     * thread-safe bir sayaç olarak kullanılır.
     */
    private final AtomicLong taskIdGenerator = new AtomicLong(1);


    @Tool(description = "Başlık, açıklama ve atanacak kişi bilgilerini kullanarak yeni bir görev oluşturur.")
    public TaskResult createTask(
            @ToolParam(description = "Görevin kısa başlığı") String title,
            @ToolParam(description = "Görevin yapılacak işi açıklayan detaylı açıklaması") String description,
            @ToolParam(description = "Görevin atanacağı kişinin adı") String assignee) {

        Long taskId = taskIdGenerator.getAndIncrement();
        Task task = new Task(taskId, title, description, assignee, TaskStatus.PENDING);
        tasks.put(taskId, task);

        /*
         * Gerçek bir uygulamada burada repository üzerinden veritabanına
         * kayıt yapılabilir ve ilgili kişiye bildirim gönderilebilir.
         */
        return new TaskResult(taskId, title, "PENDING", assignee, "Görev başarıyla oluşturuldu ve " + assignee + " kişisine atandı.");
    }

    @Tool(description = "Mevcut bir görevin durumunu task ID kullanarak günceller.")
    public TaskResult updateStatus(
            @ToolParam(description = "Güncellenecek görevin ID değeri") Long taskId,
            @ToolParam(description = "Yeni görev durumu: PENDING, IN_PROGRESS, COMPLETED veya CANCELLED")
            TaskStatus status) {

        Task existingTask = tasks.get(taskId);
        if (existingTask == null) {
            return new TaskResult(taskId, "", "ERROR", "", "Görev bulunamadı");
        }

        Task updatedTask = new Task(existingTask.id(), existingTask.title(), existingTask.description(), existingTask.assignee(), status);
        tasks.put(taskId, updatedTask);

        /*
         * Gerçek bir uygulamada burada database update, workflow tetikleme
         * veya notification gibi işlemler yapılabilir.
         */
        return new TaskResult(taskId, updatedTask.title(), status.toString(), updatedTask.assignee(), "Görev durumu şuna güncellendi: " + status);
    }


    @Tool(description = "Mevcut bir görevi farklı bir kişiye atar veya yeniden atar.")
    public TaskResult assignTask(
            @ToolParam(description = "Yeniden atanacak görevin ID değeri") Long taskId,
            @ToolParam(description = "Görevin atanacağı yeni kişinin adı") String newAssignee) {

        Task existingTask = tasks.get(taskId);
        if (existingTask == null) {
            return new TaskResult(taskId, "", "ERROR", "", "Görev bulunamadı");
        }

        Task updatedTask = new Task(existingTask.id(), existingTask.title(), existingTask.description(), newAssignee, existingTask.status());
        tasks.put(taskId, updatedTask);

        /*
         * Gerçek bir uygulamada database güncellenebilir ve yeni görev
         * sahibine bildirim gönderilebilir.
         */
        return new TaskResult(taskId, updatedTask.title(), updatedTask.status().toString(), newAssignee, "Görev şuna yeniden atandı: " + newAssignee);
    }


}
