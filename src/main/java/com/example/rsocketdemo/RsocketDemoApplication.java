package com.example.rsocketdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
public class RsocketDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(RsocketDemoApplication.class, args);
  }
}

@Controller
@Slf4j
class RSocketController {

  @MessageMapping("request-response")
  public Mono<Message> requestResponse(Message request) {
    log.info("Received request-response request: {}", request.getMessage());
    return Mono.just(new Message(String.format("Response: %s", request.getMessage())));
  }

  @MessageMapping("fire-and-forget")
  Mono<Void> fireAndForget(Message request) {
    log.info("Received fire-and-forget request: {}", request.getMessage());
    return Mono.empty();
  }

  @MessageMapping("request-stream")
  Flux<Message> requestStream(Message request) {
    log.info("Received request-stream request: {}", request.getMessage());
    return Flux
            .interval(Duration.ofSeconds(1))
        .map(index -> new Message(String.format("Response #%s", index)))
            .log();
  }

  @MessageMapping("channel")
  Flux<Message> channel(final Flux<Integer> settings) {
    log.info("Received channel request...");
    return settings
        .doOnNext(setting -> log.info("Received request: %s", setting))
        .doOnCancel(() -> log.info("Client closed the channel"))
        .switchMap(
            setting ->
                Flux.interval(Duration.ofSeconds(setting))
                    .map(index -> new Message(String.format("Response #%s", index))))
        .log();
  }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Message {
  private String message;
  private long created = Instant.now().getEpochSecond();

  public Message(String message) {
    this.message = message;
  }
}
