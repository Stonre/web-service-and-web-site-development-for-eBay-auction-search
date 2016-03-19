=====================================================================================
Q1: For which communication(s) do you use the SSL encryption? If you are encrypting the communication from (1) to (2) in Figure 2, for example,
 write (1)→(2) in your answer.
A1: (4)->(5),(5)->(6)

Q2: How do you ensure that the item was purchased exactly at the Buy_Price of that particular item?
A2: As soon as a user searches an item and opens the item result page, we create a http Session to save all the item's information. So in the
following steps for checkout, we can use all the item's information without reading oak server and the whole checkout process just needs the 
client to provide his credit card number. The checking price is determined by the item's Buy_Price information saved in the HTTP session. So
in this way, we can gaurantee that the item is purchased at the Buy_Price.

In this project, we refer to a github file about how to use getServerName(), getContextPath() in the url field.
https://github.com/zoexi/eDaze/blob/master/project5/WebContents/itemResult.jsp

We use doPost instead of doGet in (5)->(6). In this way, we can gaurantee that the credit card number will not exist in the url, which can be 
more safe.
