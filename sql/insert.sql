/*
Su Feng A20338748
CS425 proj INSERT.SQL
Insert data into relations
*/

INSERT INTO State VALUES(1,'selling');
INSERT INTO State VALUES(2,'sold');
INSERT INTO State VALUES(3,'asking');
INSERT INTO State VALUES(4,'solved');
INSERT INTO State VALUES(5,'closed');


INSERT INTO Subject VALUES(1,'sell');
INSERT INTO Subject VALUES(2,'review');
INSERT INTO Subject VALUES(3,'ask');
INSERT INTO Subject VALUES(4,'want');

INSERT INTO Groups VALUES(1,'Restaurant','Review and rate restaurants');
INSERT INTO Groups VALUES(2,'Computer','Review, ask, get and sell computers');

INSERT INTO Users VALUES(users_seq.nextval,'sfeng14','sfeng14','sfeng14@hawk.iit.edu',0);
INSERT INTO Users VALUES(users_seq.nextval,'hshu','hshu','hshu@purdue.edu',0);