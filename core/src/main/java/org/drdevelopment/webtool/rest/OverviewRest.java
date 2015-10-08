package org.drdevelopment.webtool.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.drdevelopment.webtool.model.RestResourceDiscription;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
@Path("/overview")
public class OverviewRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(OverviewRest.class);
	private static final String BASE_PATH = "/services";
	
	private static final class MethodDescription {
        private String method;
        private String fullPath;
        private String produces;
        private String consumes;
 
        public MethodDescription(String method, String fullPath, String produces, String consumes) {
            super();
            this.method = method;
            this.fullPath = fullPath;
            this.produces = produces;
            this.consumes = consumes;
        }
    }
 
    private static final class ResourceDescription {
        private String basePath;
        private List<MethodDescription> calls;
 
        public ResourceDescription(String basePath) {
            this.basePath = basePath;
            this.calls = new ArrayList<>();
        }
 
        public void addMethod(String path, ResourceMethodInvoker method) {
            String produces = mostPreferredOrNull(method.getProduces());
            String consumes = mostPreferredOrNull(method.getConsumes());

            String postfixMethod = "";
            for (Annotation annotation : method.getMethodAnnotations()) {
            	if (annotation.annotationType().isAssignableFrom(javax.ws.rs.Path.class)) {
            		javax.ws.rs.Path pathAnnotation = (javax.ws.rs.Path) annotation;
            		postfixMethod = pathAnnotation.value();
            	}
            }
            for (String verb : method.getHttpMethods()) {
                calls.add(new MethodDescription(verb, path + (postfixMethod.isEmpty() ? "" : "/" + postfixMethod), produces, consumes));
            }
        }
 
        private static String mostPreferredOrNull(MediaType[] preferred) {
            if (preferred.length == 0) {
                return null;
            }
            else {
                return preferred[0].toString();
            }
        }
 
        public static List<ResourceDescription> fromBoundResourceInvokers(Set<Map.Entry<String, List<ResourceInvoker>>> bound) {
            Map<String, ResourceDescription> descriptions = new HashMap<>();

            for (Map.Entry<String, List<ResourceInvoker>> entry : bound) {
            	ResourceInvoker aMethod = (ResourceInvoker) entry.getValue().get(0);
                String basePath = BASE_PATH + aMethod.getMethod().getDeclaringClass().getAnnotation(Path.class).value();
 
                if (!descriptions.containsKey(basePath)) {
                    descriptions.put(basePath, new ResourceDescription(basePath));
                }
 
                for (ResourceInvoker invoker : entry.getValue()) {
                	ResourceMethodInvoker method = (ResourceMethodInvoker) invoker;
                    descriptions.get(basePath).addMethod(basePath, method);
                }
            }
 
            return new ArrayList<>(descriptions.values());
        }
    }
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableEndpoints(@Context Dispatcher dispatcher) {
        ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
        List<ResourceDescription> descriptions = ResourceDescription.fromBoundResourceInvokers(registry.getBounded().entrySet());
        List<RestResourceDiscription> restDescriptions = new ArrayList<>();
        
        for (ResourceDescription resource : descriptions) {
            for (MethodDescription method : resource.calls) {
            	RestResourceDiscription restDescription = new RestResourceDiscription();
            	restDescriptions.add(restDescription);
            	
            	restDescription.setBasePath(resource.basePath);
            	restDescription.setMethod(method.method);
            	restDescription.setFullPath(method.fullPath);
            	
                if (method.consumes != null) {
                	restDescription.setConsumes(method.consumes);
                }
                if (method.produces != null) {
                	restDescription.setProduces(method.produces);
                }
            }
        }
        
		return Response.status(Response.Status.OK).entity(restDescriptions).build();
    }
 
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getAvailableEndpointsHtml(@Context Dispatcher dispatcher) {
        StringBuilder sb = new StringBuilder();
        ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
        List<ResourceDescription> descriptions = ResourceDescription.fromBoundResourceInvokers(registry.getBounded().entrySet());
 
        sb.append("<h1>").append("REST interface overview").append("</h1>");
        sb.append("Only secured rest interfaces are displayed.<br/>");
 
        for (ResourceDescription resource : descriptions) {
            sb.append("<h2>").append(resource.basePath).append("</h2>");
            sb.append("<ul>");
 
            for (MethodDescription method : resource.calls) {
                sb.append("<li> ").append(method.method).append(" ");
                sb.append("<strong>").append(method.fullPath).append("</strong>");
 
                sb.append("<ul>");
 
                if (method.consumes != null) {
                    sb.append("<li>").append("Consumes: ").append(method.consumes).append("</li>");
                }
 
                if (method.produces != null) {
                    sb.append("<li>").append("Produces: ").append(method.produces).append("</li>");
                }
 
                sb.append("</ul>");
            }
 
            sb.append("</ul>");
        }
 
        return Response.ok(sb.toString()).build();
    }
}