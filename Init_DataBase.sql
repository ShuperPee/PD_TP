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
  `idclients` INT NOT NULL,
  `client_addr` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `islogged` TINYINT NOT NULL,
  `directory` VARCHAR(45) NOT NULL,
  `port_udp` INT NOT NULL,
  `port_tcp` INT NOT NULL,
  `counter_udp` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idclients`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_1819`.`files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_1819`.`files` (
  `idfiles` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `size` VARCHAR(45) NOT NULL,
  `clients_idclients` INT NOT NULL,
  PRIMARY KEY (`clients_idclients`),
  CONSTRAINT `fk_files_clients`
    FOREIGN KEY (`clients_idclients`)
    REFERENCES `pd_1819`.`clients` (`idclients`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pd_1819`.`downloads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pd_1819`.`downloads` (
  `iddownloads` INT NOT NULL,
  `filename` VARCHAR(45) NOT NULL,
  `client_up` VARCHAR(45) NOT NULL,
  `client_down` VARCHAR(45) NOT NULL,
  `date` DATETIME NOT NULL,
  `files_clients_idclients` INT NOT NULL,
  PRIMARY KEY (`iddownloads`, `files_clients_idclients`),
  INDEX `fk_downloads_files1_idx` (`files_clients_idclients` ASC) VISIBLE,
  CONSTRAINT `fk_downloads_files1`
    FOREIGN KEY (`files_clients_idclients`)
    REFERENCES `pd_1819`.`files` (`clients_idclients`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
