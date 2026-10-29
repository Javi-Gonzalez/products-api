package com.javi.curso.springcloud.kafka.api.services;

import com.javi.curso.springcloud.kafka.api.messaging.ReplyInbox;
import com.javi.curso.springcloud.kafka.api.models.Command;
import com.javi.curso.springcloud.kafka.api.models.Reply;
import com.javi.curso.springcloud.kafka.api.models.dto.ProductDto;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge bridge;
    private final ReplyInbox replyInbox;
    private static final Logger logeer = (Logger) LoggerFactory.getLogger(ProductCommandService.class);

    public ProductCommandServiceImpl(StreamBridge bridge,  ReplyInbox replyInbox) {
        this.bridge = bridge;
        this.replyInbox = replyInbox;
    }

    @Override
    public Reply<?> sendCreateAndAwait(ProductDto dto, Duration timeout) {
        Command<ProductDto> cmd = new Command<>("CREATE", null, dto);
        String correlationId = UUID.randomUUID().toString();
        logeer.info("Api Product Cient Creating product whith correlationId={" + correlationId + "}");
        CompletableFuture<Reply<?>> future = replyInbox.register(correlationId);
        Message<Command<ProductDto>> msg = MessageBuilder.withPayload(cmd)
                .setHeader("correlationId", correlationId).build();
        boolean sent = this.bridge.send("commands-out-0", msg);
        if (!sent) {
            throw new IllegalStateException("No se pudo mandar el comando a kafka");
        }
        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Timeout esperando respuesta de products-commands desde Kafka",e);
        }
    }
}
