#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant run-all

sort item.csv | uniq > item0.csv
sort seller.csv | uniq > seller0.csv
sort bider.csv | uniq > bider0.csv
sort bid.csv | uniq > bid0.csv
sort location_item.csv | uniq > location_item0.csv
sort categ.csv | uniq > categ0.csv

mysql CS144 < load.sql

rm *.csv
rm ./lib/*
rm ./obj/*
