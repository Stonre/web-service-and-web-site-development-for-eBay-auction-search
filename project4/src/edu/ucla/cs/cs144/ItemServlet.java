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

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    /*public static String Inputstr2Str_Reader(InputStream in, String encode)  
    {  
      
    String str = "";  
    try  
    {  
    if (encode == null || encode.equals(""))  
    {
                  encode = "utf-8";  
              }  
              BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));  
              StringBuffer sb = new StringBuffer();  
                
             while ((str = reader.readLine()) != null)  
              {  
                  sb.append(str).append("\n");  
              }  
              return sb.toString();  
           } 
           catch (IOException e)  
           {  
             e.printStackTrace();  
          }  
            
           return str;  
      } */ 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	// your codes here
    	String ItemID = request.getParameter("id");
    	String Item_xml_string = AuctionSearchClient.getXMLDataForItemId(ItemID);
    	
    	Item item = null;
    	String re="";
    	try{
    		JAXBContext jc = JAXBContext.newInstance(Item.class);
    		Unmarshaller unmarshaller = jc.createUnmarshaller();
    		InputStream streamSource = new ByteArrayInputStream(Item_xml_string.getBytes("UTF-8"));
    		//StringReader streamSource = new StringReader(Item_xml_string);
    		//re=Inputstr2Str_Reader(streamSource,null);request.setAttribute("xmlitem",re);
    		item = (Item) unmarshaller.unmarshal(streamSource);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	//if (item!=null){
    	//if (Item_xml_string==null||Item_xml_string=="")
    	//	request.setAttribute("xmlitem","null");
    	//else
    	//	request.setAttribute("xmlitem",Item_xml_string);
    		request.setAttribute("item",item);
    		request.getRequestDispatcher("/itemResult.jsp").forward(request,response);
    	//}
    	//else{
    	//	request.getRequestDispatcher("/itemResult.jsp").forward(request,response);
    	//}/
    }
}
