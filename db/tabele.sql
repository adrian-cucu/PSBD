set serveroutput on;
set termout on;

--------------------------------------------------------
CREATE TABLE elev (
	id_elev 		NUMBER(10) NOT NULL, 
	nume 			VARCHAR2(50) NOT NULL,
	prenume 		VARCHAR2(50) NOT NULL,
	adresa 			VARCHAR2(256) NOT NULL,
	cnp				VARCHAR2(13) NOT NULL,
	etnie 			VARCHAR2(30) NOT NULL,
	nationalitate 	VARCHAR2(30) NOT NULL
);

ALTER TABLE elev 
ADD CONSTRAINT elev_pk 
PRIMARY KEY (id_elev);

CREATE SEQUENCE elev_seq;

CREATE OR REPLACE TRIGGER elev_on_insert
	BEFORE INSERT ON elev
	FOR EACH ROW 
BEGIN	
	SELECT elev_seq.nextval
	INTO :new.id_elev
	FROM dual;
END;
/
--------------------------------------------------------
CREATE TABLE profil (
	id_profil		NUMBER(10) NOT NULL,
	nume_profil		VARCHAR(50) NOT NULL
);

ALTER TABLE profil 
ADD CONSTRAINT profil_pk 
PRIMARY KEY (id_profil);

CREATE SEQUENCE profil_seq;

CREATE OR REPLACE TRIGGER profil_on_insert
	BEFORE INSERT ON profil
	FOR EACH ROW 
BEGIN	
	SELECT profil_seq.nextval
	INTO :new.id_profil
	FROM dual;
END;
/
--------------------------------------------------------
CREATE TABLE clasa (
	id_clasa 		NUMBER(10) NOT NULL,
	id_profil		NUMBER(10) NOT NULL,
	an_scolar		NUMBER(5)  NOT NULL,
	cod				VARCHAR(5) NOT NULL,
	clasa			NUMBER(3)  NOT NULL
);

ALTER TABLE clasa 
ADD CONSTRAINT clasa_pk 
PRIMARY KEY (id_clasa);

ALTER TABLE clasa 
ADD CONSTRAINT clsa_fk_profil 

CREATE SEQUENCE clasa_seq;

CREATE OR REPLACE TRIGGER clasa_on_insert
	BEFORE INSERT ON clasa
	FOR EACH ROW 
BEGIN	
	SELECT clasa_seq.nextval
	INTO :new.id_clasa
	FROM dual;
END;
/
--------------------------------------------------------
CREATE TABLE elev_clasa (
	id_elev			NUMBER(10) NOT NULL,
	id_clasa		NUMBER(10) NOT NULL
);

ALTER TABLE elev_clasa 
ADD CONSTRAINT elev_clasa_fk_elev 
FOREIGN KEY (id_elev) REFERENCES elev(id_elev);

ALTER TABLE elev_clasa
ADD CONSTRAINT elev_clasa_fk_clasa 
FOREIGN KEY (id_clasa) REFERENCES clasa(id_clasa);
--------------------------------------------------------
CREATE TABLE materie (
	id_materie		NUMBER(10) NOT NULL,
	nume_materie	VARCHAR(50) NOT NULL
);

ALTER TABLE materie
ADD CONSTRAINT materie_pk 
PRIMARY KEY (id_materie);
---------------------------------------------------------------
CREATE TABLE profil_materie (
	id_profil		NUMBER(10) NOT NULL,
	id_materie		NUMBER(10) NOT NULL,
	an_clasa		NUMBER(3) NOT NULL  -- anul in care se face materia respectiva
);

ALTER TABLE profil_materie
ADD CONSTRAINT profil_materie_fk_profil 
FOREIGN KEY (id_profil) REFERENCES profil(id_profil);

ALTER TABLE profil_materie
ADD CONSTRAINT profil_materie_fk_materie 
FOREIGN KEY (id_materie) REFERENCES materie(id_materie);
---------------------------------------------------------------
CREATE TABLE medie (
	id_elev 		NUMBER(10) NOT NULL,	
	medie			NUMBER(2)  NOT NULL,
	an_scolar		NUMBER(2)  NOT NULL,
	semestru		NUMBER(2)  NOT NULL
);

ALTER TABLE medie
ADD CONSTRAINT medie_fk_elev
FOREIGN KEY (id_elev) REFERENCES elev (id_elev);
---------------------------------------------------------------
CREATE TABLE bursa (
	id_bursa		NUMBER(10) NOT NULL,
	tip_bursa		VARCHAR(30) NOT NULL,
	valoare			NUMBER(5) NOT NULL	
);

