This example contains a simple utility class to simplify opening database
connections in Java applications, such as the one you will write to build
your Lucene index. 

To answer the question about my index design, I used Name, Category and Description to construct my index.
Due to that one item may have multiple categories, I let Name + Category1 + Category2 + ... + Categoryn + Description to 
construct the index.
