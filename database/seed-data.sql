USE railway_reservation;
INSERT IGNORE INTO trains(train_number,train_name,source,destination,departure_time,arrival_time,fare,capacity,active) VALUES
('12001','Shatabdi Express','New Delhi','Agra Cantt','06:00:00','08:00:00',650.00,120,TRUE),
('12951','Mumbai Rajdhani','New Delhi','Mumbai Central','16:30:00','08:35:00',1850.00,200,TRUE),
('12301','Howrah Rajdhani','New Delhi','Howrah','17:00:00','09:55:00',1650.00,180,TRUE),
('12627','Karnataka Express','New Delhi','Bengaluru City','20:20:00','06:40:00',1400.00,220,TRUE),
('12002','Return Shatabdi','Agra Cantt','New Delhi','18:00:00','20:00:00',650.00,120,TRUE);
-- Schedules are generated for the next 30 days by database/generate-schedules.sql after importing this seed.