ALTER TABLE bursa
ADD CONSTRAINT bursa_pk 
PRIMARY KEY (id_bursa);


CREATE TABLE elev_bursa (
	id_elev			NUMBER(10) NOT NULL,
	id_bursa		NUMBER(10) NOT NULL,
	an				NUMBER(10) NOT NULL
);
 
ALTER TABLE elev_bursa 
ADD CONSTRAINT elev_bursa_fk_elev
FOREIGN KEY (id_elev) REFERENCES elev (id_elev);

ALTER TABLE elev_bursa 
ADD CONSTRAINT elev_bursa_fk_bursa 
FOREIGN KEY (id_bursa) REFERENCES bursa (id_bursa);

-----------------------------------------------------------------------------------
INSERT INTO bursa (id_bursa, tip_bursa, valoare) VALUES ('1', 'performanta', '1000');
INSERT INTO bursa (id_bursa, tip_bursa, valoare) VALUES ('2', 'merit', '800');
INSERT INTO bursa (id_bursa, tip_bursa, valoare) VALUES ('3', 'ajutor social', '600');
-----------------------------------------------------------------------------------


-----------------------------------------------------------------------------------
INSERT INTO  profil (nume_profil) VALUES ('Matematica-Informatica');
INSERT INTO  profil (nume_profil) VALUES ('Matematica-Informatica intensiv informatica');
INSERT INTO  profil (nume_profil) VALUES ('Filologie');
-----------------------------------------------------------------------------------


-----------------------------------------------------------------------------------
INSERT INTO materie (id_materie, nume_materie) VALUES (1, 'Limba si literatura romana');
INSERT INTO materie (id_materie, nume_materie) VALUES (2, 'Matematica');
INSERT INTO materie (id_materie, nume_materie) VALUES (3, 'Informatica');
INSERT INTO materie (id_materie, nume_materie) VALUES (4, 'Filozofie');
INSERT INTO materie (id_materie, nume_materie) VALUES (5, 'Psihologie');
INSERT INTO materie (id_materie, nume_materie) VALUES (6, 'Istorie');
INSERT INTO materie (id_materie, nume_materie) VALUES (7, 'Sport');
INSERT INTO materie (id_materie, nume_materie) VALUES (8, 'Limba engleza');
INSERT INTO materie (id_materie, nume_materie) VALUES (9, 'Limba franceza');
INSERT INTO materie (id_materie, nume_materie) VALUES (10, 'Religie');
INSERT INTO materie (id_materie, nume_materie) VALUES (11, 'Tehnologia informatiei');
INSERT INTO materie (id_materie, nume_materie) VALUES (12, 'Fizica');
INSERT INTO materie (id_materie, nume_materie) VALUES (13, 'Stiinte politice');
------------------------------------------------------------------------------------


------------------------------------------------------------------------------------
INSERT INTO profil_materie VALUES (1, 1, 9);
INSERT INTO profil_materie VALUES (1, 1, 10);
INSERT INTO profil_materie VALUES (1, 1, 11);
INSERT INTO profil_materie VALUES (1, 1, 12);

INSERT INTO profil_materie VALUES (1, 2, 9);
INSERT INTO profil_materie VALUES (1, 2, 10);
INSERT INTO profil_materie VALUES (1, 2, 11);
INSERT INTO profil_materie VALUES (1, 2, 12);

INSERT INTO profil_materie VALUES (1, 3, 9);
INSERT INTO profil_materie VALUES (1, 3, 10);
INSERT INTO profil_materie VALUES (1, 3, 11);
INSERT INTO profil_materie VALUES (1, 3, 12);

INSERT INTO profil_materie VALUES (1, 7, 9);
INSERT INTO profil_materie VALUES (1, 7, 10);
INSERT INTO profil_materie VALUES (1, 7, 11);
INSERT INTO profil_materie VALUES (1, 7, 12);

INSERT INTO profil_materie VALUES (1, 8, 9);
INSERT INTO profil_materie VALUES (1, 8, 10);
INSERT INTO profil_materie VALUES (1, 8, 11);
INSERT INTO profil_materie VALUES (1, 8, 12);

INSERT INTO profil_materie VALUES (1, 9, 9);
INSERT INTO profil_materie VALUES (1, 9, 10);
INSERT INTO profil_materie VALUES (1, 9, 11);
INSERT INTO profil_materie VALUES (1, 9, 12);

INSERT INTO profil_materie VALUES (1, 6, 9);
INSERT INTO profil_materie VALUES (1, 6, 10);
INSERT INTO profil_materie VALUES (1, 6, 11);
INSERT INTO profil_materie VALUES (1, 6, 12);

