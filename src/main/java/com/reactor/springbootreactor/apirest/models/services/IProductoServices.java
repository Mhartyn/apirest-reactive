package com.reactor.springbootreactor.apirest.models.services;

import com.reactor.springbootreactor.apirest.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductoServices {
    
    public Flux<Producto> findAll();
    
    public Flux<Producto> findAllConNombreUpperCase();

    public Flux<Producto> findAllConNombreUpperCaseRepeat();
    
    public Mono<Producto> findById(String id);

    public Mono<Producto> save(Producto producto);

    public Mono<Void> delete(Producto producto);

    public Mono<Producto> findByNombre(String nombre);
    
    public Mono<Producto> obtnerPorNombre(String nombre);
}