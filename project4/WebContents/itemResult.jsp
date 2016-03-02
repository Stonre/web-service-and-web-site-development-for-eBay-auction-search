<%@ page language="java" contentType="text/html" import="edu.ucla.cs.cs144.*, java.util.Collections" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
	<title>Ebay data item search results</title>
    <!-- Autosuggest -->
	<style type="text/css">
		html {height:100%}
		body {height:100%}
	</style> 
	<%Item item = (Item)request.getAttribute("item");%>
	<%if (item!=null){%>
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
	<script type="text/javascript">
		function initialize(){
			var latitude = <%=item.location.Latitude%>;
			var longitude = <%=item.location.Longitude%>;
			if(latitude && longitude){
				var latlng = new google.maps.LatLng(latitude,longitude);
				var myOptions = { 
					zoom: 14, // default is 8  
					center: latlng, 
					mapTypeId: google.maps.MapTypeId.ROADMAP 
				}; 
				var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
				var marker = new google.maps.Marker({position:latlng, title:"Item Location"});
				marker.setMap(map);
			}
			else{
				var geocoder = new google.maps.Geocoder();
				var address = "<%=item.location.Location + " " + item.Country%>";
				var latlng = new google.maps.LatLng(0,0);
				var myOptions = { 
					zoom: 1, // default is 8  
					center: latlng,
					mapTypeId: google.maps.MapTypeId.ROADMAP 
				};
				var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
				var geocoderRequest = {address : address};
				geocoder.geocode(geocoderRequest, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
					map.setCenter(results[0].geometry.location);
					map.setZoom(14);
					var marker = new google.maps.Marker({position:results[0].geometry.location,title:"Item Location"});
					marker.setMap(map);
					}
				});
			}
		}
	</script>
	<%}%>
</head>
<body onload="initialize()">
<!-- NAV BAR AND FORM TO ISSUE A NEW SEARCH QUERY -->
<nav class="navbar navbar-default">
  <div class="container-fluid" >
    <!-- Brand and toggle get grouped for better mobile display -->
    <!-- Collect the nav links, forms, and other content for toggling -->
      <ul class="nav nav-pills">
			<li role="presentation" class="active"><a href="#">EbayData</a></li>
			<li role="presentation"><a href="./keywordSearch.html">KeywordSearch</a></li>
			<li role="presentation"><a href="./getItem.html">ItemSearch</a></li>
      <form name="query" action="/eBay/item" class="navbar-form navbar-right" method="GET" role="search">
        <div class="form-group">
          <input type="text" name="id" class="form-control" placeholder="Search by item...">
        </div>
        <button type="submit" class="btn btn-default">Search</button>
      </form>
	  </ul>
  </div><!-- /.container-fluid -->
</nav>

<div class="container-fluid">
<ul>
	<%
		if (item==null){
	%> 
	<div align="center"><h3>No results are found</h3></div>
		<%}
		else{
			%>
	<h2  style="font-family:courier"><b>Item Search Result:</b></h2>
	<b><%=item.Name%>,        ItemID: <%=item.ItemID%></b>
	<li>
		<%
			int len=item.Category.size();
			String category = "";
			for (int i=0;i<len;i++){
				category = category + item.Category.elementAt(i) + ";";
			}
		%>
		<b>Category</b>: <%=category%>
	</li>
	<li>
		<b>Currently</b>: <%=item.Currently%>
	</li>
	<li>
		<b>First Bid</b>: <%=item.First_Bid%>
	</li>
	<li>
		<b>Number of Bids</b>: <%=item.Number_of_Bids%>
	</li>
	<%
		int lenbid = Integer.parseInt(item.Number_of_Bids);
		if (lenbid>0){
			Collections.sort(item.bids.bid,new compareTime());
	%>
	<li>
		<b>Bids</b>
	<%
		int count=1;
		for (Item.Bid bid: item.bids.bid){
	%>
		<ul style="text-indet：2em">
			<b>Bid No</b>.<%=count%>
			<li style="text-indet：2em"><b>Bider</b>: <b>UserID</b>=<%=bid.bider.UserID%>, <b>Rating</b>=<%=bid.bider.Rating%>, <b>Location</b>=<%=bid.bider.Location%>, <b>Country</b>=<%=bid.bider.Country%></li>
			<li style="text-indet：2em"><b>Time</b>: <%=bid.Time%></li>
			<li style="text-indet：2em"><b>Amount</b>: <%=bid.Amount%></li>
		</ul>
	<%
		count++;
		}
		}	
	%>
	</li>
	<li>
		<b>Location</b>: <b>Longitude</b>=<%=item.location.Longitude%>, <b>Latitude</b>=<%=item.location.Latitude%>, <%=item.location.Location%>
	</li>
	<li>
		<b>Country</b>: <%=item.Country%>
	</li>
	<li>
		<b>Started</b>: <%=item.Started%>
	</li>
	<li>
		<b>Ends</b>: <%=item.Ends%>
	</li>
	<li>
		<b>Seller</b>: <b>UserID</b>=<%=item.seller.UserID%>, <b>Rating</b>=<%=item.seller.Rating%>
	</li>
	<li>
		<b>Description</b>: <%=item.Description%>
	</li>
		<%}%>
</ul>
</div>
<%if (item!=null){%>
<div style="position:relative; left:50px"><h2 style="font-family:courier"><b>Item Location:</b></h2></div>
<div id="map_canvas" style="width:80%; height:80%; border-style:solid; border-width:2px; border-color:#000; position:relative; left:50px"></div>
<%}%>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>