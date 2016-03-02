#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant
ant run-all

sort item.dat | uniq > item0.dat
sort seller.dat | uniq > seller0.dat
sort bider.dat | uniq > bider0.dat
sort bid.dat | uniq > bid0.dat
sort location_item.dat | uniq >location_item0.dat
sort categ.dat | uniq > categ0.dat

mysql CS144 < load.sql

rm ./*.dat
rm -rf bin
