package com.melton.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
    	// TODO -- REMOVE
    	System.out.println("inside getClasses");
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(com.melton.ws.MyResource.class);
    	// TODO -- REMOVE
    	System.out.println("size: " + s.size());
    	for( int inx=0; inx< s.size(); ++inx )
    	{
    		System.out.println( s.contains(com.melton.ws.MyResource.class) );
    	}
        return s;
    }
}
