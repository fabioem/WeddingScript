-- SCRIPT

CREATE TABLE scripts(
	scriptId int PRIMARY KEY,
	name varchar(255)
);

CREATE TABLE scriptProg(
	scriptId int,
	progId int,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId),
	FOREIGN KEY (progId) REFERENCES programs(progId)
);

-- PROGRAM

CREATE TABLE programs(
	progId int PRIMARY KEY,
	name varchar(255)
);

CREATE TABLE progAttr(
	progId int,
	attrId int,
	FOREIGN KEY (progId) REFERENCES programs(progId),
	FOREIGN KEY (attrId) REFERENCES attributes(attrId)
);

-- ATTRIBUTE

CREATE TABLE attributes(
	attributeId int PRIMARY KEY,
	name varchar(255),
	value varchar(255),
	attrTypeId int,
	serviceId int,
	mandatory boolean,
	always boolean,
	FOREIGN KEY (attrTypeId) REFERENCES attributeTypes(attrTypeId),
	FOREIGN KEY (serviceId) REFERENCES services(serviceId)
);

CREATE TABLE attributeTypes(
	attrTypeId int PRIMARY KEY,
	name varchar(10)
);

CREATE TABLE scriptAttr(
	scriptId int,
	attrId int,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId),
	FOREIGN KEY (attrId) REFERENCES attributes(attrId)
);

CREATE TABLE services(
	serviceId int PRIMARY KEY,
	name varchar(10)
);

-- INSERT

INSERT INTO programs VALUES(0, "Vacsora");
INSERT INTO scripts VALUES(0, "Teszt");
INSERT INTO services VALUES(0, "Műsorvezetés");
INSERT INTO attributeTypes VALUES(0, "Program");