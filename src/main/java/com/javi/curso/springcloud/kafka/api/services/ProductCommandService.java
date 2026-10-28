package com.javi.curso.springcloud.kafka.api.services;

import com.javi.curso.springcloud.kafka.api.models.dto.ProductDto;

public interface ProductCommandService {

    void sendCreate (ProductDto dto);
}
