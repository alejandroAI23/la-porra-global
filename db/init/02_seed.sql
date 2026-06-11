-- =====================================================================
-- La Porra Global - Datos semilla
-- Mundial 2026 - Calendario oficial (72 partidos fase de grupos)
-- Horarios almacenados en UTC. Hora España peninsular = UTC+2 en verano.
-- =====================================================================

SET NAMES utf8mb4;

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_BAR_OWNER');

-- -------------------------------------------------------
-- 48 selecciones clasificadas para el Mundial 2026
-- -------------------------------------------------------

-- Grupo A
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('México',        'MEX', '🇲🇽', 'A'),
    ('Sudáfrica',     'RSA', '🇿🇦', 'A'),
    ('Corea del Sur', 'KOR', '🇰🇷', 'A'),
    ('Chequia',       'CZE', '🇨🇿', 'A');

-- Grupo B
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Canadá',               'CAN', '🇨🇦', 'B'),
    ('Bosnia y Herzegovina', 'BIH', '🇧🇦', 'B'),
    ('Qatar',                'QAT', '🇶🇦', 'B'),
    ('Suiza',                'SUI', '🇨🇭', 'B');

-- Grupo C
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Brasil',    'BRA', '🇧🇷', 'C'),
    ('Marruecos', 'MAR', '🇲🇦', 'C'),
    ('Haití',     'HAI', '🇭🇹', 'C'),
    ('Escocia',   'SCO', '🏴',   'C');

-- Grupo D
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Estados Unidos', 'USA', '🇺🇸', 'D'),
    ('Paraguay',       'PAR', '🇵🇾', 'D'),
    ('Australia',      'AUS', '🇦🇺', 'D'),
    ('Turquía',        'TUR', '🇹🇷', 'D');

-- Grupo E
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Alemania',        'GER', '🇩🇪', 'E'),
    ('Curazao',         'CUW', '🇨🇼', 'E'),
    ('Costa de Marfil', 'CIV', '🇨🇮', 'E'),
    ('Ecuador',         'ECU', '🇪🇨', 'E');

-- Grupo F
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Países Bajos', 'NED', '🇳🇱', 'F'),
    ('Japón',        'JPN', '🇯🇵', 'F'),
    ('Suecia',       'SWE', '🇸🇪', 'F'),
    ('Túnez',        'TUN', '🇹🇳', 'F');

-- Grupo G
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Bélgica',       'BEL', '🇧🇪', 'G'),
    ('Egipto',        'EGY', '🇪🇬', 'G'),
    ('Irán',          'IRN', '🇮🇷', 'G'),
    ('Nueva Zelanda', 'NZL', '🇳🇿', 'G');

-- Grupo H
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('España',       'ESP', '🇪🇸', 'H'),
    ('Cabo Verde',   'CPV', '🇨🇻', 'H'),
    ('Arabia Saudí', 'KSA', '🇸🇦', 'H'),
    ('Uruguay',      'URU', '🇺🇾', 'H');

-- Grupo I
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Francia',  'FRA', '🇫🇷', 'I'),
    ('Senegal',  'SEN', '🇸🇳', 'I'),
    ('Irak',     'IRQ', '🇮🇶', 'I'),
    ('Noruega',  'NOR', '🇳🇴', 'I');

-- Grupo J
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Argentina', 'ARG', '🇦🇷', 'J'),
    ('Argelia',   'ALG', '🇩🇿', 'J'),
    ('Austria',   'AUT', '🇦🇹', 'J'),
    ('Jordania',  'JOR', '🇯🇴', 'J');

-- Grupo K
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Portugal',   'POR', '🇵🇹', 'K'),
    ('RD Congo',   'COD', '🇨🇩', 'K'),
    ('Uzbekistán', 'UZB', '🇺🇿', 'K'),
    ('Colombia',   'COL', '🇨🇴', 'K');

-- Grupo L
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('Inglaterra', 'ENG', '🏴',   'L'),
    ('Croacia',    'CRO', '🇭🇷', 'L'),
    ('Ghana',      'GHA', '🇬🇭', 'L'),
    ('Panamá',     'PAN', '🇵🇦', 'L');

