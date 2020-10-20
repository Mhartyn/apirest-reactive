package com.reactor.springbootreactor.apirest.controllers;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;

import com.reactor.springbootreactor.apirest.models.documents.Producto;
import com.reactor.springbootreactor.apirest.models.services.IProductoServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoServices services;

    @Value("{config.uploads.path}")
    private String path;

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @PostMapping("/upload/{id}")
    public Mono<ResponseEntity<Producto>> upload(@PathVariable String id, @RequestPart FilePart file){        
        log.info(file.filename());
        return services.findById(id).flatMap(p -> {
            String nombre = UUID.randomUUID() + "-" + file.filename().replace(" ", "").replace("//", "").replace(":", "");
            p.setFoto(nombre);

            return file.transferTo(new File(path + p.getFoto())).then(services.save(p));
        })
        .map(p -> ResponseEntity.ok(p))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> listar(){
        //return Mono.just(ResponseEntity.ok(services.findAll()));
        return Mono.just(
            ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(services.findAll())
            );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
        //return services.findById(id).map(p -> ResponseEntity.ok(p));
        return services.findById(id).map( p ->
            ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(p))
            .defaultIfEmpty(ResponseEntity.notFound().build()
            );
    }
    
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Producto> monoProducto){
        
        Map<String, Object> respuesta = new HashMap<String, Object>();
        
        return monoProducto.flatMap(producto -> {            
            if (producto.getCreateAt() == null) {
                producto.setCreateAt(new Date());            
            }
            
            return services.save(producto)
            .map( p -> {
                respuesta.put("producto", p);
                respuesta.put("mensaje", "producto creado con exito");
                respuesta.put("timestamp", new Date());
                return ResponseEntity.created(URI.create("/api/productos/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(respuesta);
            });
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
            .flatMap(e -> Mono.just(e.getFieldErrors()))
            .flatMapMany(Flux::fromIterable)
            .map(fieldError -> "El campo: " + fieldError.getField() + " " + fieldError.getDefaultMessage())
            .collectList()
            .flatMap(list -> {
                respuesta.put("error", list);
                respuesta.put("timestamp", new Date());
                respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                            return Mono.just(ResponseEntity.badRequest().body(respuesta));
                        });
        });
    }

    @PostMapping("/v2")
    public Mono<ResponseEntity<Producto>> crearConFoto(Producto producto, @RequestPart FilePart file){
        if (producto.getCreateAt() == null) {
            producto.setCreateAt(new Date());            
        }

        String nombre = UUID.randomUUID() + "-" + file.filename().replace(" ", "").replace("//", "").replace(":", "");
        producto.setFoto(nombre);

        return file.transferTo(new File(path + producto.getFoto())).then(services.save(producto))        
        .map( p -> ResponseEntity.created(URI.create("/api/productos/".concat(p.getId())))
        .contentType(MediaType.APPLICATION_JSON)
        .body(p));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto, @PathVariable String id){
        //return services.findById(id).map(p -> ResponseEntity.ok(p));
        return services.findById(id).flatMap( p ->{
                p.setNombre(producto.getNombre());
                p.setPrecio(producto.getPrecio());
                p.setCategoria(producto.getCategoria());
                return services.save(p);
            })
            .map( p -> ResponseEntity.created(URI.create("/api/productos/".concat(p.getId())))
            .contentType(MediaType.APPLICATION_JSON)
            .body(p))
            .defaultIfEmpty(ResponseEntity.notFound().build()
            );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
        return services.findById(id).flatMap( p -> {
            return services.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
