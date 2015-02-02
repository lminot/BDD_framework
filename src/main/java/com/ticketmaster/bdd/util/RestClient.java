package com.ticketmaster.bdd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import wslite.http.HTTPClient;
import wslite.http.HTTPMethod;
import wslite.http.HTTPRequest;
import wslite.http.HTTPResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;


@SuppressWarnings("all")
public class RestClient {

	private static HTTPClient httpclient = new HTTPClient();
	private  static HTTPRequest req = new HTTPRequest();
	private static HTTPResponse resp = null;
	
	public static HTTPResponse get(String url, Map header) throws Exception
	{ 
		 URL url1 = new URL(url);
		 req.setMethod(HTTPMethod.GET);
		 req.setHeaders(header);
		 req.setUrl(url1);
		 resp = httpclient.execute(req);
		 
		 return resp ;
	}
	
	public static ClientResponse post(String requestBody,Map header,String url)throws Exception
	{	
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML, "");
			response = builder.type(MediaType.APPLICATION_XML).post(ClientResponse.class, requestBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * post json request
	 * @param resource - REST resource
	 * @param requestBody - JSON request body
	 * @return
	 * @throws Exception
	 */
	public static ClientResponse postJson(Map reqHeader, String url, String requestBody)throws Exception
	{		
		System.out.println("endPoint=  " + url);
		ClientResponse response=null;
		try {
			Client client = Client.create();
			// filter is for printing  request and response in the console.
			client.addFilter(new LoggingFilter(System.out));
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			//set the headers
			if (reqHeader!=null && !reqHeader.isEmpty())
			{
				Iterator it = reqHeader.entrySet().iterator();
				while (it.hasNext()) 
				{
					Map.Entry pairs = (Map.Entry)it.next();
					builder.header((String) pairs.getKey(), pairs.getValue());
				}
			}
			response = builder.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}	
	/**
	 * post txt/json request
	 * @param resource - REST resource
	 * @param requestBody - JSON request body
	 * @return
	 * @throws Exception
	 */
	public static ClientResponse postTxtJson(Map reqHeader, String url, String requestBody)throws Exception
	{		
		System.out.println("endPoint=  " + url);
		ClientResponse response=null;
		try {
			Client client = Client.create();
			// filter is for printing  request and response in the console.
			client.addFilter(new LoggingFilter(System.out));
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			//set the headers
			if (reqHeader!=null && !reqHeader.isEmpty())
			{
				Iterator it = reqHeader.entrySet().iterator();
				while (it.hasNext()) 
				{
					Map.Entry pairs = (Map.Entry)it.next();
					builder.header((String) pairs.getKey(), pairs.getValue());
				}
			}
			response = builder.type("text/json").post(ClientResponse.class, requestBody);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static ClientResponse postXml(String requestBody,String url)throws Exception
	{		
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML, "");
			response = builder.type(MediaType.APPLICATION_XML).post(ClientResponse.class, requestBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static ClientResponse getJSON(String url,Map reqHeader)throws Exception
	{
		System.out.println("endPoint=  " + url);
		ClientResponse response=null;
		try {
			Client client = Client.create();
			// filter is for printing  request and response in the console.
			client.addFilter(new LoggingFilter(System.out));
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			//set the headers
			if (reqHeader!=null && !reqHeader.isEmpty())
			{
				Iterator it = reqHeader.entrySet().iterator();
				while (it.hasNext()) 
				{
					Map.Entry pairs = (Map.Entry)it.next();
					builder.header((String) pairs.getKey(), pairs.getValue());
				}
			}
			response = builder.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static ClientResponse put(String requestBody, Map header, String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		System.out.println("endPoint=  " + url);
		try {
			Client client = Client.create();
			// filter is for printing  request and response in the console.
			client.addFilter(new LoggingFilter(System.out));
			WebResource webResource = client.resource(url);

			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			//set the headers
			if (header!=null && !header.isEmpty()){
				Iterator it = header.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
					builder.header((String) pairs.getKey(), pairs.getValue());
				}
			}

			response = builder.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, requestBody);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return response;
	}
	
	public static ClientResponse putXML(String requestBody,String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);

			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML, "");

			response = builder.type(MediaType.APPLICATION_XML).put(ClientResponse.class, requestBody);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }	 
		return response;
	}
	
	public static ClientResponse putJSON(String requestBody,String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			client.addFilter(new LoggingFilter(System.out));
			
			
			WebResource webResource = client.resource(url);

			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");

			response = builder.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, requestBody);
		  } catch (Exception e) {	 
			  e.printStackTrace();	 
		  }
		return response;
	}
	
	public static ClientResponse putJSON(String requestBody,String url,Map reqHeader)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			client.addFilter(new LoggingFilter(System.out));

			WebResource webResource = client.resource(url);

			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			if (reqHeader!=null && !reqHeader.isEmpty()){
							Iterator it = reqHeader.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry pairs = (Map.Entry)it.next();
								builder.header((String) pairs.getKey(), pairs.getValue());
							}
						}
			response = builder.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, requestBody);
		  } catch (Exception e) {	 
			  e.printStackTrace();	 
		  }
		return response;
	}
	
	public static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
			is.close();}catch(Exception e){}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
 
	}
	
	public static ClientResponse delete(Map header,String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML, "");
			response = builder.type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return response;
	}
	
	public static ClientResponse deleteXML(String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML, "");
			response = builder.type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return response;
	}
	
	public static ClientResponse deleteJSON(String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			response = builder.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return response;
	}
	
	public static ClientResponse getXML(String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url.trim());
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_XML, "");
			response = builder.type(MediaType.APPLICATION_XML).get(ClientResponse.class);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return response;
	}
	
	public static ClientResponse getJSON(String url)throws Exception
	{
		String output=null;
		ClientResponse response=null;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_JSON, "");
			response = builder.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return response;
	}

}
