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

public class ItemPayServlet extends HttpServlet implements Servlet {
       
    public ItemPayServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// your codes here
    	HttpSession session = request.getSession(true);
    	
    	Item item = (Item)session.getAttribute("item");

    	request.setAttribute("item",item);
    	session.setAttribute("item",item);
    	request.getRequestDispatcher("/itemPayResult.jsp").forward(request,response);
    }
}