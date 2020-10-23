package com.reactor.springbootreactor.apirest.models.dao;

import com.reactor.springbootreactor.apirest.models.documents.Categoria;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface ICateogriaDao extends ReactiveMongoRepository<Categoria,String> {
    
    public Mono<Categoria> findByNombre(String nombre);   
}
