package edu.ucla.cs.cs144;

import java.util.Comparator;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

//This code refers to a github project: https://github.com/zoexi/eDaze/blob/master/project4/src/edu/ucla/cs/cs144/BidComparable.java

public class compareTime implements Comparator<Item.Bid>{
	public int compare(Item.Bid bid1,Item.Bid bid2){
		DateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		try{
			Date date1 = format.parse(bid1.Time);
			Date date2 = format.parse(bid2.Time);
			
			return date2.compareTo(date1);
		}catch(ParseException e){
			
		}
		return 0;
	}
}