INSERT INTO profil_materie VALUES (1, 5, 10);

INSERT INTO profil_materie VALUES (1, 4, 12);

INSERT INTO profil_materie VALUES (1, 10, 9);
INSERT INTO profil_materie VALUES (1, 10, 10);
INSERT INTO profil_materie VALUES (1, 10, 11);
INSERT INTO profil_materie VALUES (1, 10, 12);

INSERT INTO profil_materie VALUES (1, 11, 9);
INSERT INTO profil_materie VALUES (1, 11, 10);
INSERT INTO profil_materie VALUES (1, 11, 11);
INSERT INTO profil_materie VALUES (1, 11, 12);

INSERT INTO profil_materie VALUES (1, 12, 9);
INSERT INTO profil_materie VALUES (1, 12, 10);
INSERT INTO profil_materie VALUES (1, 12, 11);
INSERT INTO profil_materie VALUES (1, 12, 12);


INSERT INTO profil_materie VALUES (2, 1, 9);
INSERT INTO profil_materie VALUES (2, 1, 10);
INSERT INTO profil_materie VALUES (2, 1, 11);
INSERT INTO profil_materie VALUES (2, 1, 12);

INSERT INTO profil_materie VALUES (2, 2, 9);
INSERT INTO profil_materie VALUES (2, 2, 10);
INSERT INTO profil_materie VALUES (2, 2, 11);
INSERT INTO profil_materie VALUES (2, 2, 12);

INSERT INTO profil_materie VALUES (2, 3, 9);
INSERT INTO profil_materie VALUES (2, 3, 10);
INSERT INTO profil_materie VALUES (2, 3, 11);
INSERT INTO profil_materie VALUES (2, 3, 12);

INSERT INTO profil_materie VALUES (2, 7, 9);
INSERT INTO profil_materie VALUES (2, 7, 10);
INSERT INTO profil_materie VALUES (2, 7, 11);
INSERT INTO profil_materie VALUES (2, 7, 12);

INSERT INTO profil_materie VALUES (2, 8, 9);

INSERT INTO profil_materie VALUES (2, 8, 10);
INSERT INTO profil_materie VALUES (2, 8, 11);
INSERT INTO profil_materie VALUES (2, 8, 12);

INSERT INTO profil_materie VALUES (2, 9, 9);
INSERT INTO profil_materie VALUES (2, 9, 10);
INSERT INTO profil_materie VALUES (2, 9, 11);
INSERT INTO profil_materie VALUES (2, 9, 12);

INSERT INTO profil_materie VALUES (2, 6, 9);
INSERT INTO profil_materie VALUES (2, 6, 10);
INSERT INTO profil_materie VALUES (2, 6, 11);
INSERT INTO profil_materie VALUES (2, 6, 12);

INSERT INTO profil_materie VALUES (2, 5, 10);

INSERT INTO profil_materie VALUES (2, 4, 12);

INSERT INTO profil_materie VALUES (2, 10, 9);
INSERT INTO profil_materie VALUES (2, 10, 10);
INSERT INTO profil_materie VALUES (2, 10, 11);
INSERT INTO profil_materie VALUES (2, 10, 12);

INSERT INTO profil_materie VALUES (2, 11, 9);
INSERT INTO profil_materie VALUES (2, 11, 10);

INSERT INTO profil_materie VALUES (2, 12, 9);
INSERT INTO profil_materie VALUES (2, 12, 10);
INSERT INTO profil_materie VALUES (2, 12, 11);
INSERT INTO profil_materie VALUES (2, 12, 12);


INSERT INTO profil_materie VALUES (3, 1, 9);
INSERT INTO profil_materie VALUES (3, 1, 10);
INSERT INTO profil_materie VALUES (3, 1, 11);
INSERT INTO profil_materie VALUES (3, 1, 12);

INSERT INTO profil_materie VALUES (3, 2, 9);
INSERT INTO profil_materie VALUES (3, 2, 10);

INSERT INTO profil_materie VALUES (3, 3, 9);

INSERT INTO profil_materie VALUES (3, 7, 9);
INSERT INTO profil_materie VALUES (3, 7, 10);
INSERT INTO profil_materie VALUES (3, 7, 11);
INSERT INTO profil_materie VALUES (3, 7, 12);

INSERT INTO profil_materie VALUES (3, 8, 9);
INSERT INTO profil_materie VALUES (3, 8, 10);
INSERT INTO profil_materie VALUES (3, 8, 11);
INSERT INTO profil_materie VALUES (3, 8, 12);

