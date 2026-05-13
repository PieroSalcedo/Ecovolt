INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (1, 'Plan Básico', 19.90, 5, 'Básico', 'Mensual', 1);
INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (2, 'Plan Estándar', 29.90, 10, 'Intermedio', 'Mensual', 1);
INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (3, 'Plan Premium', 49.90, 20, 'Prioritario', 'Mensual', 1);
INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (4, 'Plan Familiar', 59.90, 30, 'Prioritario', 'Mensual', 1);
INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (5, 'Plan Hogar Plus', 69.90, 40, 'Avanzado', 'Mensual', 1);
INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (6, 'Plan Casa Inteligente', 89.90, 60, 'Avanzado', 'Mensual', 1);
INSERT INTO subscription_plans (id_plan, name, monthly_price, device_limit, support_level, billing_cycle, status) VALUES (7, 'Plan Ilimitado', 119.90, 100, 'Premium', 'Mensual', 1);


INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (1, 1, 'carlos.lopez@gmail.com', 'password123', 'Carlos', 'Lopez', '987654321', 1);
INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (2, 2, 'maria.torres@gmail.com', 'password123', 'Maria', 'Torres', '986123456', 1);
INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (3, 3, 'jose.ramos@gmail.com', 'password123', 'Jose', 'Ramos', '985456789', 1);
INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (4, 4, 'ana.paredes@gmail.com', 'password123', 'Ana', 'Paredes', '984321654', 1);
INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (5, 5, 'renzo.devoto@gmail.com', 'password123', 'Renzo', 'Devoto', '983789456', 1);
INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (6, 6, 'ruth.vasquez@gmail.com', 'password123', 'Ruth', 'Vasquez', '982654987', 1);
INSERT INTO users (id_user, id_plan, email, password, first_name, last_name, phone_number, status) VALUES (7, 7, 'luis.villegas@gmail.com', 'password123', 'Luis', 'Villegas', '981147258', 1);


INSERT INTO roles (id_role, role_name, description, status) VALUES (1, 'PROPIETARIO', 'Dueño de la vivienda con acceso completo al sistema EcoVolt', 1);
INSERT INTO roles (id_role, role_name, description, status) VALUES (2, 'ARRENDATARIO', 'Usuario que alquila una vivienda y accede a los dispositivos autorizados', 1);


INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (1, 1, 1, 1);
INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (2, 2, 1, 1);
INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (3, 3, 2, 1);
INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (4, 4, 2, 1);
INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (5, 5, 1, 1);
INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (6, 6, 2, 1);
INSERT INTO user_roles (id_user_role, id_user, id_role, status) VALUES (7, 7, 1, 1);


INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (1, 1, 'Av. Los Olivos 245', 'Lima', 'Casa Principal', 0.6543, 120.50, 1);
INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (2, 2, 'Jr. Primavera 458', 'Surco', 'Departamento Familiar', 0.6890, 95.30, 1);
INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (3, 3, 'Calle Las Flores 120', 'Miraflores', 'Departamento Alquilado', 0.6725, 80.00, 1);
INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (4, 4, 'Av. Central 985', 'San Borja', 'Minidepartamento', 0.6600, 65.75, 1);
INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (5, 5, 'Calle Los Robles 321', 'San Isidro', 'Casa EcoVolt', 0.7012, 150.00, 1);
INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (6, 6, 'Av. Universitaria 744', 'San Miguel', 'Vivienda Compartida', 0.6480, 88.40, 1);
INSERT INTO homes (id_home, id_user, address, city, alias, energy_tariff, square_meters, status) VALUES (7, 7, 'Jr. Castilla 562', 'La Molina', 'Casa de Campo', 0.6920, 180.90, 1);


INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (1, 1, 'Sala', 1, 'Norte', 25.50, 'Social', 1);
INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (2, 2, 'Dormitorio Principal', 2, 'Este', 18.20, 'Privado', 1);
INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (3, 3, 'Cocina', 1, 'Sur', 14.00, 'Servicio', 1);
INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (4, 4, 'Comedor', 1, 'Oeste', 22.30, 'Social', 1);
INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (5, 5, 'Estudio', 2, 'Norte', 16.40, 'Privado', 1);
INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (6, 6, 'Lavandería', 1, 'Sur', 10.80, 'Servicio', 1);
INSERT INTO rooms (id_room, id_home, name, floor_number, orientation, area_sqm, room_type, status) VALUES (7, 7, 'Dormitorio Secundario', 2, 'Este', 15.60, 'Privado', 1);


INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (1, 1, 'SN-ECO-001', 'Luz Inteligente de Sala', 'Iluminación', 'EcoVolt', '1.0.0', 1);
INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (2, 2, 'SN-ECO-002', 'Aire Acondicionado', 'Climatización', 'Samsung', '2.1.0', 1);
INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (3, 3, 'SN-ECO-003', 'Refrigeradora Inteligente', 'Electrodoméstico', 'LG', '3.0.2', 1);
INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (4, 4, 'SN-ECO-004', 'Sensor de Movimiento', 'Seguridad', 'EcoVolt', '1.2.5', 1);
INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (5, 5, 'SN-ECO-005', 'Tomacorriente Inteligente', 'Energía', 'TP-Link', '4.0.1', 1);
INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (6, 6, 'SN-ECO-006', 'Lavadora Inteligente', 'Electrodoméstico', 'Whirlpool', '2.5.3', 1);
INSERT INTO devices (id_device, id_room, serial_number, device_name, category, manufacturer, firmware_version, status) VALUES (7, 7, 'SN-ECO-007', 'Sensor de Voltaje', 'Monitoreo', 'EcoVolt', '1.1.4', 1);


INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (1, 1, 60.50, 220.00, 0.2750, 0.950, 60.00, 1);
INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (2, 2, 1200.00, 220.00, 5.4545, 0.920, 60.00, 1);
INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (3, 3, 350.75, 220.00, 1.5943, 0.890, 60.00, 1);
INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (4, 4, 15.20, 220.00, 0.0691, 0.970, 60.00, 1);
INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (5, 5, 180.40, 220.00, 0.8200, 0.940, 60.00, 1);
INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (6, 6, 850.00, 220.00, 3.8636, 0.910, 60.00, 1);
INSERT INTO energy_readings (id_reading, id_device, wattage, voltage, amperage, power_factor, frequency, status) VALUES (7, 7, 45.30, 220.00, 0.2059, 0.980, 60.00, 1);


INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (1, 2, 'SOBRECONSUMO', 'Consumo excesivo', 'El aire acondicionado superó el consumo esperado.', 'Alta', '2026-05-13 08:30:00', 'Pendiente', 1);
INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (2, 7, 'VOLTAJE', 'Voltaje inestable', 'Se detectaron variaciones irregulares de voltaje.', 'Media', '2026-05-13 09:00:00', 'Pendiente', 1);
INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (3, 4, 'CONEXION', 'Dispositivo desconectado', 'El sensor de movimiento perdió conexión.', 'Alta', '2026-05-13 09:15:00', 'Resuelta', 1);
INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (4, 3, 'ANOMALIA', 'Consumo anómalo', 'La refrigeradora presenta un consumo mayor al habitual.', 'Media', '2026-05-13 10:00:00', 'Pendiente', 1);
INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (5, 5, 'USO_PROLONGADO', 'Dispositivo encendido por mucho tiempo', 'El tomacorriente inteligente ha permanecido activo demasiado tiempo.', 'Baja', '2026-05-13 10:30:00', 'Pendiente', 1);
INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (6, 6, 'SOBRECONSUMO', 'Consumo elevado en lavadora', 'La lavadora registró un pico alto de energía.', 'Media', '2026-05-13 11:00:00', 'Resuelta', 1);
INSERT INTO alerts (id_alert, id_device, alert_type, title, description, severity_level, alert_date, alert_status, status) VALUES (7, 1, 'ILUMINACION', 'Luz activa fuera del horario', 'La luz de sala quedó encendida fuera de la programación.', 'Baja', '2026-05-13 11:20:00', 'Pendiente', 1);


INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (1, 1, 1, 'Encendido nocturno de sala', 'Horario', 'ENCENDER', '18:00:00', '23:00:00', 'Lunes,Martes,Miércoles,Jueves,Viernes', 'Activa', 1);
INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (2, 2, 2, 'Apagado automático del aire acondicionado', 'Ahorro energético', 'APAGAR', '23:30:00', '23:35:00', 'Todos los días', 'Activa', 1);
INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (3, 3, 3, 'Revisión de consumo de refrigeradora', 'Monitoreo', 'VERIFICAR_CONSUMO', '08:00:00', '08:10:00', 'Todos los días', 'Activa', 1);
INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (4, 4, 4, 'Activación de seguridad nocturna', 'Seguridad', 'ACTIVAR', '22:00:00', '06:00:00', 'Todos los días', 'Activa', 1);
INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (5, 5, 5, 'Modo ahorro en tomacorriente', 'Ahorro energético', 'APAGAR', '00:00:00', '06:00:00', 'Todos los días', 'Activa', 1);
INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (6, 6, 6, 'Lavado programado', 'Horario', 'ENCENDER', '07:00:00', '08:30:00', 'Sábado', 'Pausada', 1);
INSERT INTO automations (id_automation, id_user, id_device, automation_name, automation_type, action, start_time, end_time, week_days, automation_status, status) VALUES (7, 7, 7, 'Monitoreo diario de voltaje', 'Monitoreo', 'MEDIR', '09:00:00', '09:05:00', 'Todos los días', 'Activa', 1);


INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (1, 2, 1, 'ALERTA', 'Consumo excesivo detectado', 'El aire acondicionado superó el consumo esperado.', 'Push', '2026-05-13 08:31:00', NULL, 'No leída', 1);
INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (2, 7, 2, 'ALERTA', 'Voltaje inestable', 'Se detectaron irregularidades de voltaje en tu vivienda.', 'Correo', '2026-05-13 09:01:00', '2026-05-13 09:10:00', 'Leída', 1);
INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (3, 4, 3, 'ALERTA', 'Sensor desconectado', 'El sensor de movimiento perdió conexión.', 'Push', '2026-05-13 09:16:00', '2026-05-13 09:20:00', 'Leída', 1);
INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (4, 3, 4, 'ALERTA', 'Consumo anómalo', 'La refrigeradora presenta un consumo inusual.', 'SMS', '2026-05-13 10:01:00', NULL, 'No leída', 1);
INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (5, 5, 5, 'ALERTA', 'Uso prolongado detectado', 'Un tomacorriente ha permanecido activo demasiado tiempo.', 'Web', '2026-05-13 10:31:00', NULL, 'No leída', 1);
INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (6, 6, 6, 'ALERTA', 'Pico de energía detectado', 'La lavadora registró un consumo elevado.', 'Correo', '2026-05-13 11:01:00', '2026-05-13 11:15:00', 'Leída', 1);
INSERT INTO notifications (id_notification, id_user, id_alert, notification_type, title, message, delivery_channel, sent_at, read_at, notification_status, status) VALUES (7, 1, 7, 'ALERTA', 'Luz activa fuera del horario', 'La luz de sala permaneció activa más tiempo del programado.', 'Push', '2026-05-13 11:21:00', NULL, 'No leída', 1);


INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (1, 1, 1, 'Mensual', '2026-04-01', '2026-04-30', 'PDF', '/reportes/reporte_abril_usuario1.pdf', '2026-05-01 08:00:00', 1);
INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (2, 2, 2, 'Semanal', '2026-05-01', '2026-05-07', 'Excel', '/reportes/reporte_semana1_usuario2.xlsx', '2026-05-08 09:00:00', 1);
INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (3, 3, 3, 'Mensual', '2026-04-01', '2026-04-30', 'PDF', '/reportes/reporte_abril_usuario3.pdf', '2026-05-01 10:00:00', 1);
INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (4, 4, 4, 'Diario', '2026-05-12', '2026-05-12', 'Excel', '/reportes/reporte_diario_usuario4.xlsx', '2026-05-13 07:30:00', 1);
INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (5, 5, 5, 'Mensual', '2026-04-01', '2026-04-30', 'PDF', '/reportes/reporte_abril_usuario5.pdf', '2026-05-01 11:30:00', 1);
INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (6, 6, 6, 'Semanal', '2026-05-05', '2026-05-11', 'Excel', '/reportes/reporte_semana2_usuario6.xlsx', '2026-05-12 08:45:00', 1);
INSERT INTO energy_reports (id_report, id_user, id_home, report_type, start_date, end_date, report_format, file_path, generated_at, status) VALUES (7, 7, 7, 'Mensual', '2026-04-01', '2026-04-30', 'PDF', '/reportes/reporte_abril_usuario7.pdf', '2026-05-01 12:15:00', 1);