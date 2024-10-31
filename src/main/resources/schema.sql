DROP TABLE IF EXISTS notifications;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    notification_text TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    UNIQUE (email, created)
);

