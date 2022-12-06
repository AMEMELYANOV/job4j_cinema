CREATE TABLE IF NOT EXISTS sessions (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  description TEXT,
  postername VARCHAR
);

COMMENT ON TABLE sessions IS 'Сеансы';
COMMENT ON COLUMN sessions.id IS 'Идентификатор сеанса';
COMMENT ON COLUMN sessions.name IS 'Наименование сеанса';
COMMENT ON COLUMN sessions.description IS 'Подробное описание сеанса';