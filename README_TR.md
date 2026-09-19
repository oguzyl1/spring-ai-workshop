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
    └── multimodal
        ├── ImageDetectionController.java
        └── ImageGenerationController.java

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

## Planlanan Sonraki Konular

Bir sonraki aşamalarda repoya eklenmesi planlanan başlıklar:

- Chat memory
- Embeddings
- Vector store
- Retrieval-Augmented Generation (RAG)
- Tool calling
- MCP
- Observability
- Evaluation ve testing
- Daha production odaklı entegrasyon desenleri

## Bu Repo Neden Var?

Bu repo, ilk günden kusursuz kurumsal mimari sunan bir uygulama olmaktan çok, Spring AI öğrenme sürecini belgeleyen pratik bir çalışma alanıdır.

Bu nedenle bazı örneklerde konuyu daha net gösterebilmek için logic doğrudan controller içinde tutulmuştur. İleride bu kavramlar daha temiz katmanlı mimarilerde kullanılacaktır.

## Lisans

Bu proje eğitim ve portfolyo amacıyla geliştirilmektedir.