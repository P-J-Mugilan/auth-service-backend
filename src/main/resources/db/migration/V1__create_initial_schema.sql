CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       public_id CHAR(36) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20) NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       status VARCHAR(30) NOT NULL,
                       email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                       updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
                           ON UPDATE CURRENT_TIMESTAMP(6),

                       PRIMARY KEY (id),
                       UNIQUE KEY uk_users_public_id (public_id),
                       UNIQUE KEY uk_users_email (email),
                       UNIQUE KEY uk_users_phone_number (phone_number),

                       CONSTRAINT chk_users_status
                           CHECK (status IN ('ACTIVE', 'LOCKED', 'DISABLED', 'PENDING_VERIFICATION'))
);