CREATE TABLE user_sessions
(
    id          uuid           NOT NULL DEFAULT uuid_generate_v4(),
    user_id     uuid           NOT NULL,
    user_agent  varchar        NOT NULL,
    ip          varchar        NOT NULL,
    created_at  timestamptz(0) NOT NULL DEFAULT now(),
    expires_at  timestamptz(0) NOT NULL DEFAULT now(),
    CONSTRAINT user_sessions_pk PRIMARY KEY (id)
);

ALTER TABLE user_sessions
    ADD CONSTRAINT user_sessions_fk_ FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE;