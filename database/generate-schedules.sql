USE railway_reservation;
-- MySQL 8 recursive CTE: populate the next 30 days for every active train.
INSERT IGNORE INTO train_schedules(train_id,journey_date,available_seats)
WITH RECURSIVE dates AS (SELECT CURRENT_DATE + INTERVAL 1 DAY AS d UNION ALL SELECT d + INTERVAL 1 DAY FROM dates WHERE d < CURRENT_DATE + INTERVAL 30 DAY)
SELECT t.id, dates.d, t.capacity FROM trains t CROSS JOIN dates WHERE t.active=TRUE;
