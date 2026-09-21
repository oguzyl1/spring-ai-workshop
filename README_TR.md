# Spring AI Workshop

[English README](README.md)

Bu repo, **Spring AI 2.x** ve **Google Gemini** kullanarak GenAI entegrasyonlarını uygulamalı şekilde öğrenmek amacıyla hazırlanmış bir workshop projesidir.

Amaç, küçük ama odaklı örnekler üzerinden Spring AI kavramlarını adım adım anlamak ve daha sonra bunları daha ciddi, production odaklı projelerde kullanabilecek bir temel oluşturmaktır.

## Kullanılan Teknolojiler

- Java 21
- Spring Boot 4.1.1
- Spring AI 2.0.1
- Google GenAI / Gemini
- Maven
- Spring Web MVC
- Project Reactor

## Bu Repo Şu Ana Kadar Neleri Kapsıyor?

Şu an repository içinde aşağıdaki konulara ait örnekler bulunuyor:

- Temel chat istekleri
- `Flux<String>` ile streaming response
- Tam `ChatResponse` nesnesine erişim
- System prompt kullanımı
- Dinamik parametreli prompt template
- Java record’ları ile structured output
- Provider-native structured output
- Schema validation
- Multimodal input (image-to-text)
- Gemini image modelleri ile image generation
- `ChatMemory` ile konuşma hafızası
- `conversationId` ile konuşmaların birbirinden ayrılması
- `MessageChatMemoryAdvisor` kullanımı
- Modelin kendi bilgisiyle cevap üretmesi
- System prompt üzerinden harici context sağlanması
- "Stuff the prompt" / Bring Your Own Data yaklaşımı
- Google GenAI ile text embedding
- `TokenTextSplitter` ile document chunking
- `SimpleVectorStore` ile yerel vector storage
- `QuestionAnswerAdvisor` ile semantic retrieval
- Temel Retrieval-Augmented Generation (RAG)
- RAG ile structured output'un birlikte kullanımı
- `@Tool` ile tool calling
- `@ToolParam` ile tool parametrelerinin açıklanması
- Gerçek zamanlı bilgi sağlayan utility tool'lar
- Uygulama state'ini değiştiren action tool'lar
- Tool üzerinden harici API entegrasyonu

Workshop ilerledikçe repo genişletilmeye devam edecektir.

## Proje Yapısı

    src/main/java/dev/oguzhan/workshop
    ├── chat
    │   └── ChatController.java
    ├── prompt
    │   ├── AcmeBankController.java
    │   └── ArticleController.java
    ├── output
    │   ├── Activity.java
    │   ├── Itinerary.java
    │   └── VacationPlanController.java
    ├── multimodal
    │   ├── ImageDetectionController.java
    │   └── ImageGenerationController.java
    ├── memory
    │   └── MemoryController.java
    ├── byod
    │   └── ModelComparison.java
    ├── rag
    │    ├── Model.java
    │    ├── Models.java
    │    ├── ModelsController.java
    │    └── RagConfiguration.java
    ├── tools
    │   ├── datetime
    │   │   ├── DateTimeController.java
    │   │   └── DateTimeTools.java
    │   ├── action
    │   │   ├── TaskManagementController.java
    │   │   └── TaskManagementTools.java
    │   └── weather
            ├── WeatherController.java
            └── WeatherTools.java

## Konfigürasyon

Proje, Spring AI üzerinden Google Gemini kullanır.

API key environment variable üzerinden verilir:

`GOOGLE_GENAI_API_KEY`

Örnek `application.yaml`:

    spring:
      application:
        name: spring-ai-workshop

      ai:
        google:
          genai:
            api-key: ${GOOGLE_GENAI_API_KEY}

            chat:
              model: gemini-3.6-flash

            image:
              model: gemini-3.1-flash-image
              aspect-ratio: 1:1
              image-size: 1K
            
            embedding:
              api-key: ${GOOGLE_GENAI_API_KEY}
              text:
                model: gemini-embedding-2

