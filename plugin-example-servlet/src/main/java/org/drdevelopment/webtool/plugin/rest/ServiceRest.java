package org.drdevelopment.webtool.plugin.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/example")
public class ServiceRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRest.class);

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getAllProducts() {
		LOGGER.debug("Rest service called.");

		List<Product> products = new ArrayList<>();
		products.add(new Product("Apple", 3.45));
		products.add(new Product("Peer", 2.125));
        return Response.status(Response.Status.OK).entity(products).build();
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

