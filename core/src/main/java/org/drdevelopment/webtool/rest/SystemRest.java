package org.drdevelopment.webtool.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.configuration.Config;
import org.drdevelopment.webtool.model.Page;
import org.drdevelopment.webtool.repository.PageRepository;
import org.drdevelopment.webtool.repository.PagesRepository;
import org.drdevelopment.webtool.repository.ParagraphRepository;
import org.drdevelopment.webtool.rest.data.WebsiteSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/system")
public class SystemRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemRest.class);

	@GET
    @Path("memory")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response getMemory() {
		LOGGER.trace("Retrieve memory usage.");
        
        return Response.status(Response.Status.OK).entity(new MemoryUsage()).build();
	}

	@GET
    @Path("load")
	@Produces({ MediaType.APPLICATION_JSON })
	@RolesAllowed("admin")
    public Response loadOnSystem() {
        return Response.status(Response.Status.OK).entity(new Load()).build();
	}

	@GET
    @Path("pageStats")
	@Produces({ MediaType.APPLICATION_JSON })
    public Response pageStats() {
        return Response.status(Response.Status.OK).entity(new PageStats()).build();
	}

	@SuppressWarnings("unused")
	private class PageStats {
        int nrOfPages;
        int nrOfParagraphs;

		public PageStats() {
			super();
			this.nrOfPages = PagesRepository.getPages().size();
			this.nrOfParagraphs = ParagraphRepository.getParagraphs().size();
		}

		public int getNrOfPages() {
			return nrOfPages;
		}

		public int getNrOfParagraphs() {
			return nrOfParagraphs;
		}
	}
	
	@SuppressWarnings("unused")
	private class Load {
        double systemLoad;

		public Load() {
			super();
			this.systemLoad = Config.getConfig().getSystemLoadAverage();
		}

		public double getSystemLoad() {
			return systemLoad;
		}
	}
	
	@SuppressWarnings("unused")
	private class MemoryUsage {
        long maxMemory;
        long allocatedMemory;
        long freeMemory;
        long totalMemory;
	
		public MemoryUsage() {
			super();

	        Runtime runtime = Runtime.getRuntime();
	        maxMemory = runtime.maxMemory() / (1024 * 1024);
	        allocatedMemory = runtime.totalMemory() / (1024 * 1024);
	        freeMemory = runtime.freeMemory() / (1024 * 1024);
	        totalMemory = freeMemory + (maxMemory - allocatedMemory); 
		}

		public long getMaxMemory() {
			return maxMemory;
		}

		public long getAllocatedMemory() {
			return allocatedMemory;
		}

		public long getFreeMemory() {
			return freeMemory;
		}

		public long getTotalMemory() {
			return totalMemory;
		}
		
	}
}

