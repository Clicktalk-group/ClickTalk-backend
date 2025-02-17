CREATE DATABASE IF NOT EXISTS clicktalk_db;

use clicktalk_db;

CREATE TABLE IF NOT EXISTS Users (
        id BIGINT AUTO_INCREMENT PRIMARY KEY ,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS Conversations (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id bigint NOT NULL,
        title VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT NOW(),
        FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Messages (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        conv_id BIGINT NOT NULL,
        content LONGTEXT NOT NULL,
        is_bot BOOLEAN NOT NULL,
        created_at TIMESTAMP DEFAULT NOW(),
        FOREIGN KEY (conv_id) REFERENCES Conversations(id)
);

CREATE TABLE IF NOT EXISTS Settings (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL,
        theme VARCHAR(255) NOT NULL,
        FOREIGN KEY (user_id) REFERENCES Users(id)
);

