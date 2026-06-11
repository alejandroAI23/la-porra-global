-- =====================================================================
-- La Porra Global - Esquema inicial
-- Plataforma no oficial de predicciones. Sin apuestas con dinero real.
-- =====================================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE TABLE IF NOT EXISTS roles (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(30) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name  VARCHAR(80),
    avatar_url    VARCHAR(255),
    enabled       BIT          NOT NULL DEFAULT 1,
    created_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS teams (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(60) NOT NULL UNIQUE,
    code       VARCHAR(3)  NOT NULL UNIQUE,
    flag_emoji VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    group_name VARCHAR(5)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS matches (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    home_team_id BIGINT       NOT NULL,
    away_team_id BIGINT       NOT NULL,
    kickoff_at   TIMESTAMP(6) NOT NULL,
    stage        VARCHAR(20)  NOT NULL,
    status       VARCHAR(15)  NOT NULL DEFAULT 'SCHEDULED',
    home_score   INT,
    away_score   INT,
    venue        VARCHAR(80),
    CONSTRAINT fk_matches_home FOREIGN KEY (home_team_id) REFERENCES teams (id),
    CONSTRAINT fk_matches_away FOREIGN KEY (away_team_id) REFERENCES teams (id),
    INDEX idx_matches_kickoff (kickoff_at),
    INDEX idx_matches_status (status)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS leagues (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(80)  NOT NULL,
    description VARCHAR(255),
    invite_code VARCHAR(10)  NOT NULL UNIQUE,
    type        VARCHAR(15)  NOT NULL DEFAULT 'FRIENDS',
    owner_id    BIGINT       NOT NULL,
    max_members INT          NOT NULL DEFAULT 50,
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_leagues_owner FOREIGN KEY (owner_id) REFERENCES users (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS league_members (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    league_id BIGINT       NOT NULL,
    user_id   BIGINT       NOT NULL,
    role      VARCHAR(10)  NOT NULL DEFAULT 'MEMBER',
    joined_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT uq_league_member UNIQUE (league_id, user_id),
    CONSTRAINT fk_lm_league FOREIGN KEY (league_id) REFERENCES leagues (id) ON DELETE CASCADE,
    CONSTRAINT fk_lm_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS predictions (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT       NOT NULL,
    match_id             BIGINT       NOT NULL,
    predicted_home_score INT          NOT NULL,
    predicted_away_score INT          NOT NULL,
    points_awarded       INT,
    created_at           TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at           TIMESTAMP(6),
    CONSTRAINT uq_prediction UNIQUE (user_id, match_id),
    CONSTRAINT fk_pred_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_pred_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ranking_entries (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    league_id        BIGINT       NOT NULL,
    user_id          BIGINT       NOT NULL,
    total_points     INT          NOT NULL DEFAULT 0,
    exact_hits       INT          NOT NULL DEFAULT 0,
    outcome_hits     INT          NOT NULL DEFAULT 0,
    current_position INT,
    updated_at       TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT uq_ranking_entry UNIQUE (league_id, user_id),
    CONSTRAINT fk_re_league FOREIGN KEY (league_id) REFERENCES leagues (id) ON DELETE CASCADE,
    CONSTRAINT fk_re_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_ranking_points (league_id, total_points DESC)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS bars (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(255),
    address     VARCHAR(200)   NOT NULL,
    city        VARCHAR(80),
    latitude    DECIMAL(10, 7),
    longitude   DECIMAL(10, 7),
    phone       VARCHAR(20),
    owner_id    BIGINT         NOT NULL,
    verified    BIT            NOT NULL DEFAULT 0,
    created_at  TIMESTAMP(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_bars_owner FOREIGN KEY (owner_id) REFERENCES users (id),
    INDEX idx_bars_city (city)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS bar_events (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    bar_id      BIGINT       NOT NULL,
    match_id    BIGINT,
    title       VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    starts_at   TIMESTAMP(6) NOT NULL,
    capacity    INT,
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_be_bar FOREIGN KEY (bar_id) REFERENCES bars (id) ON DELETE CASCADE,
    CONSTRAINT fk_be_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE SET NULL,
    INDEX idx_bar_events_starts (bar_id, starts_at)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS subscriptions (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    plan         VARCHAR(15)  NOT NULL DEFAULT 'FREE',
    status       VARCHAR(15)  NOT NULL DEFAULT 'ACTIVE',
    external_ref VARCHAR(100),
    starts_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ends_at      TIMESTAMP(6),
    CONSTRAINT fk_sub_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS notifications (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    type       VARCHAR(25)  NOT NULL,
    title      VARCHAR(120) NOT NULL,
    message    VARCHAR(500),
    is_read    BIT          NOT NULL DEFAULT 0,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_notif_user_read (user_id, is_read)
) ENGINE = InnoDB;
