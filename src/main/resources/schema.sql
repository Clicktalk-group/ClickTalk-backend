CREATE DATABASE IF NOT EXISTS clicktalk_db;

use clicktalk_db;

CREATE TABLE IF NOT EXISTS users (
        id BIGINT AUTO_INCREMENT PRIMARY KEY ,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(50) NOT NULL,
        created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS conversations (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id bigint NOT NULL,
        title VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT NOW(),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS projects(
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT,
        title VARCHAR(255) NOT NULL ,
        context LONGTEXT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS project_conversation(
        project_id BIGINT,
        conv_id BIGINT UNIQUE,
        PRIMARY KEY (project_id,conv_id),
        FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
        FOREIGN KEY (conv_id) REFERENCES  conversations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        conv_id BIGINT NOT NULL,
        content LONGTEXT NOT NULL,
        is_bot BOOLEAN NOT NULL,
        created_at TIMESTAMP DEFAULT NOW(),
        FOREIGN KEY (conv_id) REFERENCES conversations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS settings (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL,
        theme VARCHAR(255) NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

