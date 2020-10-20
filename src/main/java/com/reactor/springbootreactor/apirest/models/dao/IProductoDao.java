package com.reactor.springbootreactor.apirest.models.dao;


import com.reactor.springbootreactor.apirest.models.documents.Producto;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IProductoDao extends ReactiveMongoRepository<Producto,String>{
    
}
