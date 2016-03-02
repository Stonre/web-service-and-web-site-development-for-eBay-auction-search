package edu.ucla.cs.cs144;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="Item")
public class Item{
	@XmlAttribute(name="ItemID")
	public String ItemID;
	
	@XmlElement(name="Name")
	public String Name;
	
	@XmlElement(name="Category")
	public Vector<String> Category = new Vector<String>();
	
	@XmlElement(name="Currently")
	public String Currently;
	
	@XmlElement(name="First_Bid")
	public String First_Bid;
	
	@XmlElement(name="Number_of_Bids")
	public String Number_of_Bids;
	
	@XmlElement(name="Bids")
	public Bids bids;
	
	@XmlElement(name="Location")
	public Location location;
	
	@XmlElement(name="Country")
	public String Country;
	
	@XmlElement(name="Started")
	public String Started;
	
	@XmlElement(name="Ends")
	public String Ends;
	
	@XmlElement(name="Seller")
	public Seller seller;
	
	@XmlElement(name="Description")
	public String Description;
	
	//@XmlElement(name="Bid")
	//public List<Bid> Bids = new ArrayList<Bid>();
	
	public static class Bids {
		@XmlElement(name="Bid")
		public List<Bid> bid = new ArrayList<Bid>();
	}
	
	public static class Bid{
		@XmlElement(name="Bidder")
		public Bider bider;
		
		@XmlElement(name="Time")
		public String Time;
		
		@XmlElement(name="Amount")
		public String Amount;
	}
	
	public static class Bider{
		@XmlAttribute(name="UserID")
		public String UserID;
		
		@XmlAttribute(name="Rating")
		public String Rating;
		
		@XmlElement(name="Location")
		public String Location;
		
		@XmlElement(name="Country")
		public String Country;
	}
	
	public static class Location{
		@XmlAttribute(name="Latitude")
		public String Latitude;
		
		@XmlAttribute(name="Longitude")
		public String Longitude;
		
		@XmlValue
		public String Location;
	}
	
	public static class Seller{
		@XmlAttribute(name="UserID")
		public String UserID;
		
		@XmlAttribute(name="Rating")
		public String Rating;
	}
}