## Önemli Notlar

- API key bilgileri kaynak koda yazılmamalıdır.
- Chat ve image generation farklı model türleri kullanır; ancak aynı Google GenAI bağlantı ayarlarını paylaşabilirler.
- Image generation tarafı provider tarafında billing/kredi gerektirebilir.
- Bu repo bir **öğrenme/workshop reposudur**; production-ready bir sistem olarak tasarlanmamıştır.

## Ana Öğrenme Alanları

### 1. Chat Temelleri

Repo, `ChatClient` kullanımını anlamak için temel chat örnekleriyle başlar:

- düz metin cevap
- streaming cevap
- tam response metadata erişimi

### 2. Prompting Teknikleri

Örnekler şunları kapsar:

- system prompt ile rol ve sınır tanımlama
- template ve parametrelerle dinamik prompt üretme

Bu örnekler, promptların daha kontrollü ve tekrar kullanılabilir hale getirilmesini göstermektedir.

### 3. Structured Output

Repo içinde, model çıktısını doğrudan Java record yapılarına dönüştüren örnekler yer alır.

Böylece serbest metin yanıtlarından, uygulama içinde daha rahat kullanılabilecek tipli veri yapılarına geçiş gösterilir.

### 4. Multimodal Input

Image-to-text örneği sayesinde bir Gemini chat modeline hem metin hem de görsel gönderilebilir.

Bu da multimodal promptların Spring AI chat akışı içinde nasıl ele alınabildiğini gösterir.

> **Not:** Image-to-text örneği, `src/main/resources/images/image1.jpg` konumunda yerel bir JPEG görsel bekler.  
> Bu dosya repository'e dahil edilmemektedir. Örneği çalıştırmadan önce kendi görselinizi `image1.jpg` adıyla bu klasöre ekleyin.

### 5. Image Generation

Ayrı bir örnekte Spring AI’nin `ImageModel` abstraction’ı kullanılarak Google GenAI image modelleri ile text-to-image üretimi gösterilir.

Bu bölüm, görsel üretimin de Spring Boot uygulamasına benzer bir Spring AI yaklaşımıyla entegre edilebildiğini göstermektedir.

### 6. Chat Memory

Projede Spring AI'nin `ChatMemory` abstraction'ı ve `MessageChatMemoryAdvisor` kullanılarak konuşma hafızası örneği bulunmaktadır.

Her konuşma bir `conversationId` ile ayrılır. Böylece farklı konuşmalar kendi geçmişlerini ve bağlamlarını birbirinden bağımsız şekilde koruyabilir.

Spring AI 2.x ile birlikte memory advisor kullanılan her istekte conversation ID açıkça verilmek zorundadır.

Workshop'taki mevcut örnek Spring AI'nin varsayılan in-memory yapısını kullanmaktadır. Bu nedenle uygulama yeniden başlatıldığında konuşma hafızası kaybolur.

İlerleyen aşamalarda kalıcı memory çözümleri ve gerçek uygulamalardaki conversation yönetimi incelenebilir.

### 7. Harici Veriyi Prompt'a Dahil Etme

Projede modelin yalnızca kendi eğitim bilgisini kullanması ile çalışma anında harici veri sağlanması arasındaki fark gösterilmektedir.

İlk örnekte modele yalnızca kullanıcı sorusu gönderilir ve model mevcut bilgisine dayanarak cevap üretir.

İkinci örnekte ise "stuff the prompt" yaklaşımı kullanılarak uygulamanın sahip olduğu veri doğrudan system prompt içerisine eklenir. Böylece modeli yeniden eğitmeden, çalışma anında ek bilgi sağlanabilir.

Bu yöntem küçük veri kümelerinde kullanışlıdır ancak veri miktarı büyüdükçe tüm içeriği her istekte prompt'a eklemek verimsiz hale gelir. Bu örnek, daha sonra kullanılan Retrieval-Augmented Generation (RAG) yaklaşımına geçişi göstermektedir.

