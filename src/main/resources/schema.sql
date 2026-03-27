CREATE TABLE IF NOT EXISTS Post (
    id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    body VARCHAR(255) NULL,
    version INT,
    PRIMARY KEY (id)
);