-- =============================================
-- PROYECTO: EcoVolt - Arquitectura Web
-- ESTÁNDAR: PostgreSQL
-- =============================================
--CREATE DATABASE ecovolt

-- 1. TABLA: subscription_plans (Independiente)
CREATE TABLE subscription_plans (
                                    id_plan BIGSERIAL PRIMARY KEY,
    -- 5 Campos de Negocio
                                    name VARCHAR(50) NOT NULL,
                                    monthly_price DECIMAL(10,2) NOT NULL,
                                    device_limit INTEGER NOT NULL,
                                    support_level VARCHAR(30) NOT NULL,
                                    billing_cycle VARCHAR(20) NOT NULL,
    -- 5 Campos de Auditoría
                                    status INTEGER DEFAULT 1,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP,
                                    created_by VARCHAR(50) DEFAULT 'SYSTEM',
                                    updated_by VARCHAR(50)
);

-- 2. TABLA: users (Depende de subscription_plans)
CREATE TABLE users (
                       id_user BIGSERIAL PRIMARY KEY,
                       id_plan BIGINT NOT NULL,
    -- 5 Campos de Negocio
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       phone_number VARCHAR(15),
    -- 5 Campos de Auditoría
                       status INTEGER DEFAULT 1,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,
                       created_by VARCHAR(50) DEFAULT 'SYSTEM',
                       updated_by VARCHAR(50),
                       CONSTRAINT fk_user_plan FOREIGN KEY(id_plan) REFERENCES subscription_plans(id_plan)
);

-- 3. TABLA: homes (Depende de users)
CREATE TABLE homes (
                       id_home BIGSERIAL PRIMARY KEY,
                       id_user BIGINT NOT NULL,
    -- 5 Campos de Negocio
                       address VARCHAR(200) NOT NULL,
                       city VARCHAR(50) NOT NULL,
                       alias VARCHAR(50) NOT NULL, -- Ej: Casa Playa
                       energy_tariff DECIMAL(10,4) NOT NULL, -- Costo por kWh
                       square_meters DECIMAL(10,2),
    -- 5 Campos de Auditoría
                       status INTEGER DEFAULT 1,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,
                       created_by VARCHAR(50) DEFAULT 'SYSTEM',
                       updated_by VARCHAR(50),
                       CONSTRAINT fk_home_user FOREIGN KEY(id_user) REFERENCES users(id_user)
);

-- 4. TABLA: rooms (Depende de homes)
CREATE TABLE rooms (
                       id_room BIGSERIAL PRIMARY KEY,
                       id_home BIGINT NOT NULL,
    -- 5 Campos de Negocio
                       name VARCHAR(50) NOT NULL, -- Ej: Cocina
                       floor_number INTEGER NOT NULL,
                       orientation VARCHAR(20), -- Norte, Sur...
                       area_sqm DECIMAL(10,2),
                       room_type VARCHAR(30), -- Social, Privado, Servicio
    -- 5 Campos de Auditoría
                       status INTEGER DEFAULT 1,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,
                       created_by VARCHAR(50) DEFAULT 'SYSTEM',
                       updated_by VARCHAR(50),
                       CONSTRAINT fk_room_home FOREIGN KEY(id_home) REFERENCES homes(id_home)
);

-- 5. TABLA: devices (Depende de rooms)
CREATE TABLE devices (
                         id_device BIGSERIAL PRIMARY KEY,
                         id_room BIGINT NOT NULL,
    -- 5 Campos de Negocio
                         serial_number VARCHAR(100) NOT NULL UNIQUE,
                         device_name VARCHAR(100) NOT NULL,
                         category VARCHAR(50) NOT NULL, -- Ej: Climatización
                         manufacturer VARCHAR(50),
                         firmware_version VARCHAR(20),
    -- 5 Campos de Auditoría
                         status INTEGER DEFAULT 1,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP,
                         created_by VARCHAR(50) DEFAULT 'SYSTEM',
                         updated_by VARCHAR(50),
                         CONSTRAINT fk_device_room FOREIGN KEY(id_room) REFERENCES rooms(id_room)
);

-- 6. TABLA: energy_readings (Depende de devices)
CREATE TABLE energy_readings (
                                 id_reading BIGSERIAL PRIMARY KEY,
                                 id_device BIGINT NOT NULL,
    -- 5 Campos de Negocio
                                 wattage DECIMAL(10,2) NOT NULL,
                                 voltage DECIMAL(10,2) NOT NULL,
                                 amperage DECIMAL(10,4) NOT NULL,
                                 power_factor DECIMAL(4,3) NOT NULL, -- Eficiencia (0 a 1)
                                 frequency DECIMAL(5,2) NOT NULL, -- Hz
    -- 5 Campos de Auditoría
                                 status INTEGER DEFAULT 1,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP,
                                 created_by VARCHAR(50) DEFAULT 'SYSTEM',
                                 updated_by VARCHAR(50),
                                 CONSTRAINT fk_reading_device FOREIGN KEY(id_device) REFERENCES devices(id_device)
);