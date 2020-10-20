package com.reactor.springbootreactor.apirest.models.dao;

import com.reactor.springbootreactor.apirest.models.documents.Categoria;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICateogriaDao extends ReactiveMongoRepository<Categoria,String> {
    
}
