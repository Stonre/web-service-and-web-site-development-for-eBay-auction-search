package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		SearchResult[] results = null;
		try{
			SearchEngine s = new SearchEngine();
			TopDocs td = s.performSearch(query,numResultsToSkip+numResultsToReturn);
			ScoreDoc[] hits = td.scoreDocs;
			hits = Arrays.copyOfRange(hits, numResultsToSkip, hits.length);
			results = new SearchResult[hits.length];
			for (int i=0;i<hits.length;i++){
				Document doc = s.getDocument(hits[i].doc);
				results[i] = new SearchResult(doc.get("ItemID"),doc.get("Name"));
			}
		}catch(Exception e){
			System.out.println("Exception caught for basic Search");
		}
		return results;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		SearchResult[] results = null;
		try{
			Connection con = DbManager.getConnection(true);
			String area = "Polygon((" + region.getLx() + " " + region.getLy() + "," + region.getLx() + " " + region.getRy() + "," + region.getRx() + " " + region.getRy() + "," +
										region.getRx() + " " + region.getLy() + "," + region.getLx() + " " + region.getLy() + "))";
			String sqlSpatialQuery = "SELECT ItemID FROM geom WHERE Contains(GeomFromText(' "+ area +" '), pos);";
			Statement stmt = con.createStatement();	
			ResultSet spatialResult = stmt.executeQuery(sqlSpatialQuery);
			SearchResult[] basicResults = basicSearch(query,0,Integer.MAX_VALUE);
			HashSet<String> idmap = new HashSet<String>();
			while(spatialResult.next()){
				if(idmap.contains(spatialResult.getString("ItemID"))) continue;
				else idmap.add(spatialResult.getString("ItemID"));
			}
			Vector<SearchResult> intersectResult = new Vector<SearchResult>();
			for(SearchResult br : basicResults){
				if(idmap.contains(br.getItemId())) intersectResult.addElement(br);
				else continue;
			}
			int len = 0,totalLen = intersectResult.size();
			if(numResultsToSkip >= totalLen) len = 0;
			if(numResultsToSkip < totalLen && numResultsToReturn + numResultsToSkip > totalLen) len = totalLen - numResultsToSkip;
			if(numResultsToReturn + numResultsToReturn <= totalLen) len = numResultsToReturn;
			results = new SearchResult[len];
			for(int i=numResultsToSkip;i<len;i++){
				results[i]=intersectResult.elementAt(i);
			}
		}catch(Exception e){
			System.out.println("Exception caught for spatial Search");
		}
		return results;
	}
	
	public String Time_Convert(String t){
		String re = "";
		SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat out = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		try{
			Date d = in.parse(t);
			re = out.format(d);
		}catch(Exception e){
		}
		return re;
	}
	
	public String string_escape(String s){
		String re = s;
		String[] tableo = {"<",">","&","\"","\'"};
		String[] tabler = {"&lt;","&gt;","&amp;","&quot;","&apos;"};
		
		for (int i=0;i<tableo.length;i++){
			re = re.replaceAll(tableo[i], tabler[i]);
		}
		return re;
	}

	public String getXMLDataForItemId(String itemId) {
		String re = "";
		try{
			Connection conn = DbManager.getConnection(true);
			
			Statement stmt_item = conn.createStatement();
			String Queryitem = "SELECT * FROM item WHERE ItemID=" + itemId + ";";
			ResultSet itemset = stmt_item.executeQuery(Queryitem);
			
			if (!itemset.isBeforeFirst()){
				conn.close();
				return re;
			}
			
			itemset.next();
			re = re + "<Item ItemID=\"" + itemId + "\">\n";
			re = re + "  <Name>" + itemset.getString("Name") + "</Name>\n";
			
			String Querycateg = "SELECT * FROM categ WHERE ItemID=" + itemId + ";";
			Statement stmt_categ = conn.createStatement();
			ResultSet categset = stmt_categ.executeQuery(Querycateg);
			while (categset.next()){
				re = re + "  <Category>" + string_escape(categset.getString("Category")) + "</Category>\n";
			}
			
			String current = String.format("$%.2f", itemset.getFloat("Currently"));
			re = re + "  <Currently>" + current + "</Currently>\n";
			
			float buy_price = itemset.getFloat("Buy_Price");
			if (buy_price!=0){
				re = re + "  <Buy_Price>" + String.format("$%.2f", buy_price) + "</Buy_Price>\n";
			}
			
			String first_price = String.format("$%.2f", itemset.getFloat("First_Bid"));
			re = re + "  <First_Bid>" + first_price + "</First_Bid>\n";
			int numofbid = itemset.getInt("Number_of_Bids");
			re = re + "  <Number_of_Bids>" + numofbid + "</Number_of_Bids>\n";
			
			String bids = "";
			if (numofbid==0)
				bids = "  <Bids />\n";
			else{
				bids = bids + "  <Bids>\n";
				Statement stmt_bids = conn.createStatement();
				String Querybids = "SELECT * FROM bid WHERE ItemID=" + itemId + ";";
				ResultSet bidsset = stmt_bids.executeQuery(Querybids);
				while (bidsset.next()){
					bids = bids + "    <Bid>\n";
					String bidsid = bidsset.getString("UserID");
					Statement stmt_bider = conn.createStatement();
					String Querybider = "SELECT * FROM bider WHERE UserID=\"" + bidsid + "\";";
					ResultSet biderset = stmt_bider.executeQuery(Querybider);
					biderset.next();
					bids = bids + "      <Bidder Rating=\"" + biderset.getString("Rating") +
							"\" UserID=\"" + bidsid + "\">\n";
					bids = bids + "        <Location>" + biderset.getString("Location") + "</Location>\n";
					bids = bids + "        <Country>" + biderset.getString("Country") + "</Country>\n";
					bids = bids + "      </Bidder>\n";
					bids = bids + "      <Time>" + Time_Convert(bidsset.getString("Time")) + "</Time>\n";
					bids = bids + "      <Amount>$" + bidsset.getString("Amount") + "</Amount>\n";
					bids = bids + "    </Bid>\n";
				}
			}
			re = re + bids;
			
			Statement stmt_location = conn.createStatement();
			String Querylocation = "SELECT * FROM location_item WHERE ItemID=" + itemId + ";";
			ResultSet locationset = stmt_location.executeQuery(Querylocation);
			locationset.next();
			re = re + "  <Location";
			if (locationset.getFloat("Latitute")!=0){
				re = re + " Latitude=\"" + locationset.getFloat("Latitute") + "\" ";
			}
			if (locationset.getFloat("Longitute")!=0){
				re = re + "Longitude=\"" + locationset.getFloat("Longitute") + "\"";
			}
			re = re + ">";
			re = re + string_escape(locationset.getString("Location")) + "</Location>\n";
			re = re + "  <Country>" + string_escape(locationset.getString("Country")) + "</Country>\n";
			re = re + "  <Started>" + Time_Convert(itemset.getString("Started")) + "</Started>\n";
			re = re + "  <Ends>" + Time_Convert(itemset.getString("Ends")) + "</Ends>\n";
			String tmp = itemset.getString("UserID");
			Statement stmt_seller = conn.createStatement();
			String Querylocation2 = "SELECT * FROM seller WHERE UserID=\"" + tmp + "\";";
			ResultSet sellerset = stmt_seller.executeQuery(Querylocation2);
			sellerset.next();
			re = re + "  <Seller Rating=\"" + sellerset.getString("Rating") +
					"\" UserID=\"" + tmp + "\"/>\n";
			re = re + "  <Description>" + string_escape(itemset.getString("Description")) + "</Description>\n";
			re = re + "</Item>";
		}catch (Exception e){
			
		}
		return re;
	}
	
	public String echo(String message) {
		return message;
	}

}
