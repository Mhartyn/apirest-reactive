package com.reactor.springbootreactor.apirest.models.services;

import com.reactor.springbootreactor.apirest.models.dao.ICateogriaDao;
import com.reactor.springbootreactor.apirest.models.documents.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoriaService implements ICategoriaServices {

    @Autowired
    private ICateogriaDao dao;

    @Override
    public Flux<Categoria> findAll() {        
        return dao.findAll();
    }

    @Override
    public Mono<Categoria> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Categoria> save(Categoria categoria) {
        return dao.save(categoria);
    }

    @Override
    public Mono<Categoria> findByNombre(String nombre) {
        return dao.findByNombre(nombre);
    }
    
}