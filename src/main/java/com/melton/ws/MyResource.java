package com.melton.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
 
/**
 * Root resource (exposed at "test" path)
 */
@Path("test")
public class MyResource {
 
	public MyResource() {
		super();
    	// TODO -- REMOVE
    	System.out.println("inside MyResource()");
	}
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
}
