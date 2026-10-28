package com.javi.curso.springcloud.kafka.api.controllers;

import com.javi.curso.springcloud.kafka.api.models.dto.ProductDto;
import com.javi.curso.springcloud.kafka.api.services.ProductCommandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductCommandService commandService;

    public ProductController(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductDto dto) {
        commandService.sendCreate(dto);
        return ResponseEntity.ok().body(Map.of("message", "Success Sent"));
    }
}
