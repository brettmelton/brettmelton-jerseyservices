package com.melton.ws;

import io.swagger.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.melton.ws.pojo.SiteAccessBean;
import com.melton.ws.pojo.SiteAccessEntries;

/**
 * Root resource (exposed at "access" path)
 */
@Path("siteaccess")
@Api(value = "/siteaccess", tags = "siteaccess")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MyResource {
 
    private static DataSource s_ds;

	public MyResource() {
		super();
	}
	
	@DELETE
	@Path("/{agent}")
	@ApiOperation(value = "Deletes UserAgents with regularexpression matching")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid UserAgent"),
	    @ApiResponse(code = 404, message = "UserAgent not found") })
	public Response deleteEntriesbyAgent(@PathParam("agent") String strAgent) {

        Connection conn = null;
        PreparedStatement pstmt = null;

        try
        {
            conn = getConnectionFromPool();
            StringBuilder sbSql = new StringBuilder();
            sbSql.append( "DELETE FROM access_log " );
            sbSql.append( "WHERE to_days(now()) - to_days(create_date) <= 30 " );
            sbSql.append( "AND useragent LIKE \"%" );
            sbSql.append( strAgent );
            sbSql.append( "%\"" );

            pstmt = conn.prepareStatement(sbSql.toString());
            pstmt.execute();
        }
        catch(SQLException sqlExc)
        {
            System.out.println("Exception getting connection from pool: " + sqlExc );
    		return Response.status(500).entity("SQL Exc: " + sqlExc.getMessage()).build();
        }
        finally
        {
            try {
                if( null != pstmt ) pstmt.close();
                if( null != conn ) conn.close();
                pstmt = null;
                conn = null;
            } catch (Exception ex) {}
        }
		return Response.status(202).entity("Agent Strings deleted successfully!").build();
	}
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as json media type.
     */
    @GET
    @ApiOperation(value = "Finds most recent site acccess", 
      notes = "This will only provide the most recent site access entries up to the number you provide or 2 by default",
      response = SiteAccessEntries.class, 
      responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid value") })
    @Produces(MediaType.APPLICATION_JSON)   // TEXT_PLAIN
    public SiteAccessEntries getLastAccess(
        @DefaultValue("2") @QueryParam("num") int iNumItems) {
    	    	
    	SiteAccessEntries listSiteAccess = new SiteAccessEntries();
    
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            conn = getConnectionFromPool();

            StringBuilder sbSql = new StringBuilder();
            sbSql.append( "SELECT * FROM access_log " );
            sbSql.append( "WHERE to_days(now()) - to_days(create_date) <= 30 " );
            sbSql.append( "ORDER BY create_date DESC " );
            sbSql.append( String.format("LIMIT %d", iNumItems) );
            String name, uri, request, session, remoteaddr, useragent, referrer, project;
            Timestamp ts;

            pstmt = conn.prepareStatement(sbSql.toString());
            rs = pstmt.executeQuery();

            while( rs.next() )
            {
	            name = rs.getString("name");
	            uri = rs.getString("uri");
	            request = rs.getString("request");
	            session = rs.getString("session");
	            remoteaddr = rs.getString("remoteaddr");
	            useragent = rs.getString("useragent");
	            referrer = rs.getString("referrer");
	            ts = rs.getTimestamp("create_date");
	            project = rs.getString("project");
	
	            listSiteAccess.getSiteAccessList().add( new SiteAccessBean( remoteaddr, useragent ) );
            }
        }
        catch(SQLException sqlExc)
        {
            System.out.println("Exception getting connection from pool: " + sqlExc );
        }
        finally
        {
            try {
                if( null != rs ) rs.close();
                if( null != pstmt ) pstmt.close();
                if( null != conn ) conn.close();
                rs = null;
                pstmt = null;
                conn = null;
            } catch (Exception ex) {}
        }
        
    return listSiteAccess;
    }
    
    
    
    
    /**
     * 
     * @return
     */
    protected Connection getConnectionFromPool()
    {
        Connection conn = null;
        try
        {
            conn = getDataSource().getConnection();
        }
        catch(Exception exc)
        {
            System.out.println("Exception trying to get connection from ds: " + exc );
        }
        return conn;
    }    
    /**
     * 
     * @return
     */
    private DataSource getDataSource()
    {
        if( null == s_ds )
        {
            try
            {
                Context init = new InitialContext();
                Context ctx = (Context) init.lookup("java:comp/env");
                s_ds = (DataSource) ctx.lookup("jdbc/brettmelton_com");
            }
            catch ( NamingException namingExc )
            {
                System.out.println( "NamingException: " + namingExc );
            }
        }
        return s_ds;
    }
}