-- -------------------------------------------------------
-- Fase de grupos - 72 partidos (horarios en UTC)
-- IDs por orden de inserción:
--  1 México          2 Sudáfrica        3 Corea del Sur   4 Chequia
--  5 Canadá          6 Bosnia y Herz.   7 Qatar           8 Suiza
--  9 Brasil         10 Marruecos       11 Haití          12 Escocia
-- 13 Estados Unidos 14 Paraguay        15 Australia      16 Turquía
-- 17 Alemania       18 Curazao         19 Costa de Marfil 20 Ecuador
-- 21 Países Bajos   22 Japón           23 Suecia         24 Túnez
-- 25 Bélgica        26 Egipto          27 Irán           28 Nueva Zelanda
-- 29 España         30 Cabo Verde      31 Arabia Saudí   32 Uruguay
-- 33 Francia        34 Senegal         35 Irak           36 Noruega
-- 37 Argentina      38 Argelia         39 Austria        40 Jordania
-- 41 Portugal       42 RD Congo        43 Uzbekistán     44 Colombia
-- 45 Inglaterra     46 Croacia         47 Ghana          48 Panamá
-- -------------------------------------------------------

-- Jornada 1
INSERT INTO matches (home_team_id, away_team_id, kickoff_at, stage, status, venue) VALUES
    ( 1,  2, '2026-06-11 19:00:00', 'GROUP', 'SCHEDULED', 'Ciudad de México'),    -- México vs Sudáfrica
    ( 3,  4, '2026-06-12 02:00:00', 'GROUP', 'SCHEDULED', 'Zapopan'),             -- Corea del Sur vs Chequia
    ( 5,  6, '2026-06-12 19:00:00', 'GROUP', 'SCHEDULED', 'Toronto'),             -- Canadá vs Bosnia y Herzegovina
    (13, 14, '2026-06-13 01:00:00', 'GROUP', 'SCHEDULED', 'Los Ángeles'),         -- Estados Unidos vs Paraguay
    ( 7,  8, '2026-06-13 19:00:00', 'GROUP', 'SCHEDULED', 'Santa Clara'),         -- Qatar vs Suiza
    ( 9, 10, '2026-06-13 22:00:00', 'GROUP', 'SCHEDULED', 'Nueva Jersey'),        -- Brasil vs Marruecos
    (11, 12, '2026-06-14 01:00:00', 'GROUP', 'SCHEDULED', 'Foxborough'),          -- Haití vs Escocia
    (15, 16, '2026-06-14 04:00:00', 'GROUP', 'SCHEDULED', 'Vancouver'),           -- Australia vs Turquía
    (17, 18, '2026-06-14 17:00:00', 'GROUP', 'SCHEDULED', 'Houston'),             -- Alemania vs Curazao
    (21, 22, '2026-06-14 20:00:00', 'GROUP', 'SCHEDULED', 'Arlington'),           -- Países Bajos vs Japón
    (19, 20, '2026-06-14 23:00:00', 'GROUP', 'SCHEDULED', 'Filadelfia'),          -- Costa de Marfil vs Ecuador
    (23, 24, '2026-06-15 02:00:00', 'GROUP', 'SCHEDULED', 'Guadalupe'),           -- Suecia vs Túnez
    (29, 30, '2026-06-15 16:00:00', 'GROUP', 'SCHEDULED', 'Atlanta'),             -- España vs Cabo Verde
    (25, 26, '2026-06-15 19:00:00', 'GROUP', 'SCHEDULED', 'Seattle'),             -- Bélgica vs Egipto
    (31, 32, '2026-06-15 22:00:00', 'GROUP', 'SCHEDULED', 'Miami'),               -- Arabia Saudí vs Uruguay
    (27, 28, '2026-06-16 01:00:00', 'GROUP', 'SCHEDULED', 'Los Ángeles'),         -- Irán vs Nueva Zelanda
    (33, 34, '2026-06-16 19:00:00', 'GROUP', 'SCHEDULED', 'Nueva Jersey'),        -- Francia vs Senegal
    (35, 36, '2026-06-16 22:00:00', 'GROUP', 'SCHEDULED', 'Foxborough'),          -- Irak vs Noruega
    (37, 38, '2026-06-17 01:00:00', 'GROUP', 'SCHEDULED', 'Kansas City'),         -- Argentina vs Argelia
    (39, 40, '2026-06-17 04:00:00', 'GROUP', 'SCHEDULED', 'Santa Clara'),         -- Austria vs Jordania
    (41, 42, '2026-06-17 17:00:00', 'GROUP', 'SCHEDULED', 'Houston'),             -- Portugal vs RD Congo
    (45, 46, '2026-06-17 20:00:00', 'GROUP', 'SCHEDULED', 'Arlington'),           -- Inglaterra vs Croacia
    (47, 48, '2026-06-17 23:00:00', 'GROUP', 'SCHEDULED', 'Toronto'),             -- Ghana vs Panamá
    (43, 44, '2026-06-18 02:00:00', 'GROUP', 'SCHEDULED', 'Ciudad de México');    -- Uzbekistán vs Colombia

