package com.reactor.springbootreactor.apirest.models.documents;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "productos")
public class Producto {
    @Id
    private String id;

    @NotEmpty
    private String nombre;
        
    private String foto;

    @NotNull
    private Double precio;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
    
    @Valid
    @NotNull
    private Categoria categoria;

    public Producto() {
    }

    public Producto(String nombre, Double precio, Categoria categoria) {        
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Producto id(String id) {
        this.id = id;
        return this;
    }

    public Producto nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Producto precio(Double precio) {
        this.precio = precio;
        return this;
    }

    public Producto createAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }
    

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", precio='" + getPrecio() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}