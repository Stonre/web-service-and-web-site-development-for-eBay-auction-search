SELECT COUNT(*) FROM (SELECT UserID FROM bider UNION SELECT UserID FROM seller) as a1;
SELECT COUNT(*) FROM location_item WHERE binary Location LIKE 'New York';
SELECT COUNT(*) FROM (SELECT ItemID, COUNT(*) AS cn FROM categ GROUP BY categ.ItemID HAVING COUNT(*)=4) as cont;
SELECT bid.ItemID FROM bid inner join item on item.ItemID=bid.ItemID WHERE item.Ends>"2001-12-20 00:00:01" AND bid.Amount=(SELECT MAX(bid.Amount) From bid inner join item on item.ItemID=bid.ItemID);
SELECT COUNT(*) FROM seller WHERE Rating > 1000;
SELECT COUNT(*) FROM seller INNER JOIN bider on seller.UserID=bider.UserID;
SELECT COUNT(*) FROM (SELECT DISTINCT Category FROM ((SELECT bid.ItemID,bid.Amount FROM bid WHERE bid.Amount>100) AS t1 INNER JOIN categ on t1.ItemID=categ.ItemID)) as re;
