package com.melton.ws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/services")
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
	
	
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(MyResource.class);

        //Manually adding MOXyJSONFeature
        resources.add(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);
 
        //Configure Moxy behavior
        resources.add(JsonMoxyConfigurationContextResolver.class);
        
        return resources;
    }
}
