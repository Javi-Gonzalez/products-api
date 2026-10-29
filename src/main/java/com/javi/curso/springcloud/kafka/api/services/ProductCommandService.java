package com.javi.curso.springcloud.kafka.api.services;

import com.javi.curso.springcloud.kafka.api.models.Reply;
import com.javi.curso.springcloud.kafka.api.models.dto.ProductDto;

import java.time.Duration;

public interface ProductCommandService {

    Reply<?> sendCreateAndAwait (ProductDto dto, Duration timeout);
}
