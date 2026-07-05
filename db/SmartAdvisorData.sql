-- Data opcional para probar EcoVolt Smart Advisor en PostgreSQL.
-- Ejecutar despues de ScriptBd.txt, cuando ya existan homes, rooms, devices y readings.

INSERT INTO public.energy_goal (monthly_limit_kwh, alert_threshold_percentage, id_home, status)
VALUES
(90.00, 80, 1, 1),
(55.00, 75, 2, 1),
(35.00, 80, 3, 1)
ON CONFLICT DO NOTHING;

-- Lecturas extra para reforzar el analisis mensual de la vivienda 1.
-- Usa dispositivos existentes creados por ScriptBd.txt.
INSERT INTO public.energy_reading (wattage, voltage, id_device, reading_at, status)
VALUES
(8.5000, 222.10, 1, NOW() - interval '2 days', 1),
(7.9000, 221.70, 1, NOW() - interval '1 day', 1),
(12.3000, 223.20, 2, NOW() - interval '3 days', 1),
(10.8000, 220.40, 2, NOW() - interval '1 day', 1),
(6.4000, 219.80, 3, NOW() - interval '4 days', 1),
(5.7000, 220.50, 4, NOW() - interval '2 days', 1);

-- Opcion de menu para Angular.
-- type = 2 para que aparezca en el desplegable "Consultas".
INSERT INTO public."option" ("name", route, "type", status)
VALUES ('Smart Advisor IA', 'advisor/smart', 2, 1);

INSERT INTO public.rol_has_option (id_role, id_option)
SELECT 1, id_option FROM public."option" WHERE route = 'advisor/smart';
