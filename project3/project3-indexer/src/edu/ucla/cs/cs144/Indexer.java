package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/indexer"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }
    
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    
    public void indexitem(Vector<String> ss) throws IOException{
    	IndexWriter writer = getIndexWriter(false);
    	Document doc = new Document();
    	doc.add(new StringField("ItemID",ss.elementAt(0),Field.Store.YES));//System.out.println(ss.elementAt(0));
    	doc.add(new StringField("Name",ss.elementAt(1),Field.Store.YES));
    	String fullSearchableText;
    	fullSearchableText = ss.elementAt(1) + " " + ss.elementAt(2) + " " + ss.elementAt(3);
    	doc.add(new TextField("content",fullSearchableText,Field.Store.NO));
    	writer.addDocument(doc);
    }
 
    public void rebuildIndexes(){

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	    
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT item.ItemID,Name,Category,Description FROM item left join categ on item.ItemID=categ.ItemID");
		String categtmp;
		String categ;
		String Idtmp;
		String ItemID;
		String Nametmp;
		String Descriptiontmp;
		String Name, Description;
		rs.next();
		Vector<Vector<String>> tmp1 = new Vector<Vector<String>>();
		Vector<String> tmp2 = new Vector<String>();
		categ = rs.getString("Category");
		ItemID = rs.getString("ItemID");
		Name = rs.getString("Name");
		Description = rs.getString("Description");
		while (rs.next()){
			Idtmp = rs.getString("ItemID");
			categtmp = rs.getString("Category");
			Nametmp = rs.getString("Name");
			Descriptiontmp = rs.getString("Description");
			
			if (Idtmp.equals(ItemID)){
				categ = categ + " " + categtmp;
			}
			else{
				tmp2.addElement(ItemID);
				tmp2.addElement(Name);
				tmp2.addElement(categ);
				tmp2.addElement(Description);
				//tmp1.addElement(tmp2);
				indexitem(tmp2);
				ItemID = Idtmp;
				categ = categtmp;
				Name = Nametmp;
				Description = Descriptiontmp;
				tmp2.clear();
			}
		}
		tmp2.addElement(ItemID);
		tmp2.addElement(Name);
		tmp2.addElement(categ);
		tmp2.addElement(Description);
		//tmp1.addElement(tmp2);System.out.println(tmp1.size());
		indexitem(tmp2);
		rs.close();
		
		/*ResultSet rs2 = stmt.executeQuery("SELECT * FROM categ");
		String categtmp;
		String categ;
		String Idtmp;
		String ItemID;
		String Name, Description;
		Vector<Vector<String>> rs1s = new Vector<Vector<String>>();
		Vector<String> rs2s = new Vector<String>();
		rs2.next();
		categ = rs2.getString("Category");
		ItemID = rs2.getString("ItemID");
		while (rs2.next()){
			Idtmp = rs2.getString("ItemID");
			categtmp = rs2.getString("Category");
			if (Idtmp.equals(ItemID)){
				categ = categ + " " + categtmp;
			}
			else{
				rs2s.addElement(categ);
				ItemID = Idtmp;
				categ = categtmp;
			}
		}
		rs2s.addElement(categ);System.out.println(rs2s.size());
		rs2.close();
		stmt.close();
		
		Statement stmt2 = conn.createStatement();
		ResultSet rs1 = stmt2.executeQuery("SELECT ItemID, Name, Description FROM item");
		while (rs1.next()){
			ItemID = rs1.getString("ItemID");			Vector<String> tmp = new Vector<String>();
			Name = rs1.getString("Name");
			Description = rs1.getString("Description");
			tmp.addElement(ItemID);
			tmp.addElement(Name);
			tmp.addElement(Description);
			rs1s.addElement(tmp);
		}
		System.out.println(rs1s.size());
		rs1.close();
		stmt2.close();*/
		/*for (int i = 0;i<tmp1.size();i++){
			indexitem(tmp1.elementAt(i));
		}*/
		closeIndexWriter();
	} catch (SQLException ex) {
	    System.out.println(ex);
	} catch (Exception e){
    	System.out.println("Exception caught.\n");
    }


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
	/*try{

	} catch (SQLException ex){
        System.out.println("SQLException caught");
        System.out.println("---");
        while ( ex != null ){
            System.out.println("Message   : " + ex.getMessage());
            System.out.println("SQLState  : " + ex.getSQLState());
            System.out.println("ErrorCode : " + ex.getErrorCode());
            System.out.println("---");
            ex = ex.getNextException();
        }
   }*/
	
	
        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
	    try{	
	        Indexer idx = new Indexer();
	        idx.rebuildIndexes();
	    }catch (Exception e){
	    	System.out.println("Exception caught main.\n");
	    }
    }   
}
