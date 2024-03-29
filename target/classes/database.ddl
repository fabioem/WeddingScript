-- SCRIPT
CREATE TABLE scripts(
	scriptId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	date date,
	comment varchar(255) NOT NULL,
	lastEdited datetime NOT NULL,
	created datetime NOT NULL
);

CREATE TABLE scriptProg(
	scriptId int NOT NULL,
	progId int NOT NULL,
	time int,
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId) ON DELETE CASCADE,
	FOREIGN KEY (progId) REFERENCES programs(progId) ON DELETE CASCADE
);

CREATE TABLE scriptProgAttr(
	scriptId int NOT NULL,
	progId int NOT NULL,
	attrId int NOT NULL,
	value varchar(255),
	FOREIGN KEY (scriptId) REFERENCES scripts(scriptId) ON DELETE CASCADE,
	FOREIGN KEY (progId) REFERENCES programs(progId) ON DELETE CASCADE
);

-- PROGRAM
CREATE TABLE programs(
	progId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	defaultTime int,
	defaultProgram boolean
);

CREATE TABLE progAttr(
	progId int NOT NULL,
	attrId int NOT NULL,
	value varchar(255),
	FOREIGN KEY (progId) REFERENCES programs(progId) ON DELETE CASCADE,
	FOREIGN KEY (attrId) REFERENCES attributes(attrId) ON DELETE CASCADE
);

-- ATTRIBUTE
CREATE TABLE attributes(
	attributeId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	defaultValue varchar(255),
	attrTypeId int NOT NULL,
	mandatory boolean,
	FOREIGN KEY (attrTypeId) REFERENCES attributeTypes(attrTypeId) ON DELETE CASCADE
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

-- SERVICE
CREATE TABLE services(
	serviceId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(10) NOT NULL UNIQUE
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

CREATE TABLE serviceAttr(
	serviceId int NOT NULL,
	attrId int NOT NULL,
	value varchar(255),
	FOREIGN KEY (serviceId) REFERENCES services(serviceId) ON DELETE CASCADE,
	FOREIGN KEY (attrId) REFERENCES attributes(attrId) ON DELETE CASCADE
);

-- Insert demo values
INSERT INTO programs VALUES(0, "Vacsora", 68400000, 1);
INSERT INTO programs VALUES(1, "Nyitótánc", 75600000, 1);
INSERT INTO programs VALUES(2, "Újasszony tánc", 86400000, 1);
INSERT INTO programs VALUES(3, "Torta", 79200000, 1); -- 22:00
INSERT INTO programs VALUES(4, "Vendégvárás", 64800000, 1); -- 18:00
INSERT INTO programs VALUES(5, "Gyertyafénykeringő", 84600000, 1); -- 23:30
INSERT INTO programs VALUES(6, "Ifjú pár érkezik", 66600000, 1); -- 18:30
INSERT INTO scripts VALUES(0, "Judit és Dani", '2021-07-01 00:00:00.000', "kalocsai mintás stílus", (DATETIME('now', 'localtime')), (DATETIME('now', 'localtime')));
INSERT INTO scripts VALUES(1, "Evelin és László", '2021-08-02 00:00:00.000', "rock lagzi", (DATETIME('now', 'localtime')), (DATETIME('now', 'localtime')));
INSERT INTO services VALUES(0, "CeremóniaMester");
INSERT INTO services VALUES(1, "LED-es oldalfal világítás");
INSERT INTO services VALUES(2, "Ceremónia hangosítás");
INSERT INTO services VALUES(3, "Felhőtánc");
INSERT INTO services VALUES(4, "CeremóniaMester külső helyszínen");
INSERT INTO services VALUES(5, "Extra hang- és fénytechnika");
--INSERT INTO attributeTypes VALUES(2, "Basic");
INSERT INTO attributeTypes VALUES(0, "Script");
INSERT INTO attributeTypes VALUES(1, "Program");
INSERT INTO attributeTypes VALUES(2, "Service");
INSERT INTO scriptProg VALUES(0, 1, 75900000); --21:05 Nyitótánc
INSERT INTO scriptProg VALUES(0, 2, 86400000); --24:00 Újasszony tánc
INSERT INTO attributes VALUES(0, "Zene", "", 1, 1);
INSERT INTO attributes VALUES(1, "Beszéd", "vőlegény", 1, 1);
INSERT INTO attributes VALUES(2, "Himnusz", "nem", 1, 1);
INSERT INTO attributes VALUES(3, "Szolgáltatói asztal", "igen", 0, 0);
INSERT INTO attributes VALUES(4, "Oldalfal LED-ek színe", "", 2, 0);
INSERT INTO attributes VALUES(5, "Bevonuló zene", "", 1, 0);
INSERT INTO attributes VALUES(6, "Molinónkat kitehetjük", "igen", 0, 0);
INSERT INTO attributes VALUES(7, "Lakodalmas zene szólhat", "igen", 0, 0);
INSERT INTO attributes VALUES(8, "Tánc jellege", "", 1, 1);
INSERT INTO progAttr VALUES(2, 0, "Hagyományos lakodalmas"); -- újasszonytánchoz zene
INSERT INTO progAttr VALUES(1, 0, ""); -- nyitótánchoz zene
INSERT INTO progAttr VALUES(1, 8, "keringő / keringő, majd gyors / vicces"); -- nyitótánc jellege
INSERT INTO progAttr VALUES(0, 0, "Váradi Roma Café + Jazz-Lounge világsláger feldolgozások"); -- vacsorához zene
INSERT INTO progAttr VALUES(0, 1, "Vőlegény"); -- vacsorához beszéd
INSERT INTO progAttr VALUES(0, 2, "Nem"); -- vacsorához himnusz
INSERT INTO progAttr VALUES(6, 0, "Lakodalom van a..."); -- bevonuló zene
INSERT INTO scriptAttr VALUES(0, 3, "Külön szolgáltatói asztal"); -- szolgáltatói asztal
INSERT INTO serviceAttr VALUES(2, 4, "halvány rózsaszín"); -- oldalfal szolg. színe