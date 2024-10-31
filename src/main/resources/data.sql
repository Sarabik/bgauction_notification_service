INSERT INTO notifications (user_id, email, notification_text, created) VALUES
(1, 'john.doe@example.com', 'Your auction has ended.', NOW() - INTERVAL 120 MINUTE),
(1, 'john.doe@example.com', 'Your bid was successful.', NOW() - INTERVAL 100 MINUTE),
(2, 'jane.smith@example.com', 'Your auction has ended.', NOW() - INTERVAL 90 MINUTE),
(3, 'alice.johnson@example.com', 'Your auction is about to start.', NOW() - INTERVAL 70 MINUTE);
