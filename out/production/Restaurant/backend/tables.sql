CREATE SEQUENCE product_seq START WITH 1;
CREATE TABLE Product(
  ID int NOT NULL DEFAULT nextval('product_seq'),
  NAME varchar(30) NOT NULL,
  MEAL_TYPE varchar(10) NOT NULL,
  DESCRIPTION varchar(200) NOT NULL,
  PRICE int NOT NULL,
  AVAILABILITY BOOLEAN NOT NULL DEFAULT TRUE,
  APPROX_TIME int NOT NULL DEFAULT 30,
  CONSTRAINT positive_price CHECK(PRICE > 0),
  PRIMARY KEY(ID)
);
ALTER SEQUENCE product_seq OWNED BY Product.ID;

CREATE SEQUENCE allergy_seq START WITH 1;
CREATE TABLE Allergy(
  ID int NOT NULL DEFAULT nextval('allergy_seq'),
  NAME varchar(30) NOT NULL,
  PRIMARY KEY(ID)
);
ALTER SEQUENCE allergy_seq OWNED BY Allergy.ID;

CREATE SEQUENCE staff_seq START WITH 1;
CREATE TABLE Staff(
  ID int NOT NULL DEFAULT nextval('staff_seq'),
  FIRST_NAME varchar(30) NOT NULL,
  LAST_NAME varchar(30) NOT NULL,
  ROLE varchar(10) NOT NULL,
  PRIMARY KEY(ID)
);
ALTER SEQUENCE staff_seq OWNED BY Staff.ID;

CREATE TABLE Labour(
  STAFF_ID int NOT NULL,
  CLOCK_IN timestamp NOT NULL,
  CLOCK_OUT timestamp DEFAULT NULL,
  CONSTRAINT chronological CHECK(CLOCK_IN < CLOCK_OUT),
  FOREIGN KEY(STAFF_ID) REFERENCES Staff(ID),
  PRIMARY KEY(STAFF_ID, CLOCK_IN)
);

CREATE TABLE Tables(
  TABLE_NUM int NOT NULL,
  STAFF_ID int NOT NULL,
  FOREIGN KEY(STAFF_ID) REFERENCES Staff(ID),
  PRIMARY KEY(TABLE_NUM)
);

CREATE TABLE TableAssist(
  TABLE_NUM int NOT NULL,
  REQUEST_TIME timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  REASON varchar(30),
  FOREIGN KEY(TABLE_NUM) REFERENCES TABLES(TABLE_NUM),
  PRIMARY KEY(TABLE_NUM)
);

CREATE SEQUENCE orders_seq START WITH 1;
CREATE TABLE Orders(
  ID int NOT NULL DEFAULT nextval('orders_seq'),
  TABLE_NUM int NOT NULL,
  STAFF_ID int DEFAULT NULL,
  STATUS varchar(30) NOT NULL DEFAULT 'UNCONFIRMED',
  CREATION_TIME timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PAID BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY(STAFF_ID) REFERENCES STAFF(ID),
  FOREIGN KEY(TABLE_NUM) REFERENCES TABLES(TABLE_NUM),
  PRIMARY KEY(ID)
);
ALTER SEQUENCE orders_seq OWNED BY Orders.ID;

CREATE SEQUENCE ingredient_seq START WITH 1;
CREATE TABLE Ingredient(
  ID int NOT NULL DEFAULT nextval('ingredient_seq'),
  NAME varchar(30) NOT NULL,
  INITIAL_STOCK int NOT NULL,
  PROCUREMENT_PRICE int NOT NULL,
  MENU_PRICE int NOT NULL,
  CALORIES int NOT NULL,
  PRIMARY KEY(ID)
);
ALTER SEQUENCE ingredient_seq OWNED BY Ingredient.ID;

CREATE TABLE IngredientAllergy(
  INGREDIENT_ID int NOT NULL,
  ALLERGY_ID int NOT NULL,
  FOREIGN KEY(INGREDIENT_ID) REFERENCES INGREDIENT(ID),
  FOREIGN KEY(ALLERGY_ID) REFERENCES ALLERGY(ID),
  PRIMARY KEY(INGREDIENT_ID, ALLERGY_ID)
);

CREATE TABLE ProdIngredient(
  PRODUCT_ID int NOT NULL,
  INGREDIENT_ID int NOT NULL,
  QUANTITY int NOT NULL,
  CONSTRAINT positive_ingredient_quantity CHECK(QUANTITY > 0),
  FOREIGN KEY(PRODUCT_ID) REFERENCES PRODUCT(ID),
  FOREIGN KEY(INGREDIENT_ID) REFERENCES INGREDIENT(ID),
  PRIMARY KEY(PRODUCT_ID, INGREDIENT_ID)
  
);

CREATE SEQUENCE item_seq START WITH 1;
CREATE TABLE OrderItem(
  ID int NOT NULL DEFAULT nextval('item_seq'),
  PRODUCT_ID int NOT NULL,
  ORDER_ID int NOT NULL,
  QUANTITY int NOT NULL DEFAULT 1,
  CONSTRAINT positive_item_quantity CHECK(QUANTITY > 0),
  COMMENTS varchar(100) DEFAULT NULL,
  FOREIGN KEY(PRODUCT_ID) REFERENCES PRODUCT(ID),
  FOREIGN KEY(ORDER_ID) REFERENCES ORDERS(ID),
  PRIMARY KEY(ID)
);
ALTER SEQUENCE item_seq OWNED BY OrderItem.ID;

CREATE TABLE ItemIngredient(
  ITEM_ID int NOT NULL,
  INGREDIENT_ID int NOT NULL,
  QUANTITY int NOT NULL DEFAULT 1,
  CONSTRAINT positive_ingredient_quantity CHECK(QUANTITY > 0),
  FOREIGN KEY(ITEM_ID) REFERENCES OrderItem(ID),
  FOREIGN KEY(INGREDIENT_ID) REFERENCES INGREDIENT(ID),
  PRIMARY KEY(ITEM_ID, INGREDIENT_ID)
);
