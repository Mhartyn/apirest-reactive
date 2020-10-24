package com.reactor.springbootreactor.apirest;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactor.springbootreactor.apirest.models.documents.Categoria;
import com.reactor.springbootreactor.apirest.models.documents.Producto;
import com.reactor.springbootreactor.apirest.models.services.CategoriaService;
import com.reactor.springbootreactor.apirest.models.services.ProductoService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApirestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductoService services;

	@Autowired
	private CategoriaService categoriaServices;

	@Value("${config.base.endpoint}")
	private String url;

	@Test
	public void listarTest() {

		client.get().uri(url).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBodyList(Producto.class).consumeWith(response -> {
					List<Producto> productos = response.getResponseBody();
					productos.forEach(p -> {
						System.out.println(p.getNombre());
					});

					Assertions.assertTrue(productos.size() >= 9);
				});
		// .hasSize(9);
	}

	@Test
	public void verTest() {

		String nombre = "cel";
		Producto producto = services.findByNombre(nombre).block();

		client.get().uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
				.accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.id").isNotEmpty().jsonPath("$.nombre")
				.isEqualTo(nombre);
	}

	/* @Test
	public void crearTest() {

		Categoria categoria = categoriaServices.findByNombre("electronica").block();
		Producto producto = new Producto("mesa", 140.5, categoria);

		client.post().uri(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(producto), Producto.class).exchange().expectStatus().isCreated().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.id").isNotEmpty().jsonPath("$.nombre")
				.isEqualTo("mesa").jsonPath("$.categoria.nombre").isEqualTo("electronica");
	} */

	@Test
	public void crearV1Test() {
		Categoria categoria = categoriaServices.findByNombre("electronica").block();
		Producto producto = new Producto("mesa", 140.5, categoria);

		client.post().uri(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(producto), Producto.class).exchange().expectStatus().isCreated().expectHeader()
				.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.producto.id").isNotEmpty()
				.jsonPath("$.producto.nombre").isEqualTo("mesa")
				.jsonPath("$.producto.categoria.nombre").isEqualTo("electronica");
	}

	/*
	 * @Test public void crear2Test() {
	 * 
	 * Categoria categoria = categoriaServices.findByNombre("electronica").block();
	 * Producto producto = new Producto("mesa 2", 140.5, categoria);
	 * 
	 * client.post() .uri(url) .contentType(MediaType.APPLICATION_JSON)
	 * .accept(MediaType.APPLICATION_JSON) .body(Mono.just(producto),
	 * Producto.class) .exchange() .expectStatus().isCreated()
	 * .expectHeader().contentType(MediaType.APPLICATION_JSON)
	 * .expectBody(Producto.class) .consumeWith(response -> { Producto p =
	 * response.getResponseBody(); Assertions.assertTrue(p.getId() != null);
	 * Assertions.assertEquals(p.getNombre(), "mesa 2");
	 * Assertions.assertEquals(p.getCategoria().getNombre(), "electronica"); }); }
	 */

	@Test
	public void crear2_v1_Test() {
		Categoria categoria = categoriaServices.findByNombre("electronica").block();
		Producto producto = new Producto("mesa 2", 140.5, categoria);

		client.post()
		.uri(url)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>(){})
		.consumeWith(response -> {
			Object o = response.getResponseBody().get("producto");
			Producto p = new ObjectMapper().convertValue(o, Producto.class);
			Assertions.assertTrue(p.getId() != null);
			Assertions.assertEquals(p.getNombre(), "mesa 2");
			Assertions.assertEquals(p.getCategoria().getNombre(), "electronica");
		});
	}
 
	@Test
	public void editarTest() {
		Categoria categoria = categoriaServices.findByNombre("electronica").block();
		Producto producto= services.findByNombre("tv").block();

		Producto productoEditado = new Producto("mesa cel", 140.5, categoria);

		client.put()
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.id").isEqualTo(producto.getId())
		.jsonPath("$.nombre").isEqualTo("mesa cel")
		.jsonPath("$.precio").isEqualTo("140.5");
	}

	@Test
	public void eliminarTest() {
		Producto producto= services.findByNombre("cel 2").block();

		client.delete()
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody().isEmpty();
		
		client.get()
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNotFound()
		.expectBody().isEmpty();
	}
}
