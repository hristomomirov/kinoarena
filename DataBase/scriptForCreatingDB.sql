-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema kinoarena
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema kinoarena
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `kinoarena` DEFAULT CHARACTER SET utf8 ;
USE `kinoarena` ;

-- -----------------------------------------------------
-- Table `kinoarena`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `role_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `age` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`cinema`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`cinema` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `ciry` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`hall`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`hall` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `number` INT NOT NULL,
  `capacity` INT NOT NULL,
  `cinema_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `cinema_id_idx` (`cinema_id` ASC) ,
  CONSTRAINT `cinema_id`
    FOREIGN KEY (`cinema_id`)
    REFERENCES `kinoarena`.`cinema` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;

-- -----------------------------------------------------
-- Table `kinoarena`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`genre` (
  `id` INT NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`projection`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`projection` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `lenght` INT NOT NULL,
  `descripiton` TEXT NOT NULL,
  `age_restriction` INT NOT NULL DEFAULT 3,
  `genre_id` INT NOT NULL,
  `start_at` DATETIME NOT NULL COMMENT 'A movie can\'t be added before 10 am ',
  `end_at` DATETIME NOT NULL COMMENT 'A movie cant finish after 1am',
  PRIMARY KEY (`id`),
  INDEX `genre_id_idx` (`genre_id` ASC) ,
  CONSTRAINT `genre_id`
    FOREIGN KEY (`genre_id`)
    REFERENCES `kinoarena`.`genre` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`halls_have_projections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`halls_have_projections` (
  `hall_id` INT NOT NULL,
  `projection_id` INT NOT NULL,
  INDEX `hall_id_idx` (`hall_id` ASC) ,
  INDEX `projection_id_idx` (`projection_id` ASC) ,
  CONSTRAINT `hall_id`
    FOREIGN KEY (`hall_id`)
    REFERENCES `kinoarena`.`hall` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `projection_id`
    FOREIGN KEY (`projection_id`)
    REFERENCES `kinoarena`.`projection` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `owner_id` INT NOT NULL,
  `cinema_id` INT NOT NULL,
  `hall_id` INT NOT NULL,
  `projection_id` INT NOT NULL,
  `row` INT NOT NULL,
  `seat` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `owner_id_idx` (`owner_id` ASC) ,
  INDEX `cinema_id_idx` (`cinema_id` ASC) ,
  INDEX `screen_id_idx` (`hall_id` ASC) ,
  INDEX `movie_id_idx` (`projection_id` ASC) ,
  CONSTRAINT `ticket_owner_id`
    FOREIGN KEY (`owner_id`)
    REFERENCES `kinoarena`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ticket_cinema_id`
    FOREIGN KEY (`cinema_id`)
    REFERENCES `kinoarena`.`cinema` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ticket_hall_id`
    FOREIGN KEY (`hall_id`)
    REFERENCES `kinoarena`.`hall` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ticket_projection_id`
    FOREIGN KEY (`projection_id`)
    REFERENCES `kinoarena`.`projection` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`user_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`user_roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_roles_id`
    FOREIGN KEY (`id`)
    REFERENCES `kinoarena`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `kinoarena`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kinoarena`.`status` (
  `id` INT NOT NULL,
  `status` VARCHAR(45) NOT NULL COMMENT 'adult\nstudent\nelder',
  PRIMARY KEY (`id`),
  CONSTRAINT `status_id`
    FOREIGN KEY (`id`)
    REFERENCES `kinoarena`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
