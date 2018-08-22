create table if not exists User (
  UserId INTEGER identity PRIMARY KEY,
  Username  varchar(30) unique not null
);