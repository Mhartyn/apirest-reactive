package com.reactor.springbootreactor.apirest.models.services;

import com.reactor.springbootreactor.apirest.models.dao.IProductoDao;
import com.reactor.springbootreactor.apirest.models.documents.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoService implements IProductoServices {

    @Autowired
    private IProductoDao dao;

    @Override
    public Flux<Producto> findAll() {
        return dao.findAll();
    }

    @Override
    public Mono<Producto> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return dao.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return dao.delete(producto);
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCase() {
        return dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCaseRepeat() {
        return findAllConNombreUpperCase().repeat(5000);
    }
    
}
