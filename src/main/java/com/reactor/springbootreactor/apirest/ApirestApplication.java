package com.reactor.springbootreactor.apirest;

import java.util.Date;

import com.reactor.springbootreactor.apirest.models.documents.Categoria;
import com.reactor.springbootreactor.apirest.models.documents.Producto;
import com.reactor.springbootreactor.apirest.models.services.ICategoriaServices;
import com.reactor.springbootreactor.apirest.models.services.IProductoServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ApirestApplication  implements CommandLineRunner{

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	@Autowired
	private IProductoServices service;
	
	@Autowired
	private ICategoriaServices categoriaService;
	
	private static final Logger log = LoggerFactory.getLogger(ApirestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ApirestApplication.class, args);		
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();
		
		Categoria electronica = new Categoria("electronica");
		Categoria computo = new Categoria("computo");

		Flux.just(electronica, computo)
			.flatMap(categoriaService::save)
			.doOnNext(c -> log.info("Categoria Creada: " + c.getId() + " " + c.getNombre()))
			.thenMany(
				Flux.just(
			         new Producto("cel", 120.4, electronica),
			         new Producto("tv", 320.5, electronica),
			         new Producto("pc", 520.0, computo),
			         new Producto("cel 1", 120.4, electronica),
			         new Producto("tv 1", 320.5, electronica),
			         new Producto("pc 1", 520.0, computo),
			         new Producto("cel 2", 120.4, electronica),
			         new Producto("tv 2", 320.5, electronica),
			         new Producto("pc 2", 520.0, computo)
			         )
			         .flatMap(producto -> {
			         	producto.setCreateAt(new Date());
			         	return service.save(producto);
			         })
			)		
			.subscribe(producto -> log.info(producto.getId() + " " + producto.getNombre()));
	}
}
