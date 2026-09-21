package dev.oguzhan.workshop.byod;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/models")
public class ModelComparison {

    private final ChatClient chatClient;

    public ModelComparison(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    public String models() {
        /*
         * Bu endpoint modele yalnızca kullanıcı sorusunu gönderir.
         *
         * Harici bir veri kaynağı veya ek context verilmediği için model,
         * cevabını kendi eğitim bilgisine dayanarak üretir.
         *
         * Bu yaklaşım güncel veya uygulamaya özel bilgiler gerektiğinde
         * yetersiz kalabilir.
         */

        return chatClient.prompt()
                .user("""
                        Bana popüler büyük dil modellerinin güncel listesini ve
                        mevcut bağlam pencerelerini verir misin?
                        """)
                .call()
                .content();
    }

    @GetMapping("/stuff-the-prompt")
    public String modelsStuffThePrompt() {

        /*
         * "Stuff the Prompt" yaklaşımında dışarıdan sahip olduğumuz veriyi
         * doğrudan prompt içerisine ekliyoruz.
         *
         * Böylece model yalnızca kendi eğitim bilgisinden değil,
         * request sırasında sağlanan ek context'ten de yararlanabilir.
         *
         * Küçük veri kümeleri için basit ve kullanışlıdır ancak veri büyüdükçe
         * tüm içeriği her request'te prompt'a eklemek maliyetli ve verimsiz olur.
         *
         * RAG yaklaşımı bu problemi, yalnızca kullanıcı sorusuyla ilgili
         * parçaları dinamik olarak bulup prompt'a ekleyerek çözer.
         */
        var system = """
                       Eğer sana güncel dil modelleri ve bu modellerin context window boyutları hakkında soru sorulursa,
                       cevabını hazırlarken aşağıdaki bilgileri kullan:
                [
                                 {
                                   "company": "OpenAI",
                                   "model": "GPT-4o",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "OpenAI",
                                   "model": "GPT-4o mini",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "OpenAI",
                                   "model": "GPT-4 Turbo",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "OpenAI",
                                   "model": "o1-preview",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "OpenAI",
                                   "model": "o1",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "OpenAI",
                                   "model": "GPT-5",
                                   "context_window_size": 400000
                                 },
                                 {
                                   "company": "Anthropic",
                                   "model": "Claude Sonnet 4.5",
                                   "context_window_size": 200000
                                 },
                                 {
                                   "company": "Anthropic",
                                   "model": "Claude Opus 4.1",
                                   "context_window_size": 200000
                                 },
                                 {
                                   "company": "Anthropic",
                                   "model": "Claude Haiku 4.5",
                                   "context_window_size": 200000
                                 },
                                 {
                                   "company": "Anthropic",
                                   "model": "Claude 3.5 Sonnet",
                                   "context_window_size": 200000
                                 },
                                 {
                                   "company": "Anthropic",
                                   "model": "Claude 3 Opus",
                                   "context_window_size": 200000
                                 },
                                 {
                                   "company": "Google",
                                   "model": "Gemini 2.5 Pro",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Google",
                                   "model": "Gemini 2.5 Flash",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Google",
                                   "model": "Gemini 2.0 Pro",
                                   "context_window_size": 2000000
                                 },
                                 {
                                   "company": "Google",
                                   "model": "Gemini 2.0 Flash",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Google",
                                   "model": "Gemini 3 Pro",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Meta AI",
                                   "model": "Llama 3.3 70B",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Meta AI",
                                   "model": "Llama 3.2 3B",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Meta AI",
                                   "model": "Llama 3.1 405B",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "xAI",
                                   "model": "Grok 4 Fast",
                                   "context_window_size": 2000000
                                 },
                                 {
                                   "company": "xAI",
                                   "model": "Grok 4",
                                   "context_window_size": 256000
                                 },
                                 {
                                   "company": "xAI",
                                   "model": "Grok 2",
                                   "context_window_size": 131072
                                 },
                                 {
                                   "company": "Mistral AI",
                                   "model": "Mistral Large 2",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Mistral AI",
                                   "model": "Mistral Small 3.1",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Mistral AI",
                                   "model": "Mistral NeMo 12B",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Mistral AI",
                                   "model": "Mixtral 8x22B",
                                   "context_window_size": 64000
                                 },
                                 {
                                   "company": "Mistral AI",
                                   "model": "Mixtral 8x7B",
                                   "context_window_size": 32000
                                 },
                                 {
                                   "company": "Alibaba Cloud",
                                   "model": "Qwen2.5-Max",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Alibaba Cloud",
                                   "model": "Qwen2.5-7B-Instruct-1M",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Alibaba Cloud",
                                   "model": "Qwen2.5-14B-Instruct-1M",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Alibaba Cloud",
                                   "model": "Qwen2.5 72B",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "DeepSeek",
                                   "model": "DeepSeek V3.1",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "DeepSeek",
                                   "model": "DeepSeek R1",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "DeepSeek",
                                   "model": "DeepSeek V3",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Cohere",
                                   "model": "Command A",
                                   "context_window_size": 256000
                                 },
                                 {
                                   "company": "Cohere",
                                   "model": "Command R+",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Cohere",
                                   "model": "Command R",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Databricks",
                                   "model": "DBRX Instruct",
                                   "context_window_size": 32000
                                 },
                                 {
                                   "company": "AI21 Labs",
                                   "model": "Jamba 1.6",
                                   "context_window_size": 256000
                                 },
                                 {
                                   "company": "AI21 Labs",
                                   "model": "Jamba 1.5 Large",
                                   "context_window_size": 256000
                                 },
                                 {
                                   "company": "IBM",
                                   "model": "Granite 3B/8B Long",
                                   "context_window_size": 128000
                                 },
                                 {
                                   "company": "Amazon AWS",
                                   "model": "Amazon Nova Premier",
                                   "context_window_size": 1000000
                                 },
                                 {
                                   "company": "Amazon AWS",
                                   "model": "Amazon Nova Pro",
                                   "context_window_size": 300000
                                 },
                                 {
                                   "company": "Amazon AWS",
                                   "model": "Amazon Nova Lite",
                                   "context_window_size": 300000
                                 },
                                 {
                                   "company": "Amazon AWS",
                                   "model": "Amazon Nova Micro",
                                   "context_window_size": 128000
                                 }
                               ]
                """;


        return chatClient.prompt()
                .user("""
                        Bana popüler büyük dil modellerinin güncel bir listesini
                        ve mevcut context window boyutlarını verir misin?
                        """)
                .system(system)
                .call()
                .content();
    }

}
