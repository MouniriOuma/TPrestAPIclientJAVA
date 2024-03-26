package com.ws.tprestapijava;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

public class App {
    private static final String BASE_URI = "http://localhost:8080/TPrestAPI-1.0-SNAPSHOT/rest/products";
    private final Client client;
    public App() {
        ClientConfig config = new ClientConfig();
        this.client = ClientBuilder.newClient(config);
    }
    public void close() {
        client.close();
    }
    public String getList() {
        WebTarget target = client.target(BASE_URI);
        return target.request().accept(MediaType.APPLICATION_JSON).get(String.class);
    }
    public Product getProduct(String productId) {
        WebTarget target = client.target(BASE_URI + "/" + productId);
        return target.request().accept(MediaType.APPLICATION_JSON).get(Product.class);
    }
    public String addProduct(Product product) {
        WebTarget target = client.target(BASE_URI);
        Response response = target.request()
                .post(Entity.entity(product, MediaType.APPLICATION_JSON), Response.class);
        String location = response.getLocation().toString();
        String id = String.valueOf(product.getId());
        return BASE_URI + "/" + id;
    }
    public Response updateProduct(String productId, Product product) {
        WebTarget target = client.target(BASE_URI).path(productId);
        return target.request()
                .put(Entity.entity(product, MediaType.APPLICATION_JSON), Response.class);
    }
    public Response deleteProduct(String productId) {
        WebTarget target = client.target(BASE_URI).path(productId);
        return target.request().delete(Response.class);
    }

    public static void main(String[] args) {
        App restClient = new App();
        try {
            restClient.testMethods();
        } finally {
            restClient.close();
        }
    }

    private void testMethods() {
        // Test list method
        System.out.println("List of products:");
        System.out.println(getList());

        System.out.println("-----------------------------");

        // Test get method
        System.out.println("Product with ID 3:");
        System.out.println(getProduct("3"));

        System.out.println("-----------------------------");

        // Test add method
        Product newProduct = new Product(4, "phone ouma", 6000, "samsung A52s");
        System.out.println("Added product location:");
        System.out.println(addProduct(newProduct));
        System.out.println("List of products:");
        System.out.println(getList());


        System.out.println("-----------------------------");

        // Test update method
        System.out.println("Product with ID 4:");
        System.out.println(getProduct("4"));
        Product updatedProduct = new Product(4, "Chareur", 300, "Chargeur type C 5m");
        System.out.println("Update product response:");
        System.out.println(updateProduct("4", updatedProduct));
        System.out.println("Product with ID 4:");
        System.out.println(getProduct("4"));

        System.out.println("-----------------------------");

        // Test delete method
        System.out.println("Delete product response:");
        System.out.println(deleteProduct("4"));
        System.out.println("List of products:");
        System.out.println(getList());
    }
}