-- Jornada 2
INSERT INTO matches (home_team_id, away_team_id, kickoff_at, stage, status, venue) VALUES
    ( 4,  2, '2026-06-18 16:00:00', 'GROUP', 'SCHEDULED', 'Atlanta'),             -- Chequia vs Sudáfrica
    ( 8,  6, '2026-06-18 19:00:00', 'GROUP', 'SCHEDULED', 'Los Ángeles'),         -- Suiza vs Bosnia y Herzegovina
    ( 5,  7, '2026-06-18 22:00:00', 'GROUP', 'SCHEDULED', 'Vancouver'),           -- Canadá vs Qatar
    ( 1,  3, '2026-06-19 01:00:00', 'GROUP', 'SCHEDULED', 'Zapopan'),             -- México vs Corea del Sur
    (13, 15, '2026-06-19 19:00:00', 'GROUP', 'SCHEDULED', 'Seattle'),             -- Estados Unidos vs Australia
    (12, 10, '2026-06-19 22:00:00', 'GROUP', 'SCHEDULED', 'Foxborough'),          -- Escocia vs Marruecos
    ( 9, 11, '2026-06-20 00:30:00', 'GROUP', 'SCHEDULED', 'Filadelfia'),          -- Brasil vs Haití
    (16, 14, '2026-06-20 03:00:00', 'GROUP', 'SCHEDULED', 'Santa Clara'),         -- Turquía vs Paraguay
    (21, 23, '2026-06-20 17:00:00', 'GROUP', 'SCHEDULED', 'Houston'),             -- Países Bajos vs Suecia
    (17, 19, '2026-06-20 20:00:00', 'GROUP', 'SCHEDULED', 'Toronto'),             -- Alemania vs Costa de Marfil
    (20, 18, '2026-06-21 00:00:00', 'GROUP', 'SCHEDULED', 'Kansas City'),         -- Ecuador vs Curazao
    (24, 22, '2026-06-21 04:00:00', 'GROUP', 'SCHEDULED', 'Guadalupe'),           -- Túnez vs Japón
    (29, 31, '2026-06-21 16:00:00', 'GROUP', 'SCHEDULED', 'Atlanta'),             -- España vs Arabia Saudí
    (25, 27, '2026-06-21 19:00:00', 'GROUP', 'SCHEDULED', 'Los Ángeles'),         -- Bélgica vs Irán
    (32, 30, '2026-06-21 22:00:00', 'GROUP', 'SCHEDULED', 'Miami'),               -- Uruguay vs Cabo Verde
    (28, 26, '2026-06-22 01:00:00', 'GROUP', 'SCHEDULED', 'Vancouver'),           -- Nueva Zelanda vs Egipto
    (37, 39, '2026-06-22 17:00:00', 'GROUP', 'SCHEDULED', 'Arlington'),           -- Argentina vs Austria
    (33, 35, '2026-06-22 21:00:00', 'GROUP', 'SCHEDULED', 'Filadelfia'),          -- Francia vs Irak
    (36, 34, '2026-06-23 00:00:00', 'GROUP', 'SCHEDULED', 'Toronto'),             -- Noruega vs Senegal
    (40, 38, '2026-06-23 03:00:00', 'GROUP', 'SCHEDULED', 'Santa Clara'),         -- Jordania vs Argelia
    (41, 43, '2026-06-23 17:00:00', 'GROUP', 'SCHEDULED', 'Houston'),             -- Portugal vs Uzbekistán
    (45, 47, '2026-06-23 20:00:00', 'GROUP', 'SCHEDULED', 'Foxborough'),          -- Inglaterra vs Ghana
    (48, 46, '2026-06-23 23:00:00', 'GROUP', 'SCHEDULED', 'Foxborough'),          -- Panamá vs Croacia
    (44, 42, '2026-06-24 02:00:00', 'GROUP', 'SCHEDULED', 'Zapopan');             -- Colombia vs RD Congo

