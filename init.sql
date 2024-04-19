DROP DATABASE IF EXISTS quickmenu;

CREATE DATABASE quickmenu;

\c quickmenu

CREATE TABLE users (
  user_id UUID NOT NULL UNIQUE PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  username TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  role SMALLINT NOT NULL
);

CREATE TABLE menus (
  menu_id UUID NOT NULL UNIQUE PRIMARY KEY,
  user_id UUID NOT NULL,
  name TEXT NOT NULL,
  code VARCHAR(6) NOT NULL UNIQUE
);

CREATE TABLE options (
  option_id UUID NOT NULL UNIQUE PRIMARY KEY,
  menu_id UUID NOT NULL,
  name TEXT NOT NULL,
  price NUMERIC(5, 2) NOT NULL,
  avaliability BOOLEAN NOT NULL
);

CREATE TABLE settings (
  settings_id UUID NOT NULL UNIQUE PRIMARY KEY,
  menu_id UUID NOT NULL,
  background_color TEXT NOT NULL,
  font_color TEXT NOT NULL,
  font_size SMALLINT NOT NULL
);