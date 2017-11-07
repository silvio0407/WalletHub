CREATE DATABASE PARSE_LOG

USE PARSE_LOG
 
 CREATE TABLE log_information (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data_request DATETIME,
    ip varchar(15),
    description_request varchar(500)
);
