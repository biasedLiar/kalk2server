INSERT INTO person (username, password_hash) VALUES
    ('sdf', '18ee24150dcb1d96752a4d6dd0f20dfd8ba8c38527e40aa8509b7adecf78f9c6'),
    ('qwe', '489cd5dbc708c7e541de4d7cd91ce6d0f1613573b7fc5b40d3942ccb9555cf35');

INSERT INTO question (text, owner) VALUES
   ('1+5=6.0' , 1),
   ('3-2=1.0' , 1),
   ('5*6=30.0' , 1),
   ('6/2=30.0' , 1),
   ('1+1=2.0' , 1),
   ('6*9+6+9=69.0' , 1),
   ('3*3+5=14.0' , 2),
   ('1+2=3.0' , 2),
   ('7*8=56.0' , 2),
   ('100/4=25.0' , 2),
   ('1/4=0.25' , 2),
   ('10*10=100.0' , 2),
   ('3+0.14=3.14' , 2);
