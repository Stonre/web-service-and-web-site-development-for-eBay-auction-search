package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpSession;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// your codes here
    	String ItemID = request.getParameter("id");
    	String Item_xml_string = AuctionSearchClient.getXMLDataForItemId(ItemID);
    	HttpSession session = request.getSession(true);
    	
    	Item item = null;
    	String re="";
    	try{
    		JAXBContext jc = JAXBContext.newInstance(Item.class);
    		Unmarshaller unmarshaller = jc.createUnmarshaller();
    		InputStream streamSource = new ByteArrayInputStream(Item_xml_string.getBytes("UTF-8"));
    		item = (Item) unmarshaller.unmarshal(streamSource);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	session.setAttribute("item",item);
    	request.setAttribute("item",item);
    	request.getRequestDispatcher("/itemResult.jsp").forward(request,response);
    }
}