### 8. Retrieval-Augmented Generation (RAG)

Projede Spring AI kullanılarak temel bir Retrieval-Augmented Generation örneği bulunmaktadır.

Dil modellerine ait bilgileri içeren yerel bir JSON dosyası okunur, daha küçük parçalara ayrılır, embedding'lere dönüştürülür ve `SimpleVectorStore` içerisinde saklanır.

Kullanıcı bir soru gönderdiğinde Spring AI:

1. Kullanıcı sorusunu embedding'e dönüştürür.
2. Vector store içerisinde anlamsal olarak benzer document chunk'larını arar.
3. Bulunan içerikleri model context'ine ekler.
4. Zenginleştirilmiş prompt'u Gemini modeline gönderir.
5. Model cevabını yapılandırılmış bir Java tipine dönüştürür.

Örnekte retrieval ve prompt augmentation işlemlerini otomatik olarak gerçekleştirmek için `QuestionAnswerAdvisor` kullanılmaktadır.

`SimpleVectorStore` yalnızca öğrenme ve yerel denemeler için kullanılmaktadır. Gerçek uygulamalarda genellikle PGVector, Qdrant, Elasticsearch veya başka bir kalıcı vector database tercih edilir.

### 9. Tool Calling

Projede Spring AI'nin `@Tool` ve `@ToolParam` yapıları kullanılarak tool calling örnekleri bulunmaktadır.

Tool calling sayesinde model yalnızca metin üretmekle kalmaz; kullanıcının doğal dilde verdiği isteğe göre uygulama içerisindeki bir fonksiyonun çağrılması gerektiğine ve bu fonksiyona hangi parametrelerin gönderileceğine karar verebilir.

Mevcut örneklerde üç farklı tool kullanım türü gösterilmektedir:

- **Utility tool:** Modelin gerçek zamanlı bilgiye ihtiyaç duyduğunda güncel tarih ve saat bilgisini alması.
- **Action tool:** Görev oluşturma, durum güncelleme ve görevi başka bir kişiye atama gibi uygulama durumunu değiştiren işlemler.
- **Information retrieval tool:** Harici bir hava durumu API'sinden güncel tahmin ve aktif hava uyarılarının alınması.

Spring AI, Java metotlarını `ChatClient` üzerinden modele kullanılabilir tool'lar olarak sunar. Model uygun tool'u seçer, Spring AI ilgili Java metodunu çalıştırır ve elde edilen sonuç tekrar modele verilerek kullanıcıya doğal dilde son cevap oluşturulur.

Workshop örneklerinde state basit şekilde memory içerisinde tutulmakta ve public API'ler kullanılmaktadır. Gerçek uygulamalarda tool'lar service katmanını, veritabanlarını, diğer mikroservisleri veya harici API'leri çağırabilir.

## Örnek Endpointler

Repository içinde şu an yer alan bazı örnek endpointler:

- `/chat`
- `/stream`
- `/joke`
- `/acme/chat`
- `/post/new`
- `/vacation/unstructured`
- `/vacation/structured`
- `/image/detection/image-to-text`
- `/image/generator/generate-image`
- `/rag/models`
- `/models`
- `/models/stuff-the-prompt`
- `/tools`
- `/tasks`
- `/weather`

## Planlanan Sonraki Konular

Bir sonraki aşamalarda repoya eklenmesi planlanan başlıklar:

- MCP
- Observability
- Evaluation ve testing
- Daha production odaklı entegrasyon desenleri

## Bu Repo Neden Var?

Bu repo, ilk günden kusursuz kurumsal mimari sunan bir uygulama olmaktan çok, Spring AI öğrenme sürecini belgeleyen pratik bir çalışma alanıdır.

Bu nedenle bazı örneklerde konuyu daha net gösterebilmek için logic doğrudan controller içinde tutulmuştur. İleride bu kavramlar daha temiz katmanlı mimarilerde kullanılacaktır.

## Lisans

Bu proje eğitim ve portfolyo amacıyla geliştirilmektedir.