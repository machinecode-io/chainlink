CREATE SCHEMA IF NOT EXISTS App;

DROP TABLE IF EXISTS App.Numbers;
DROP TABLE IF EXISTS App.Orders;
DROP TABLE IF EXISTS App.Inventory;

CREATE TABLE IF NOT EXISTS App.Numbers (
  item  	INT,
  quantity  INT
);

CREATE TABLE IF NOT EXISTS App.Orders (
  orderID	INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  itemID	INT,
  quantity  INT
);

CREATE TABLE IF NOT EXISTS App.Inventory(
  itemID	INT NOT NULL PRIMARY KEY,
  quantity	INT NOT NULL
);

INSERT INTO App.Inventory VALUES (1, 100);

INSERT INTO App.Numbers
  VALUES (1, 10);

INSERT INTO App.Numbers
  VALUES (2, 10);

INSERT INTO App.Numbers
  VALUES (3, 10);

INSERT INTO App.Numbers
  VALUES (4, 10);

INSERT INTO App.Numbers
  VALUES (5, 10);

INSERT INTO App.Numbers
  VALUES (6, 10);

INSERT INTO App.Numbers
  VALUES (7, 10);

INSERT INTO App.Numbers
  VALUES (8, 10);

INSERT INTO App.Numbers
  VALUES (9, 10);

INSERT INTO App.Numbers
  VALUES (10, 10);

INSERT INTO App.Numbers
  VALUES (11, 10);

INSERT INTO App.Numbers
  VALUES (12, 10);

INSERT INTO App.Numbers
  VALUES (13, 10);

INSERT INTO App.Numbers
  VALUES (14, 10);

INSERT INTO App.Numbers
  VALUES (15, 10);

INSERT INTO App.Numbers
  VALUES (16, 10);

INSERT INTO App.Numbers
  VALUES (17, 10);

INSERT INTO App.Numbers
  VALUES (18, 10);

INSERT INTO App.Numbers
  VALUES (19, 10);

INSERT INTO App.Numbers
  VALUES (20, 10);