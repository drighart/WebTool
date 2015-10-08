package org.drdevelopment.webtool.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hello")
public class TestServiceRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceRest.class);

	@GET
    @Path("hello2")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response helloWorld() {
		LOGGER.debug("Rest service called.");
		
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("hello", "world!");
        JsonObject json = Json.createObjectBuilder().add("data", builder).build();

        return Response.status(Response.Status.OK).entity(json).build();
	}

	@GET
    @Path("test")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response helloTest() {
		LOGGER.debug("Rest service called.");

		Product p = new Product("Apple", 3.45);
        return Response.status(Response.Status.OK).entity(p).build();
	}

	@GET
    @Produces(MediaType.TEXT_PLAIN)
	public String helloWorld2() {
		LOGGER.debug("Rest service called 2.");
        return "This is a test";
	}

	private class Product {
		private String name;
		private Double price;
		
		public Product(String name, Double price) {
			super();
			this.name = name;
			this.price = price;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}
		
	}
}

