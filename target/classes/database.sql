-- SCRIPT

CREATE TABLE scripts(
	scriptId int PRIMARY KEY AUTO_INCREMENT,
	name varchar(255)
);

CREATE TABLE scriptProg(
	scriptId int FOREIGN KEY REFERENCES scripts(scriptId),
	progId int FOREIGN KEY REFERENCES programs(progId)
);

-- PROGRAM

CREATE TABLE programs(
	progId int PRIMARY KEY AUTO_INCREMENT,
	name varchar(255)
);

CREATE TABLE progAttr(
	progId int FOREIGN KEY REFERENCES programs(progId),
	attrId int FOREIGN KEY REFERENCES attributes(attrId)
);

-- ATTRIBUTE

CREATE TABLE attributes(
	attributeId int PRIMARY KEY AUTO_INCREMENT,
	name varchar(255),
	attrTypeId int FOREIGN KEY REFERENCES attributeTypes(attrTypeId),
	serviceId int FOREIGN KEY REFERENCES services(serviceId),
	mandatory boolean
);

CREATE TABLE attributeTypes(
	attrTypeId int PRIMARY KEY AUTO_INCREMENT,
	name varchar(10)
);

CREATE TABLE scriptAttr(
	scriptId int FOREIGN KEY REFERENCES scripts(scriptId),
	attrId int FOREIGN KEY REFERENCES attributes(attrId),
	value varchar(255)
);

CREATE TABLE services(
	serviceId int PRIMARY KEY AUTO_INCREMENT,
	name varchar(10)
);