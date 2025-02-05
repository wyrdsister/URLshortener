CREATE TABLE shortened_urls
(
   id          VARCHAR(100)                              NOT NULL,
   url         VARCHAR(500)                              NOT NULL,
   created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
   expired_at  TIMESTAMP WITHOUT TIME ZONE,
   click_count NUMERIC DEFAULT 0                         NOT NULL,
   CONSTRAINT pk_shortened_urls PRIMARY KEY (id)
);