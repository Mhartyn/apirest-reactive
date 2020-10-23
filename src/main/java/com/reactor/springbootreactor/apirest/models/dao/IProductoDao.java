package com.reactor.springbootreactor.apirest.models.dao;


import com.reactor.springbootreactor.apirest.models.documents.Producto;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface IProductoDao extends ReactiveMongoRepository<Producto,String>{
    
    public Mono<Producto> findByNombre(String nombre);
    
    @Query("{ 'nombre': ?0 }")
    public Mono<Producto> obtnerPorNombre(String nombre);
}
