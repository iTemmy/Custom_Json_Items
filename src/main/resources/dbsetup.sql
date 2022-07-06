CREATE TABLE IF NOT EXISTS item (
	ID INT NOT NULL AUTO_INCREMENT,
	UniqueName VARCHAR(100) NOT NULL UNIQUE,
	ItemName VARCHAR(100),
	Material VARCHAR(50) NOT NULL,
	Lore VARCHAR(500),
	Model INT,
	PRIMARY KEY (ID)
);
CREATE TABLE IF NOT EXISTS recipe (
	ID INT NOT NULL AUTO_INCREMENT,
	UniqueName VARCHAR(100) UNIQUE,
	Requires VARCHAR(100),
	Line1 VARCHAR(3),
	Line2 VARCHAR(3),
	Line3 VARCHAR(3),
	PRIMARY KEY (ID),
	FOREIGN KEY (UniqueName) REFERENCES item(UniqueName)
);
CREATE TABLE IF NOT EXISTS ingredients (
	ID INT NOT NULL AUTO_INCREMENT,
	UniqueName VARCHAR(100) UNIQUE,
	ingredientkey CHAR(1),
	ingredient VARCHAR(100),
	PRIMARY KEY (ID),
	FOREIGN KEY (UniqueName) REFERENCES item(UniqueName)
);
CREATE TABLE IF NOT EXISTS enchantments (
	ID INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL UNIQUE,
	max_level INT NOT NULL,
	PRIMARY KEY(ID)
);
CREATE TABLE IF NOT EXISTS item_enchants (
	item_id INT NOT NULL,
	enchantment_id INT NOT NULL,
	level INT NOT NULL,
	PRIMARY KEY(item_id, enchantment_id),
	FOREIGN KEY (item_id) REFERENCES item(id),
	FOREIGN KEY (enchantment_id) REFERENCES enchantments(id)
);
CREATE TABLE IF NOT EXISTS vanilla_attributes (
	ID INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(28),
	PRIMARY KEY (ID)
);
CREATE TABLE IF NOT EXISTS item_vanilla_attributes (
	item_id INT NOT NULL REFERENCES item(id),
	attribute_id INT NOT NULL REFERENCES vanilla_attributes(id),
	amount FLOAT(3) NOT NULL,
	slot VARCHAR(10) NOT NULL,
	PRIMARY KEY(item_id, attribute_id)
);
CREATE TABLE IF NOT EXISTS custom_attributes (
	ID INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50),
	PRIMARY KEY(ID)
);
CREATE TABLE IF NOT EXISTS item_custom_attributes (
	item_id INT NOT NULL REFERENCES item(id),
	attribute_id INT NOT NULL REFERENCES custom_attributes(id),
	value VARCHAR(100),
	PRIMARY KEY(item_id, attribute_id)
);
CREATE TABLE IF NOT EXISTS custom_items_db_version (
	version INT NOT NULL,
	patch INT NOT NULL
);
