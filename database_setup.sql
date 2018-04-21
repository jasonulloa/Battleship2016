CREATE SCHEMA `battleship` ;

CREATE TABLE `battleship`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `gamesplayed` INT NULL DEFAULT 0,
  `gameswon` INT NULL DEFAULT 0,
  `loggedin` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

CREATE TABLE `battleship`.`games` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user1` INT NULL,
  `user2` INT NULL,
  `gamestate` INT NULL DEFAULT 0,
  `mapsize` INT NULL,
  `chainedshot` INT NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `battleship`.`ships` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `gameid` INT NULL,
  `userid` INT NULL,
  `x` INT NULL,
  `y` INT NULL,
  `shiptype` INT NULL,
  `rightordown` INT NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `battleship`.`tiles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `gameid` INT NULL,
  `userid` INT NULL,
  `x` INT NULL,
  `y` INT NULL,
  `isShip` INT NULL,
  `isHit` INT NULL,
  PRIMARY KEY (`id`));