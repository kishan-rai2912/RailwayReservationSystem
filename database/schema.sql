CREATE DATABASE IF NOT EXISTS railway_reservation CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE railway_reservation;
CREATE TABLE IF NOT EXISTS users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, full_name VARCHAR(100) NOT NULL, email VARCHAR(190) NOT NULL UNIQUE,
 phone VARCHAR(20) NOT NULL, password_hash VARCHAR(100) NOT NULL, role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER', created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS trains (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, train_number VARCHAR(20) NOT NULL UNIQUE, train_name VARCHAR(120) NOT NULL,
 source VARCHAR(100) NOT NULL, destination VARCHAR(100) NOT NULL, departure_time TIME NOT NULL, arrival_time TIME NOT NULL,
 fare DECIMAL(10,2) NOT NULL, capacity INT NOT NULL, active BOOLEAN NOT NULL DEFAULT TRUE,
 CHECK(fare >= 0), CHECK(capacity > 0), INDEX idx_route(source,destination,active)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS train_schedules (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, train_id BIGINT NOT NULL, journey_date DATE NOT NULL,
 available_seats INT NOT NULL, UNIQUE KEY uq_train_date(train_id,journey_date),
 CONSTRAINT fk_schedule_train FOREIGN KEY(train_id) REFERENCES trains(id), CHECK(available_seats >= 0), INDEX idx_schedule_date(journey_date)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS bookings (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, pnr CHAR(12) NOT NULL UNIQUE, user_id BIGINT NOT NULL, schedule_id BIGINT NOT NULL,
 passenger_count INT NOT NULL, total_fare DECIMAL(10,2) NOT NULL, status ENUM('CONFIRMED','CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, cancelled_at TIMESTAMP NULL,
 CONSTRAINT fk_booking_user FOREIGN KEY(user_id) REFERENCES users(id), CONSTRAINT fk_booking_schedule FOREIGN KEY(schedule_id) REFERENCES train_schedules(id),
 CHECK(passenger_count > 0), CHECK(total_fare >= 0), INDEX idx_booking_user(user_id,created_at), INDEX idx_booking_schedule(schedule_id,status)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS passengers (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, booking_id BIGINT NOT NULL, full_name VARCHAR(100) NOT NULL, age TINYINT UNSIGNED NOT NULL,
 gender ENUM('FEMALE','MALE','OTHER') NOT NULL, CONSTRAINT fk_passenger_booking FOREIGN KEY(booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
 CHECK(age <= 120), INDEX idx_passenger_booking(booking_id)
) ENGINE=InnoDB;
