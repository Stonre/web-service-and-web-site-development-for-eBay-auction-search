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
	<%String ccn = (String)request.getAttribute("ccn");%>
	<%String ptime = (String)request.getAttribute("purchase_time");%>
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
			<li role="presentation"><a href="http://<%=request.getServerName()%>:1448<%=request.getContextPath()%>/keywordSearch.html">KeywordSearch</a></li>
			<li role="presentation"><a href="http://<%=request.getServerName()%>:1448<%=request.getContextPath()%>/getItem.html">ItemSearch</a></li>
      <form name="query" action="http://<%=request.getServerName()%>:1448<%=request.getContextPath()%>/item" class="navbar-form navbar-right" method="GET" role="search">
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
	<h2  style="font-family:courier"><b>Payment Confirmation:</b></h2>
	<li><b>Item Name: </b><%=item.Name%></li>
	<li><b>ItemID: </b><%=item.ItemID%></li>
	<%
		if (item.Buy_Price!=null){
	%>
	<li><b>Buy Price: </b><%=item.Buy_Price%></li>
	<li><b>Credit Card Number: </b><%=ccn%></li>
	<li><b>Purchase Time: </b><%=ptime%></li>
	<%
		}
	%>
	
	<%}%>
</ul>
</div>
<%if (item!=null){%>
<%}%>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>