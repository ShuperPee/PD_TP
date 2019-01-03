-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pd_1819
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pd_1819
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pd_1819` DEFAULT CHARACTER SET utf8 ;
USE `pd_1819` ;

-- -----------------------------------------------------
-- Table `pd_1819`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_1819`.`clients` (
  `idclients` INT NOT NULL AUTO_INCREMENT,
  `client_addr` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `islogged` TINYINT NOT NULL,
  `directory` VARCHAR(45) NOT NULL,
  `port_udp` INT NOT NULL,
  `port_tcp` INT NOT NULL,
  `counter_udp` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idclients`),
  UNIQUE INDEX `idclients_UNIQUE` (`idclients` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_1819`.`files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_1819`.`files` (
  `idfiles` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `size` VARCHAR(45) NOT NULL,
  `idclients` INT NOT NULL,
  PRIMARY KEY (`idfiles`),
  UNIQUE INDEX `idfiles_UNIQUE` (`idfiles` ASC) VISIBLE,
  CONSTRAINT `fk_files_clients`
    FOREIGN KEY (`idclients`)
    REFERENCES `pd_1819`.`clients` (`idclients`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_1819`.`downloads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_1819`.`downloads` (
  `iddownloads` INT NOT NULL AUTO_INCREMENT,
  `filename` VARCHAR(45) NOT NULL,
  `client_up` VARCHAR(45) NOT NULL,
  `client_down` VARCHAR(45) NOT NULL,
  `date` DATETIME NOT NULL,
  `idfiles` INT NOT NULL,
  PRIMARY KEY (`iddownloads`),
  INDEX `fk_downloads_files1_idx` (`idfiles` ASC) VISIBLE,
  UNIQUE INDEX `iddownloads_UNIQUE` (`iddownloads` ASC) VISIBLE,
  CONSTRAINT `fk_downloads_files1`
    FOREIGN KEY (`idfiles`)
    REFERENCES `pd_1819`.`files` (`idclients`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
