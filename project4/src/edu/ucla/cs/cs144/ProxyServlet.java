package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	response.setContentType("text/xml");
        try{
        	String queryStr = request.getQueryString();
            String pathUrl = "http://google.com/complete/search?output=toolbar&";
            URL url = new URL(pathUrl+queryStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            BufferedReader inString = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuilder tmpStr = new StringBuilder();
            String Line;
            PrintWriter outString = response.getWriter();
            while((Line = inString.readLine()) != null){
            	tmpStr.append(Line + '\n');
            	outString.println(tmpStr.toString());
            }
        }catch(Exception error){
        	error.printStackTrace();
        }
    }
}
