INSERT INTO Product(NAME, MEAL_TYPE, DESCRIPTION, PRICE, AVAILABILITY, APPROX_TIME) VALUES
  ('Baked beans', 'MAIN', 'Beany goodness', '101', TRUE, 10),
  ('Cup of Tea', 'DRINK', 'The best hot drink fight me', '200', TRUE, 5),
  ('Pasta', 'MAIN', 'Good carbs', '300', FALSE, 20),
  ('TACOS', 'MAIN', 'Description of Tacos goes here', '3330', FALSE, 45),
  ('Waffle', 'DESSERT', 'Description of Waffle goes here', '500', TRUE, 15);


INSERT INTO Ingredient(NAME, INITIAL_STOCK, PROCUREMENT_PRICE, MENU_PRICE, CALORIES) VALUES
  ('Baked Beans', 100, 60, 100, 162),
  ('Tea Bags', 10000, 5, 90, 1),
  ('Sugar', 1000, 70, 100, 20),
  ('Milk', 200, 48, 80, 50),
  ('Pasta', 1000, 120, 200, 258),
  ('Pasta Sauce', 10000, 80, 140, 56),
  ('Taco Shell', 450, 96, 110, 176),
  ('Salsa', 300, 170, 230, 6),
  ('Chicken', 300, 100, 200, 47),
  ('Beef', 300, 175, 305, 33),
  ('Quorn Beef', 200, 300, 400, 79),
  ('Quorn Chicken', 200, 300, 400, 75),
  ('Waffle', 250, 130, 500, 115),
  ('Vanilla Ice Cream', 200, 200, 300, 82),
  ('Chocolate Ice Cream', 200, 200, 300, 82),
  ('Strawberry Ice Cream', 200, 200, 300, 82),
  ('Mint Choc Chip Ice Cream', 200, 200, 300, 82),
  ('Chocolate Sauce', 200, 130, 210, 4),
  ('Cream', 200, 180, 280, 52),
  ('Stawberries', 300, 120, 240, 28);

INSERT INTO Allergy(NAME) VALUES
  ('LACTOSE'),
  ('GLUTEN'),
  ('NUTS'),
  ('EGGS'),
  ('WHEAT'),
  ('FISH'),
  ('SHELLFISH'),
  ('SOY');

INSERT INTO IngredientAllergy(INGREDIENT_ID, ALLERGY_ID) VALUES
  (4,1),
  (5,2),
  (7,2),
  (13,1),
  (13,4),
  (14,1),
  (15,1),
  (16,1),
  (17,1),
  (19,1);

INSERT INTO ProdIngredient(PRODUCT_ID, INGREDIENT_ID, QUANTITY) VALUES
  (1,1,1),
  (2,2,1),
  (2,3,1),
  (2,4,1),
  (3,5,1),
  (3,6,1),
  (4,7,1),
  (4,8,1),
  (4,9,1),
  (5,13,2),
  (5,14,2),
  (5,15,1),
  (5,17,2);


INSERT INTO Staff(FIRST_NAME, LAST_NAME, ROLE) VALUES
  ('Charles', 'Card', 'FOH'),
  ('Dragos', '', 'BOH'),
  ('Stephen', 'House', 'FOH'),
  ('Justus', '', 'MANAGEMENT'),
  ('Emily', 'Chaffey', 'FOH'),
  ('Kristina', '', 'FOH'),
  ('Omar', '', 'BOH'),
  ('Megan', '', 'BOH');

INSERT INTO Tables(TABLE_NUM, STAFF_ID) VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5),
  (6, 6),
  (7, 7),
  (8, 8);


INSERT INTO Orders(TABLE_NUM, STAFF_ID, STATUS, CREATION_TIME) VALUES
  (1, 1, 'SENT', '2018-2-10 13:00:00'),
  (2, 5, 'UNCONFIRMED', '2018-2-10 13:05:02'),
  (3, 4, 'READY', '2018-2-10 13:20:00'),
  (4, 3, 'READY', '2018-2-10 13:30:00'),
  (5, 2, 'KITCHEN', '2018-2-10 13:33:00'),
  (6, 2, 'SENT', '2018-2-10 13:34:22'), 
  (7, 4, 'KITCHEN', '2018-2-10 14:33:00');

INSERT INTO OrderItem(ORDER_ID, PRODUCT_ID, QUANTITY, COMMENTS) VALUES
  (1, 1, 1, ''),
  (1, 3, 8, 'Well done'),
  (2, 2, 2, ''),
  (3, 1, 3, ''),
  (4, 2, 4, ''),
  (5, 2, 5, ''),
  (6, 5, 6, ''),
  (7, 5, 7, '');

INSERT INTO ItemIngredient(ITEM_ID, INGREDIENT_ID, QUANTITY) VALUES
  (2, 6, 1),
  (6, 3, 1),
  (7, 16, 1),
  (8, 15, 2);
