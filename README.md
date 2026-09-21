# Spring AI Workshop

[Türkçe README](README_TR.md)

A hands-on learning repository for exploring **Spring AI 2.x** with **Google Gemini** in a Spring Boot application.

This project is being built incrementally while following a practical learning path around modern GenAI application development in Java. The main goal is to understand Spring AI concepts through small focused examples and later use them as a foundation for more production-oriented projects.

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring AI 2.0.1
- Google GenAI / Gemini
- Maven
- Spring Web MVC
- Project Reactor

## What This Repository Covers

So far, this repository includes examples for:

- Basic chat completions
- Streaming responses with `Flux<String>`
- Accessing the full `ChatResponse`
- System prompts
- Prompt templates with dynamic parameters
- Structured output with Java records
- Provider-native structured output
- Schema validation
- Multimodal input (image-to-text)
- Image generation with Gemini image models
- Conversation memory with `ChatMemory`
- Conversation isolation using `conversationId`
- `MessageChatMemoryAdvisor`
- Using model knowledge without external context
- Supplying external data through system prompts
- "Stuff the prompt" / Bring Your Own Data pattern
- Text embeddings with Google GenAI
- Document chunking with `TokenTextSplitter`
- Local vector storage with `SimpleVectorStore`
- Semantic retrieval with `QuestionAnswerAdvisor`
- Basic Retrieval-Augmented Generation (RAG)
- RAG combined with structured output

The repository will continue to grow as more topics are studied.

## Project Structure

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
    └── rag
        ├── Model.java
        ├── Models.java
        ├── ModelsController.java
        └── RagConfiguration.java

## Configuration

The project uses Google Gemini through Spring AI.

The API key is provided through an environment variable:

`GOOGLE_GENAI_API_KEY`

Example `application.yaml`:

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

## Important Notes

- Keep API keys out of source control.
- Chat and image generation use different model types, even if they share the same Google GenAI connection settings.
- Image generation may require billing/credits on the provider side.
- This repository is a **learning workshop**, not a production-ready system.

## Main Learning Areas

### 1. Chat Basics

The project starts with simple chat examples to understand how `ChatClient` works, including:

- plain text output
- streaming output
- full response metadata

### 2. Prompting Techniques

Examples include:

- role restriction with system prompts
- dynamic prompt construction with templates and parameters

These examples help illustrate how prompts can be made more controlled and reusable.

### 3. Structured Output

The project includes examples that map model output directly into Java records.

This helps move from free-form text responses to typed application-friendly data structures.

### 4. Multimodal Input

The image-to-text example sends both text and an image to a Gemini chat model.

This demonstrates how multimodal prompts can be handled through the Spring AI chat workflow.

> **Note:** The image-to-text example expects a local JPEG file at `src/main/resources/images/image1.jpg`.  
> This file is intentionally excluded from the repository. Add your own image named `image1.jpg` before running the example.

### 5. Image Generation

A separate image generation example uses Spring AI's `ImageModel` abstraction with Google GenAI image models.

This shows how text-to-image generation can be integrated into a Spring Boot application using the same overall Spring AI style.

### 6. Chat Memory

The project includes a conversational memory example using Spring AI's `ChatMemory` abstraction together with `MessageChatMemoryAdvisor`.

Each conversation is scoped by a `conversationId`, allowing multiple independent conversations to maintain their own context.

Spring AI 2.x requires the conversation ID to be explicitly provided for every request that uses a memory advisor.

The current workshop example uses Spring AI's default in-memory implementation, so conversation context is lost when the application restarts.

Future examples may explore persistent memory implementations and more production-oriented conversation management.

### 7. Bringing External Data into the Prompt

The project demonstrates the difference between relying only on the model's built-in knowledge and providing external data at request time.

The first example sends only a user prompt, so the model answers using its existing knowledge.

The second example uses a "stuff the prompt" approach by placing a dataset directly inside the system prompt. This allows the model to use application-provided information without retraining.

This approach works well for small datasets, but it does not scale efficiently when the amount of data grows. It serves as a simple introduction to the idea behind Retrieval-Augmented Generation (RAG), where only the most relevant pieces of external data are retrieved dynamically.

### 8. Retrieval-Augmented Generation (RAG)

The project includes a basic Retrieval-Augmented Generation example using Spring AI.

A local JSON dataset containing language model information is loaded, split into smaller chunks, converted into embeddings, and stored in a `SimpleVectorStore`.

When a user sends a question, Spring AI:

1. Converts the question into an embedding.
2. Searches the vector store for semantically similar document chunks.
3. Adds the retrieved content to the model context.
4. Sends the augmented prompt to Gemini.
5. Maps the response into a structured Java type.

The example uses `QuestionAnswerAdvisor` to handle the retrieval and prompt augmentation process automatically.

`SimpleVectorStore` is used only for learning and local experimentation. A production application would normally use a persistent vector database such as PGVector, Qdrant, Elasticsearch, or another supported vector store.


## Example Endpoints

Some example endpoints currently included in the repository:

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

## Planned Topics

The next areas planned for this repository include:

- Tool calling
- MCP
- Observability
- Evaluation and testing
- More production-oriented integration patterns

## Why This Repository Exists

This repository is meant to document a real learning journey with Spring AI rather than present a polished enterprise application from day one.

For that reason, some examples intentionally keep logic inside controllers to make the API usage easier to understand. In future projects, these concepts will be applied in cleaner layered architectures.

## License

This project is intended for learning and portfolio purposes.