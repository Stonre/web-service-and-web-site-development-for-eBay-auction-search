=================================================================================
Relational Schema Design Description:
=================================================================================
/*item is a table for each item*/
Table item (Primary Key: ItemID):
ItemID--Currently--Buy Price--First Bid--Number of Bids--UserID--Started--Ends--Description

/*categ is for item-category relation*/
Table categ (Primary Key: ItemID, Category):
ItemID--Category

/*location_item table is to store the lcation information of each item*/
Table location_item (Primary Key: ItemID):
ItemID--Longitute--Latitute--Location--Country

/*table bid is to store information of each bid.*/
Table bid (Primary Key: ItemID, UserID):
ItemID--UserID--Time--Amount

/*bider is to store the basic information of each bider*/ 
Table bider (Primary Key: UserID):
UserID--Rating--Location--Country

/*seller is to store the information of each seller*/
Table seller (Primary Key: UserID):
UserID--Rating
=================================================================================

=================================================================================
Answers for schema questions
=================================================================================
Q1:List is shown as above.
for table item keys: (ItemID),(ItemID,Currently,Buy Price,First Bid,Number of Bids,UserID,Started,Ends,Description)
for table categ keys: (ItemID, Category)
for table location_item keys:(ItemID),(ItemID,Longitute,Latitute,Location,Country)
for table bid keys: (ItemID, UserID),(ItemID,UserID,Time,Amount)
for table bider keys: (UserID)),(UserID,Rating,Location,Country)
for table seller keys: (UserID),(UserID,Rating)

Q2:
For relation(table) item: ItemID->all subsets of {Currently,Buy Price,First Bid,Number of Bids,UserID,Started,Ends,Description} except {Currently,Buy Price,First Bid,Number of Bids,UserID,Started,Ends,Description}}

For relation(table) categ:ItemID->Category

For relation(table) bid:(ItemID,UserID)->Time,(ItemID,UserID)->Amount,(ItemID,UserID)->(Time,Amount),ItemID->UserID

For relation(table) bider:UserID-> all the subsets of {Rating,Location,Country}

For relation(table) seller:UserID->Rating.

Q3：
In table location_item, I donnot use BCNF because there are some records where Longitute,Latitute,Location don't exist. So even though Longitute,Latitute can determine Location and Country and Location can determine Country, I still don't use BCNF. Also, for table bid, although ItemID can determine UserID, but one bid can only be recognized by both UserID and ItemID, So I add them in table bid.

=================================================================================

=================================================================================
