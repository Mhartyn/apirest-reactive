package com.reactor.springbootreactor.apirest.models.services;

import com.reactor.springbootreactor.apirest.models.documents.Categoria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICategoriaServices {
    
    public Flux<Categoria> findAll();
        
    public Mono<Categoria> findById(String id);

    public Mono<Categoria> save(Categoria categoria);    
}