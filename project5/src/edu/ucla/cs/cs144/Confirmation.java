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
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Confirmation extends HttpServlet implements Servlet {
       
    public Confirmation() {}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// your codes here
    	HttpSession session = request.getSession(true);
    	
    	Item item = (Item)session.getAttribute("item");
    	String ccn = request.getParameter("ccn");
    	long purchase_time = session.getLastAccessedTime();
    	
    	DateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
    	String ptime = format.format(purchase_time);

    	request.setAttribute("item",item);
    	request.setAttribute("ccn",ccn);
    	request.setAttribute("purchase_time",ptime);
    	session.setAttribute("item",item);
    	request.getRequestDispatcher("/confirmation.jsp").forward(request,response);
    }
}