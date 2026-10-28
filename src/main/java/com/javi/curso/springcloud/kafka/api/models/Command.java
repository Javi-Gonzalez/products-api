package com.javi.curso.springcloud.kafka.api.models;

public record Command<T>(String type, Long id, T body) {
}
