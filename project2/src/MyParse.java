/* CS144
*
* Parser skeleton for processing item-???.xml files. Must be compiled in
* JDK 1.5 or above.
*
* Instructions:
*
* This program processes all files passed on the command line (to parse
* an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
*
* At the point noted below, an individual XML file has been parsed into a
* DOM Document node. You should fill in code to process the node. Java's
* interface for the Document Object Model (DOM) is in package
* org.w3c.dom. The documentation is available online at
*
* http://java.sun.com/j2se/1.5.0/docs/api/index.html
*
* A tutorial of Java's XML Parsing can be found at:
*
* http://java.sun.com/webservices/jaxp/
*
* Some auxiliary methods have been written for you. You may find them
* useful.
*/

//package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;


class MyParse {
   
   static final String columnSeparator = "|*|";
   static DocumentBuilder builder;
   static FileWriter item_table;
   static FileWriter bid_table;
   static FileWriter location_item;
   static FileWriter categ_table;
   static FileWriter bider_table;
   static FileWriter seller_table;
   
   static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
   };
   
   static class MyErrorHandler implements ErrorHandler {
       
       public void warning(SAXParseException exception)
       throws SAXException {
           fatalError(exception);
       }
       
       public void error(SAXParseException exception)
       throws SAXException {
           fatalError(exception);
       }
       
       public void fatalError(SAXParseException exception)
       throws SAXException {
           exception.printStackTrace();
           System.out.println("There should be no errors " +
                              "in the supplied XML files.");
           System.exit(3);
       }
       
   }
   
   /* Non-recursive (NR) version of Node.getElementsByTagName(...)
    */
   static Node[] getNodesByTagNameNR(Node e, String tagName) {
       Vector< Node > Nodes = new Vector< Node >();
       Node child = e.getFirstChild();
       while (child != null) {
           if (child instanceof Node && child.getNodeName().equals(tagName))
           {
               Nodes.add(child);
           }
           child = child.getNextSibling();
       }
       Node[] result = new Node[Nodes.size()];
       Nodes.copyInto(result);
       return result;
   }
   
   /* Returns the first subelement of e matching the given tagName, or
    * null if one does not exist. NR means Non-Recursive.
    */
   static Node getNodeByTagNameNR(Node e, String tagName) {
       Node child = e.getFirstChild();
       while (child != null) {
           if (child instanceof Node && child.getNodeName().equals(tagName))
               return child;
           child = child.getNextSibling();
       }
       return null;
   }
   
   /* Returns the text associated with the given element (which must have
    * type #PCDATA) as child, or "" if it contains no text.
    */
   static String getNodeText(Node e) {
       if (e.getChildNodes().getLength() == 1) {
           Text elementText = (Text) e.getFirstChild();
           return elementText.getNodeValue();
       }
       else
           return "";
   }
   
   /* Returns the text (#PCDATA) associated with the first subelement X
    * of e with the given tagName. If no such X exists or X contains no
    * text, "" is returned. NR means Non-Recursive.
    */
   static String getNodeTextByTagNameNR(Node e, String tagName) {
       Node elem = getNodeByTagNameNR(e, tagName);
       if (elem != null)
           return getNodeText(elem);
       else
           return "";
   }
   
   /* Returns the amount (in XXXXX.xx format) denoted by a money-string
    * like $3,453.23. Returns the input if the input is an empty string.
    */
   static String strip(String money) {
       if (money.equals(""))
           return money;
       else {
           double am = 0.0;
           NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
           try { am = nf.parse(money).doubleValue(); }
           catch (ParseException e) {
               System.out.println("This method should work for all " +
                                  "money values you find in our data.");
               System.exit(20);
           }
           nf.setGroupingUsed(false);
           return nf.format(am).substring(1);
       }
   }
   
   /* Process one items-???.xml file.
    */
   static void processFile(File xmlFile) throws IOException, ParseException {
       Document doc = null;
       try {
           doc = builder.parse(xmlFile);
       }
       catch (IOException e) {
           e.printStackTrace();
           System.exit(3);
       }
       catch (SAXException e) {
           System.out.println("Parsing error on file " + xmlFile);
           System.out.println("  (not supposed to happen with supplied XML files)");
           e.printStackTrace();
           System.exit(3);
       }
       Node n = doc.getFirstChild();
       Node[] m = getNodesByTagNameNR(n,"Item");
       for (int i=0;i<m.length;i++)
    	   generateCSV(m[i]);
       /* At this point 'doc' contains a DOM representation of an 'Items' XML
        * file. Use doc.getDocumentElement() to get the root Element. */
       System.out.println("Successfully parsed - " + xmlFile);
       
       /* Fill in code here (you will probably need to write auxiliary
           methods). */
       
       
       /**************************************************************/
       
       //recursiveDescent(doc, 0);
   }
   
   public static String dateformat(String d)throws ParseException{
	   SimpleDateFormat in = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	   SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String re="";
	   try{
		   Date tmpdate= in.parse(d);
		   re = out.format(tmpdate);
	   }
	   catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	   return re;
   }
   

   public static void generateCSV(Node n) throws IOException, ParseException
   {
	   String ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Longitute, Latitute, UserIDS, Started, Ends, Description, Location, Country, RatingS;
	   Vector<String> Category=new Vector(), RatingU =new Vector(), UserIDU =new Vector(), Time=new Vector(), Amount=new Vector(), Locationb=new Vector(), Countryb=new Vector();
	   
	   NamedNodeMap itemid = n.getAttributes();
	   ItemID = itemid.item(0).getNodeValue();
	   Name = '"' + getNodeTextByTagNameNR(n,"Name") + '"';
	   Currently = strip(getNodeTextByTagNameNR(n,"Currently"));
	   Buy_Price = strip(getNodeTextByTagNameNR(n,"Buy_Price"));
	   First_Bid = strip(getNodeTextByTagNameNR(n,"First_Bid"));
	   Number_of_Bids = getNodeTextByTagNameNR(n,"Number_of_Bids");
	   Country = '"' + getNodeTextByTagNameNR(n,"Country") + '"';
	   Started = dateformat(getNodeTextByTagNameNR(n,"Started"));
	   Location = '"' + getNodeTextByTagNameNR(n,"Location") + '"';
	   Node loca = getNodeByTagNameNR(n, "Location");
	   NamedNodeMap lo = loca.getAttributes();
	   if (lo.getLength()!=0)
	   {
		   Latitute = lo.item(0).getNodeValue();
	   		Longitute = lo.item(1).getNodeValue();
	   }
	   else
	   {
		   Latitute="";
		   Longitute="";
	   }
	   Ends = dateformat(getNodeTextByTagNameNR(n,"Ends"));
	   Description = getNodeTextByTagNameNR(n,"Description");
	   Node bids = getNodeByTagNameNR(n,"Bids");
	   Node[] bid = getNodesByTagNameNR(bids,"Bid");
	   Node seller = getNodeByTagNameNR(n,"Seller");
	   NamedNodeMap s = seller.getAttributes();
	   RatingS = s.item(0).getNodeValue();
	   UserIDS = s.item(1).getNodeValue();
	   Node[] category = getNodesByTagNameNR(n,"Category");
	   for (int i=0;i<bid.length;i++){
		   Node bider = getNodeByTagNameNR(bid[i],"Bidder");
		   NamedNodeMap bidertmp = bider.getAttributes();
		   RatingU.add(bidertmp.item(0).getNodeValue());
		   UserIDU.add(bidertmp.item(1).getNodeValue());
		   Time.add(dateformat(getNodeTextByTagNameNR(bid[i],"Time")));
		   Amount.add(strip(getNodeTextByTagNameNR(bid[i],"Amount")));
		   Locationb.add('"' + getNodeTextByTagNameNR(bider,"Location") + '"');
		   Countryb.add('"' + getNodeTextByTagNameNR(bider,"Country") + '"');
	   }
	   for (int i=0;i<category.length;i++)
	   {
		   Category.add('"' + getNodeText(category[i]) + '"');
	   }
	   
	   for (int i=0;i<category.length;i++)
	   {
		   categ_table.append(ItemID + ',' + Category.elementAt(i) + '\n');
	   }
	   
	   item_table.append(ItemID + ',' + Name + ',' + Currently + ',' + Buy_Price + ',' + First_Bid + ',' + 
			   Number_of_Bids + ',' + UserIDS + ',' + Started + ',' + Ends + ',' + '"' + Description + '"' + '\n');
	   
	   for (int i=0;i<bid.length;i++)
	   {
		   bid_table.append(ItemID + ',' + UserIDU.elementAt(i) + ',' + Time.elementAt(i) + ',' + Amount.elementAt(i) + '\n');
		   bider_table.append(UserIDU.elementAt(i) + ',' + RatingU.elementAt(i) + ',' + Locationb.elementAt(i) + ',' + Countryb.elementAt(i) + '\n');
	   }
	   location_item.append(ItemID + ',' + Longitute + ',' + Latitute + ',' + Location + ',' + Country + '\n');
	   seller_table.append(UserIDS + ',' + RatingS + '\n');
   }
   
   /*public static void recursiveDescent(Node n, int level) {
       // adjust indentation according to level
       for(int i=0; i<4*level; i++)
           System.out.print(" ");
       
       // dump out node name, type, and value  
       String ntype = typeName[n.getNodeType()];
       String nname = n.getNodeName();
       String nvalue = n.getNodeValue();
       
       System.out.println("Type = " + ntype + ", Name = " + nname + ", Value = " + nvalue);
       
       // dump out attributes if any
       NamedNodeMap nattrib = n.getAttributes();
       if(nattrib != null && nattrib.getLength() > 0)
           for(int i=0; i<nattrib.getLength(); i++)
               recursiveDescent(nattrib.item(i),  level+1);
       
       // now walk through its children list
       org.w3c.dom.NodeList nlist = n.getChildNodes();
       
       for(int i=0; i<nlist.getLength(); i++)
           recursiveDescent(nlist.item(i), level+1);
   }*/
   
   public static void main (String[] args){
       if (args.length == 0) {
           System.out.println("Usage: java MyParser [file] [file] ...");
           System.exit(1);
       }
       
       /* Initialize parser. */
       try {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setValidating(false);
           factory.setIgnoringElementContentWhitespace(true);      
           builder = factory.newDocumentBuilder();
           builder.setErrorHandler(new MyErrorHandler());
           item_table = new FileWriter("item.csv");
		   bid_table = new FileWriter("bid.csv");
		   location_item = new FileWriter("location_item.csv");
		   categ_table = new FileWriter("categ.csv");
		   bider_table = new FileWriter("bider.csv");
		   seller_table = new FileWriter("seller.csv");
		   
	       for (int i = 0; i < args.length; i++) {
	           File currentFile = new File(args[i]);
	           processFile(currentFile);
	       }
	       item_table.flush();
	       item_table.close();
	       bid_table.flush();
	       bid_table.close();
	       location_item.flush();
	       location_item.close();
	       categ_table.flush();
	       categ_table.close();
	       bider_table.flush();
	       bider_table.close();
	       seller_table.flush();
	       seller_table.close();
       }
       catch (FactoryConfigurationError e) {
           System.out.println("unable to get a document builder factory");
           System.exit(2);
       } 
       catch (ParserConfigurationException e) {
           System.out.println("parser was unable to be configured");
           System.exit(2);
       } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
       
       /* Process all files listed on command line. */

   }
}
