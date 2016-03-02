use CS144;
CREATE TABLE geom(ItemID VARCHAR(100), Latitude FLOAT, Longitude FLOAT) ENGINE=MyISAM;
INSERT INTO geom(ItemID,Latitude,Longitude) SELECT ItemID,Latitute,Longitute FROM location_item WHERE Latitute IS NOT NULL AND Longitute IS NOT NULL;
ALTER TABLE geom ADD pos POINT NOT NULL;
UPDATE geom SET pos=PointFromText(CONCAT('POINT(',geom.Latitude,' ',geom.Longitude,')'));
CREATE SPATIAL INDEX sp_index ON geom(pos);
ALTER TABLE geom DROP COLUMN Longitude;
ALTER TABLE geom DROP COLUMN Latitude;
