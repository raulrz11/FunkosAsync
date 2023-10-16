DROP TABLE IF EXISTS FUNKOS;
CREATE TABLE IF NOT EXISTS FUNKOS  (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     uuid UUID NOT NULL,
                                     myId INTEGER,
    nombre VARCHAR(255) NOT NULL,
    modelo TEXT CHECK (modelo IN ('MARVEL', 'DISNEY', 'ANIME', 'OTROS')),
    precio DOUBLE NOT NULL,
    fecha_lanzamiento DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );