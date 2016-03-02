CREATE TABLE item (ItemID VARCHAR(100), Name VARCHAR(100), Currently DECIMAL(8,2), Buy_price DECIMAL(8,2), First_Bid DECIMAL(8,2), Number_of_Bids INT, UserID VARCHAR(100), Started TIMESTAMP, Ends TIMESTAMP, Description VARCHAR(4000), PRIMARY KEY(ItemID));

CREATE TABLE bid (ItemID VARCHAR(100), UserID VARCHAR(100), Time TIMESTAMP, Amount DECIMAL(8,2), PRIMARY KEY(ItemID,UserID));

CREATE TABLE categ (ItemID VARCHAR(100),Category VARCHAR(100), PRIMARY KEY(ItemID,Category));

CREATE TABLE location_item (ItemID VARCHAR(100), Longitute FLOAT, Latitute FLOAT, Location VARCHAR(100),Country VARCHAR(100), PRIMARY KEY(ItemID));

CREATE TABLE bider (UserID VARCHAR(100), Rating INT, Location VARCHAR(100), Country VARCHAR(100), PRIMARY KEY(UserID));

CREATE TABLE seller (UserID VARCHAR(100), Rating INT, PRIMARY KEY(UserID));