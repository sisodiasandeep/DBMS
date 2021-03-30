# Identify the columns require indexing in order, product, category tables and create indexes.

# orders
ALTER TABLE orders
ADD INDEX OrderTime (OrderTime);

# product
ALTER TABLE product
ADD INDEX Title (Title);

ALTER TABLE product
ADD INDEX Price (Price);

ALTER TABLE product
ADD INDEX TimeAdded (TimeAdded);

ALTER TABLE product
ADD INDEX Status (Status);