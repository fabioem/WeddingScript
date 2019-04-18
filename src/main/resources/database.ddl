-- SCRIPT

CREATE TABLE scripts(
	scriptId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	date date,
	comment varchar(255) NOT NULL,
	lastEdited datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
	
);

CREATE TABLE scriptProg(
	scriptId int NOT NULL,
	progId int NOT NULL,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId),
	FOREIGN KEY (progId) REFERENCES programs(progId)
);

-- PROGRAM

CREATE TABLE programs(
	progId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	defaultTime INTEGER
);

CREATE TABLE progAttr(
	progId int NOT NULL,
	attrId int NOT NULL,
	FOREIGN KEY (progId) REFERENCES programs(progId),
	FOREIGN KEY (attrId) REFERENCES attributes(attrId)
);

-- ATTRIBUTE

CREATE TABLE attributes(
	attributeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	value varchar(255) NOT NULL,
	attrTypeId int NOT NULL,
	serviceId int,
	mandatory boolean,
	FOREIGN KEY (attrTypeId) REFERENCES attributeTypes(attrTypeId),
	FOREIGN KEY (serviceId) REFERENCES services(serviceId)
);

CREATE TABLE attributeTypes(
	attrTypeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(10) NOT NULL
);

CREATE TABLE scriptAttr(
	scriptId int NOT NULL,
	attrId int NOT NULL,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId),
	FOREIGN KEY (attrId) REFERENCES attributes(attrId)
);

CREATE TABLE services(
	serviceId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(10) NOT NULL
);

-- INSERT

INSERT INTO programs VALUES(0, "Vacsora", 360000);
INSERT INTO programs VALUES(1, "Nyitótánc", 2160000);
INSERT INTO programs VALUES(2, "Újasszony tánc", 0);
INSERT INTO scripts VALUES(0, "Judit és Dani", '2019-01-01 00:00:00.000', "Komment1", (DATETIME('now')), (DATETIME('now')));
INSERT INTO scripts VALUES(1, "Juci és Béla", '2019-01-02 00:00:00.000', "Komi 2", (DATETIME('now')), (DATETIME('now')));
INSERT INTO services VALUES(0, "Műsorvezetés");
INSERT INTO services VALUES(1, "Oldalfal fényezés");
INSERT INTO attributeTypes VALUES(0, "Program");
INSERT INTO attributeTypes VALUES(1, "Script");