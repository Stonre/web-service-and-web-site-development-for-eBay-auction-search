package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
    	try{
    		String query = request.getParameter("q");
    		int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
    		int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
    		
    		AuctionSearchClient searchClient = new AuctionSearchClient();
    		SearchResult[] results = searchClient.basicSearch(query,numResultsToSkip,numResultsToReturn);
    		request.setAttribute("q", query);
            request.setAttribute("results", results);
            request.setAttribute("numResultsToSkip", numResultsToSkip);
            request.setAttribute("numResultsToReturn", numResultsToReturn);
            request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    	}catch(Exception e){
    		
    	}
    }
}
