package com.melton.ws;

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
 
/**
 * Root resource (exposed at "access" path)
 */
@Path("siteaccess")
public class MyResource {
 
    private static DataSource s_ds;

	public MyResource() {
		super();
	}
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)   // TEXT_PLAIN
    public SiteAccessBean getIt(
        @DefaultValue("2") @QueryParam("num") int iNumItems) {
    	    	
        SiteAccessBean sab = null;
    
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
	
                sab = new SiteAccessBean( remoteaddr, useragent );     
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
        
    return sab;
    
//    	return sbReturn.toString();
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