INSERT INTO profil_materie VALUES (3, 9, 9);
INSERT INTO profil_materie VALUES (3, 9, 10);
INSERT INTO profil_materie VALUES (3, 9, 11);
INSERT INTO profil_materie VALUES (3, 9, 12);

INSERT INTO profil_materie VALUES (3, 6, 9);
INSERT INTO profil_materie VALUES (3, 6, 10);
INSERT INTO profil_materie VALUES (3, 6, 11);
INSERT INTO profil_materie VALUES (3, 6, 12);

INSERT INTO profil_materie VALUES (3, 5, 9);
INSERT INTO profil_materie VALUES (3, 5, 10);
INSERT INTO profil_materie VALUES (3, 5, 11);
INSERT INTO profil_materie VALUES (3, 5, 12);

INSERT INTO profil_materie VALUES (3, 4, 11);
INSERT INTO profil_materie VALUES (3, 4, 12);

INSERT INTO profil_materie VALUES (3, 10, 9);
INSERT INTO profil_materie VALUES (3, 10, 10);
INSERT INTO profil_materie VALUES (3, 10, 11);
INSERT INTO profil_materie VALUES (3, 10, 12);

INSERT INTO profil_materie VALUES (3, 12, 9);

INSERT INTO profil_materie VALUES (3, 13, 9);
INSERT INTO profil_materie VALUES (3, 13, 10);
INSERT INTO profil_materie VALUES (3, 13, 11);
INSERT INTO profil_materie VALUES (3, 13, 12);

/*
SELECT p.*, m.nume_materie, pm.an_clasa 
FROM profil p
INNER JOIN profil_materie pm ON p.id_profil = pm.id_profil
INNER JOIN materie m ON pm.id_materie = m.id_materie
ORDER BY pm.an_clasa;
*/
-----------------------------------------------------------------------------------


-----------------------------------------------------------------------------------
INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Cucu', 'Adrian', 'Str. Lalelelor nr. 1 bl. AK47, et -1', '1960212170071', 'romana','romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Timofte', 'Marinel', 'Str. Dealului nr. 69 sat malu', '1900112170071', 'romana', 'romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate)  
VALUES ('Poteras', 'Bianca', 'Str. Dilimanilor nr. 3 Fantanele', '2960513170030', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Popovici', 'Paula', 'Str. Tiglina 1. Galati', '2970429170029', 'romana', 'romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate)  
VALUES ('Pecheanu', 'Margareta Bianca', 'Str. Voievozinor nr. 69', '2960308170055', 'romana', 'romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Ungureanu', 'Irinel', 'Str. Strazilor nr. 111 Hunedoara', '1980319170072', 'romana', 'romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Iftode', 'Andrei', 'Str. Principala nr. 1 Smardan Galati', '1970110170071', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Crihana', 'Cosmin', 'Str. Erorilor nr. 4 Galati', '1960522170031', 'romana', 'romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate)  
VALUES ('Constantin', 'Catalin', 'Str. Ororilor nr. 78 sat Costi Galati', '1961129170021', 'romana', 'romana');	

