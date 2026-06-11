-- =====================================================================
-- La Porra Global - Datos semilla
-- Nombres genéricos de selecciones (sin marcas ni assets oficiales)
-- =====================================================================

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_BAR_OWNER');

-- Selecciones de ejemplo (códigos ISO-3166 alpha-3)
INSERT INTO teams (name, code, flag_emoji, group_name) VALUES
    ('España', 'ESP', '🇪🇸', 'A'),
    ('Argentina', 'ARG', '🇦🇷', 'A'),
    ('México', 'MEX', '🇲🇽', 'A'),
    ('Japón', 'JPN', '🇯🇵', 'A'),
    ('Francia', 'FRA', '🇫🇷', 'B'),
    ('Brasil', 'BRA', '🇧🇷', 'B'),
    ('Estados Unidos', 'USA', '🇺🇸', 'B'),
    ('Marruecos', 'MAR', '🇲🇦', 'B'),
    ('Alemania', 'DEU', '🇩🇪', 'C'),
    ('Inglaterra', 'GBR', '🏴', 'C'),
    ('Uruguay', 'URY', '🇺🇾', 'C'),
    ('Senegal', 'SEN', '🇸🇳', 'C'),
    ('Portugal', 'PRT', '🇵🇹', 'D'),
    ('Países Bajos', 'NLD', '🇳🇱', 'D'),
    ('Colombia', 'COL', '🇨🇴', 'D'),
    ('Corea del Sur', 'KOR', '🇰🇷', 'D');

-- Partidos de ejemplo de la fase de grupos (junio-julio 2026)
INSERT INTO matches (home_team_id, away_team_id, kickoff_at, stage, status, venue) VALUES
    (1, 2, '2026-06-12 18:00:00', 'GROUP', 'SCHEDULED', 'Estadio Norte'),
    (3, 4, '2026-06-12 21:00:00', 'GROUP', 'SCHEDULED', 'Estadio Sur'),
    (5, 6, '2026-06-13 18:00:00', 'GROUP', 'SCHEDULED', 'Arena Central'),
    (7, 8, '2026-06-13 21:00:00', 'GROUP', 'SCHEDULED', 'Estadio del Lago'),
    (9, 10, '2026-06-14 18:00:00', 'GROUP', 'SCHEDULED', 'Estadio Norte'),
    (11, 12, '2026-06-14 21:00:00', 'GROUP', 'SCHEDULED', 'Estadio Sur'),
    (13, 14, '2026-06-15 18:00:00', 'GROUP', 'SCHEDULED', 'Arena Central'),
    (15, 16, '2026-06-15 21:00:00', 'GROUP', 'SCHEDULED', 'Estadio del Lago');
