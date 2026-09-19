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
    └── multimodal
        ├── ImageDetectionController.java
        └── ImageGenerationController.java

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

## Planned Topics

The next areas planned for this repository include:

- Chat memory
- Embeddings
- Vector stores
- Retrieval-Augmented Generation (RAG)
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