INSERT INTO elev(nume, prenume, adresa, cnp, etnie, nationalitate)  
VALUES ('Muratu', 'Viorica Daniela', 'Str. Aoleu nr. 999999', '2950421170042', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Albu', 'Mihai', 'Str. Alba-Ca-Zapada nr. 12 Galati', '1961204170031', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Aurica', 'Florin', 'Str. nr. 2 Liesti', '1960601170075', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Leni', 'Petru', 'Str. Cahul Moldova', '1960414000', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Cernov', 'Constantin', 'Str. Cahul Moldova', '1960522001', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Cernobal', 'Tortosi', 'Str. Zorillor ce au apus', '1960112170', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Odobasian', 'Romeo', 'Str. Vitei de vie nr. 1 Moldova', '1950213298', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Camenschi', 'Stela', 'Str. Dinamitei nr. 12 Moldova',   '2980134569', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Leni', 'Felicia', 'Str. nr. 1 Cahul Moldova', '2960112170', 'moldova', 'moldova');

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Leni', 'Lucia', 'Str. nr. 1 Cahul Moldova', '2990707456', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Garajiu', 'Cruela', 'Str. nr. 1 Cahul Moldova', '2971122176', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)
VALUES ('Zubucu', 'Janea', 'Str. nr. 44 Nisiporeni', '2941130000', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Suca', 'Bleat', 'Str. Mihai Viteazu nr. 3 Nisiporeni Moldova', '2960212888', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Salamandra', 'Catalin', 'Str. nr. 2 Zarafet Moldova', '1960214888', 'moldova', 'moldova');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Maftei', 'Radu', 'Str. Carbidului Galati', '1960515170045', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Maglavait', 'Catalin', 'Str. Exploziei nr. 4 Galati', '197060617967', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Zaiafet', 'Dumitru', 'Str. Super-Super Galati', '1960515170045', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)  
VALUES ('Vlase', 'Bogdan', 'Str. Victor Vilcovici nr. 44 Galati', '1970515170045', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate)  
VALUES ('Ghisman', 'Irinel', 'Str. Saturn  nr. 13 Galati', '1960719170000', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Vivianu', 'Marius', 'Str. Schela Galati', '1960123170099', 'romana', 'romana');	

INSERT INTO elev (nume, prenume, adresa, cnp, etnie, nationalitate) 
VALUES ('Vianu', 'Tudor', 'Str. Furnalului Galati', '1960910170044', 'romana', 'romana');	

-----------------------------------------------------------------------------------


-----------------------------------------------------------------------------------
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (1, 1, 2015, 'A', 9);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (2, 2, 2015, 'B', 9);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (3, 3, 2015, 'C', 9);

INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (4, 1, 2015, 'A', 10);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (5, 2, 2015, 'B', 10);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (6, 3, 2015, 'C', 10);

INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (7, 1, 2015, 'A', 11);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (8, 2, 2015, 'B', 11);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (9, 3, 2015, 'C', 11);

INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (10, 1, 2015, 'A', 12);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (11, 2, 2015, 'B', 12);
INSERT INTO clasa (id_clasa, id_profil, an_scolar, cod, clasa) VALUES (12, 3, 2015, 'C', 12);
-----------------------------------------------------------------------------------


-----------------------------------------------------------------------------------
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('1', '1');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('2', '1');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('3', '1');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('4', '1');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('5', '1');

INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('6', '2');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('7', '2');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('8', '2');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('9', '2');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('10', '2');

INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('11', '3');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('12', '3');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('13', '3');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('14', '3');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('15', '3');

INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('16', '4');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('17', '4');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('18', '4');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('19', '4');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('20', '4');

INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('21', '5');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('22', '5');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('23', '5');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('24', '5');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('25', '5');

INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('26', '6');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('27', '6');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('28', '6');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('29', '6');
INSERT INTO elev_clasa (id_elev, id_clasa) VALUES ('30', '6');
-----------------------------------------------------------------------------------

COLUMN id format 99999;
COLUMN nume format a15;
COLUMN prenume format a20;
COLUMN adresa format a60;
COLUMN cnp format a13;
COLUMN etnie format a15;
COLUMN nationalitate format a15;

SET PAGESIZE 10000;
SET LINESIZE 200;

SELECT * FROM elev;


SELECT e.id_elev, e.nume, e.prenume , c.clasa, c.cod, p.nume_profil
FROM elev e
INNER JOIN elev_clasa ec 
	ON ec.id_elev = e.id_elev
INNER JOIN clasa c 
	ON ec.id_clasa = c.id_clasa
INNER JOIN profil p
	ON p.id_profil = c.id_profil;

/*
SELECT e.id_elev, e.nume, e.prenume, c.an_scolar, c.cod, c.clasa
FROM elev e
INNER JOIN elev_clasa ec ON e.id_elev = ec.id_elev
INNER JOIN clasa c ON ec.id_clasa = c.id_clasa; 
*/

/*
SELECT * FROM profil;
SELECT * FROM clasa;
SELECT * FROM bursa;
*/

--SELECT e.nume, e.prenume, e.etnie, 
--	c.id_clasa, c.an_scolar, c.cod, c.clasa, p.nume_profil
--FROM elev e 
--INNER JOIN elev_clasa ec ON e.id_elev = ec.id_elev
--INNER JOIN clasa c ON c.id_clasa = ec.id_clasa
--INNER JOIN profil p ON c.id_profil = p.id_profil;

-----------------------------------
/*
DROP SEQUENCE profil_seq;
DROP SEQUENCE clasa_seq;

DROP TABLE medie;
DROP TABLE elev_bursa;
DROP TABLE elev_clasa;
DROP TABLE profil_materie;

DROP TABLE elev;
DROP SEQUENCE elev_seq;

DROP TABLE clasa;
DROP TABLE profil;
DROP TABLE materie;
DROP TABLE bursa;
*/
-----------------------------------


BEGIN	
	dbms_output.put_line ('Salut');
END;
/