-- Jornada 3 (partidos simultáneos por grupo)
INSERT INTO matches (home_team_id, away_team_id, kickoff_at, stage, status, venue) VALUES
    ( 6,  7, '2026-06-24 19:00:00', 'GROUP', 'SCHEDULED', 'Seattle'),             -- Bosnia y Herzegovina vs Qatar
    ( 8,  5, '2026-06-24 19:00:00', 'GROUP', 'SCHEDULED', 'Vancouver'),           -- Suiza vs Canadá
    (12,  9, '2026-06-24 22:00:00', 'GROUP', 'SCHEDULED', 'Miami'),               -- Escocia vs Brasil
    (10, 11, '2026-06-25 01:00:00', 'GROUP', 'SCHEDULED', 'Atlanta'),             -- Marruecos vs Haití
    ( 2,  3, '2026-06-25 01:00:00', 'GROUP', 'SCHEDULED', 'Guadalupe'),           -- Sudáfrica vs Corea del Sur
    ( 4,  1, '2026-06-25 01:00:00', 'GROUP', 'SCHEDULED', 'Ciudad de México'),    -- Chequia vs México
    (20, 17, '2026-06-25 20:00:00', 'GROUP', 'SCHEDULED', 'Nueva Jersey'),        -- Ecuador vs Alemania
    (18, 19, '2026-06-25 20:00:00', 'GROUP', 'SCHEDULED', 'Filadelfia'),          -- Curazao vs Costa de Marfil
    (22, 23, '2026-06-25 23:00:00', 'GROUP', 'SCHEDULED', 'Arlington'),           -- Japón vs Suecia
    (24, 21, '2026-06-25 23:00:00', 'GROUP', 'SCHEDULED', 'Kansas City'),         -- Túnez vs Países Bajos
    (16, 13, '2026-06-26 02:00:00', 'GROUP', 'SCHEDULED', 'Los Ángeles'),         -- Turquía vs Estados Unidos
    (14, 15, '2026-06-26 02:00:00', 'GROUP', 'SCHEDULED', 'Santa Clara'),         -- Paraguay vs Australia
    (34, 35, '2026-06-26 19:00:00', 'GROUP', 'SCHEDULED', 'Toronto'),             -- Senegal vs Irak
    (36, 33, '2026-06-26 19:00:00', 'GROUP', 'SCHEDULED', 'Foxborough'),          -- Noruega vs Francia
    (32, 29, '2026-06-27 00:00:00', 'GROUP', 'SCHEDULED', 'Zapopan'),             -- Uruguay vs España
    (30, 31, '2026-06-27 00:00:00', 'GROUP', 'SCHEDULED', 'Houston'),             -- Cabo Verde vs Arabia Saudí
    (28, 25, '2026-06-27 03:00:00', 'GROUP', 'SCHEDULED', 'Vancouver'),           -- Nueva Zelanda vs Bélgica
    (26, 27, '2026-06-27 03:00:00', 'GROUP', 'SCHEDULED', 'Seattle'),             -- Egipto vs Irán
    (46, 47, '2026-06-27 21:00:00', 'GROUP', 'SCHEDULED', 'Filadelfia'),          -- Croacia vs Ghana
    (48, 45, '2026-06-27 21:00:00', 'GROUP', 'SCHEDULED', 'Nueva Jersey'),        -- Panamá vs Inglaterra
    (44, 41, '2026-06-27 23:30:00', 'GROUP', 'SCHEDULED', 'Miami'),               -- Colombia vs Portugal
    (42, 43, '2026-06-27 23:30:00', 'GROUP', 'SCHEDULED', 'Atlanta'),             -- RD Congo vs Uzbekistán
    (40, 37, '2026-06-28 02:00:00', 'GROUP', 'SCHEDULED', 'Arlington'),           -- Jordania vs Argentina
    (38, 39, '2026-06-28 02:00:00', 'GROUP', 'SCHEDULED', 'Kansas City');         -- Argelia vs Austria
