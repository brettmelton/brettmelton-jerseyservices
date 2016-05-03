package com.melton.ws;

import io.swagger.jaxrs.config.BeanConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class MyApplication extends Application {

    public static ResourceConfig createApp() {
        return new ResourceConfig()
                .packages("org.glassfish.jersey.examples.jsonmoxy")
                .register(createMoxyJsonResolver());
    }

    public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
        final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
	
    public MyApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("services.brettmelton.com:80");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("com.melton.ws");
        beanConfig.setScan(true);
    }
    
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(com.melton.ws.MyResource.class);

        //Manually adding MOXyJSONFeature
        resources.add(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);
 
        //Configure Moxy behavior
        resources.add(JsonMoxyConfigurationContextResolver.class);
        
        // Add in Swagger for Documentation
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        
        return resources;
    }
}
