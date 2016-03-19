<%@ page language="java" contentType="text/html" import="edu.ucla.cs.cs144.*" %>
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
	<link rel="stylesheet" href="autosuggestion.css">
		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
		<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
		<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		<!-- AutoSuggest -->
		<script src="autosuggest.js" type="text/javascript"></script>
		<script type="text/javascript">
			window.onload = function(){
				var oTextbox = new AutoSuggestControl(document.getElementById("txt1"), new getSuggestionsFrom());
			}
		</script>
    <!-- Autosuggest -->
<% 	
    String query = (String) request.getAttribute("q");
    if (query != null && !query.isEmpty()) {
%>
      <title><%= query %>Ebay data key word search results</title>
<%
    }
    else {
%>
      <title>Ebay data key word search results</title>
<%
	}
%>
</head>

<body>
<!-- NAV BAR AND FORM TO ISSUE A NEW SEARCH QUERY -->
<nav class="navbar navbar-default">
  <div class="container-fluid">
      <ul class="nav nav-pills">
			<li role="presentation" class="active"><a href="#">EbayData</a></li>
			<li role="presentation"><a href="./keywordSearch.html">KeywordSearch</a></li>
			<li role="presentation"><a href="./getItem.html">ItemSearch</a></li>
      <form action="/eBay/search" method="GET" class="navbar-form navbar-right" role="search">
        <div class="form-group" id="form">
            <input type="text" name="q" id="txt1" placeholder="Search by keyword..." class="form-control">
            <input type="hidden" name="numResultsToSkip" value="0">
            <input type="hidden" name="numResultsToReturn" value="10">
        </div>
        <button type="submit" class="btn btn-default">Search</button>
      </form>
	  </ul>
  </div>
</nav>

<%
	int numToReturn;
	int numToSkip;
	SearchResult[] results = (SearchResult[])request.getAttribute("results");
	try{
		numToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
		numToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
	}catch(Exception e){
		numToSkip = 0;
		numToReturn = 0;
	}
	if (numToSkip<0||results.length==0)
	{
%>
<div align="center"><h3>No results are found</h3></div>

<%
	}
	else{
%>
<h2 style="font-family:courier"><b>Keyword Search Result:</b></h2>
<div class="panel panel-default">
<div class="panel-heading">Keyword Search Results from <%=numToSkip+1%> to <%=numToReturn+numToSkip%></div>
<table class="table  table-bordered table-striped">

<%
	int len = results.length;
	int counttime = 0;
	String ItemID = null;
	String Name = null;
	
	for (SearchResult result: results){
		if (counttime>10)
			break;
		ItemID = result.getItemId ();
		Name = result.getName();
		counttime++;
%>
	<tr>
		<td class="text-center">
			<a href="/eBay/item?id=<%=ItemID%>"><%=ItemID%></a>
		</td>
		<td>
			<%=Name%>
		</td>
	</tr>
<%
	}
%>
	</table>
</div>
<div class="row">
	<div style="text-align: center">
		<a href="/eBay/search?q=<%=query%>&numResultsToSkip=<%=numToSkip-10%>&numResultsToReturn=<%=numToReturn%>">Previous</a> | 
		<a href="/eBay/search?q=<%=query%>&numResultsToSkip=<%=numToSkip+10%>&numResultsToReturn=<%=numToReturn%>">Next</a>
	</div>
</div>
<%
	}
%>


    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>