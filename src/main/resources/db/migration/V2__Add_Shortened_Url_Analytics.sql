CREATE TABLE shortened_url_analytics
(
   id          VARCHAR(100)                              NOT NULL,
   click_count NUMERIC DEFAULT 0                         NOT NULL,
   CONSTRAINT pk_shortened_url_analytics PRIMARY KEY (id)
);

ALTER TABLE shortened_urls DROP COLUMN IF EXISTS click_count;