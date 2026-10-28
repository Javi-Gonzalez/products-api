package com.javi.curso.springcloud.kafka.api.services;

import com.javi.curso.springcloud.kafka.api.models.Command;
import com.javi.curso.springcloud.kafka.api.models.dto.ProductDto;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final StreamBridge bridge;

    public ProductCommandServiceImpl(StreamBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void sendCreate(ProductDto dto) {
        Command<ProductDto> cmd = new Command<>("CREATE", null, dto);
        boolean sent = this.bridge.send("commands-out-0", cmd);
        if (!sent) {
            throw new IllegalStateException("No se pudo mandar el comando a kafka");
        }
    }
}
