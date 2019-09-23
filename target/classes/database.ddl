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
	time int,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId) ON DELETE CASCADE,
	FOREIGN KEY (progId) REFERENCES programs(progId) ON DELETE CASCADE
);

-- PROGRAM
CREATE TABLE programs(
	progId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	defaultTime int
);

CREATE TABLE progAttr(
	progId int NOT NULL,
	attrId int NOT NULL,
	defaultValue varchar(255) NOT NULL,
	FOREIGN KEY (progId) REFERENCES programs(progId) ON DELETE CASCADE,
	FOREIGN KEY (attrId) REFERENCES attributes(attrId) ON DELETE CASCADE
);

-- ATTRIBUTE
CREATE TABLE attributes(
	attributeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	defaultValue varchar(255),
	attrTypeId int NOT NULL,
	serviceId int,
	mandatory boolean,
	FOREIGN KEY (attrTypeId) REFERENCES attributeTypes(attrTypeId) ON DELETE CASCADE,
	FOREIGN KEY (serviceId) REFERENCES services(serviceId) ON DELETE CASCADE
);

CREATE TABLE attributeTypes(
	attrTypeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(10) UNIQUE NOT NULL
);

CREATE TABLE scriptAttr(
	scriptId int NOT NULL,
	attrId int NOT NULL,
	value varchar(255),
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId) ON DELETE CASCADE,
	FOREIGN KEY (attrId) REFERENCES attributes(attrId) ON DELETE CASCADE
);

CREATE TABLE attrAttr(
	mainAttrId int NOT NULL,
	subAttrId int NOT NULL,
	FOREIGN KEY (mainAttrId) REFERENCES attributes(attrId) ON DELETE CASCADE
	FOREIGN KEY (subAttrId) REFERENCES attributes(attrId) ON DELETE CASCADE
);

-- SERVICE
CREATE TABLE services(
	serviceId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(10) NOT NULL
);

CREATE TABLE serviceProg(
	serviceId int NOT NULL,
	progId int NOT NULL,
	FOREIGN KEY (serviceId) REFERENCES services(serviceId) ON DELETE CASCADE,
	FOREIGN KEY (progId) REFERENCES programs(progId) ON DELETE CASCADE
);

CREATE TABLE scriptService(
	scriptId int NOT NULL,
	serviceId int NOT NULL,
	FOREIGN KEY (serviceId) REFERENCES services(serviceId) ON DELETE CASCADE,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId) ON DELETE CASCADE
);

-- CREATE TABLE serviceAttr(
	-- serviceId int NOT NULL,
	-- attrId int NOT NULL,
	-- defaultValue varchar(255),
	-- FOREIGN KEY (serviceId) REFERENCES services(serviceId) ON DELETE CASCADE,
	-- FOREIGN KEY (attrId) REFERENCES attributes(attrId) ON DELETE CASCADE
-- );

-- Insert demo values
INSERT INTO programs VALUES(0, "Vacsora", 68400000);
INSERT INTO programs VALUES(1, "Nyitótánc", 75600000);
INSERT INTO programs VALUES(2, "Újasszony tánc", 86400000);
INSERT INTO scripts VALUES(0, "Judit és Dani", '2019-01-01 00:00:00.000', "Komment1", (DATETIME('now')), (DATETIME('now')));
INSERT INTO scripts VALUES(1, "Juci és Béla", '2019-01-02 00:00:00.000', "Komi 2", (DATETIME('now')), (DATETIME('now')));
INSERT INTO services VALUES(0, "Alap");
INSERT INTO services VALUES(1, "Műsorvezetés");
INSERT INTO services VALUES(2, "LED-es oldalfal világítás");
INSERT INTO services VALUES(3, "Ceremónia hangosítás kis cs.");
INSERT INTO services VALUES(4, "Ceremónia hangosítás nagy cs.");
INSERT INTO services VALUES(5, "Felhőtánc");
INSERT INTO attributeTypes VALUES(0, "Basic");
INSERT INTO attributeTypes VALUES(1, "Script");
INSERT INTO attributeTypes VALUES(2, "Program");
INSERT INTO scriptProg VALUES(0, 1, 75800000);
INSERT INTO scriptProg VALUES(0, 2, 86400000);
INSERT INTO attributes VALUES(0, "Zene", "", 2, null, 1);
INSERT INTO attributes VALUES(1, "Beszéd", "vőlegény", 2, null, 1);
INSERT INTO attributes VALUES(2, "Himnusz", "nem", 2, null, 1);
INSERT INTO attributes VALUES(3, "Szolgáltatói asztal", "igen", 0, null, 0);
INSERT INTO progAttr VALUES(1, 0, ""); /* nyitótánchoz zene */
INSERT INTO progAttr VALUES(0, 0, "Váradi Roma Café + Jazz-Lounge világsláger feldolgozások"); /* vacsorához zene */
INSERT INTO progAttr VALUES(0, 1, "Vőlegény"); /* vacsorához beszéd */
INSERT INTO progAttr VALUES(0, 2, "Nem"); /* vacsorához himnusz */
INSERT INTO scriptAttr VALUES(0, 3, "Külön teremben");