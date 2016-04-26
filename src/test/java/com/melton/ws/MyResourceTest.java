package com.melton.ws;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.*;

public class MyResourceTest {
 
    private HttpServer server;
    private WebTarget target;
 
    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
 
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }
 
    @After
    public void tearDown() throws Exception {
        server.stop();
    }
 
    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        String responseMsg = target.path("siteaccess").request().get(String.class);
        System.out.println(responseMsg);
        assertNotNull(responseMsg);
    }
}
