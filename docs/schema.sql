CREATE DATABASE IF NOT EXISTS ticket_flow;
USE ticket_flow;

CREATE TABLE utenti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    cognome VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    ruolo VARCHAR(30) NOT NULL,
    data_registrazione DATETIME NOT NULL
);

CREATE TABLE categorie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descrizione VARCHAR(255)
);

CREATE TABLE ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titolo VARCHAR(150) NOT NULL,
    descrizione TEXT,
    stato VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    priorita VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    data_creazione DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_ultimo_aggiornamento DATETIME,
    autore_id BIGINT NOT NULL,
    operatore_id BIGINT,
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_ticket_autore FOREIGN KEY (autore_id) REFERENCES utenti(id) ON DELETE RESTRICT,
    CONSTRAINT fk_ticket_operatore FOREIGN KEY (operatore_id) REFERENCES utenti(id) ON DELETE RESTRICT,
    CONSTRAINT fk_ticket_categoria FOREIGN KEY (categoria_id) REFERENCES categorie(id)
);

CREATE TABLE commenti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    testo VARCHAR(255) NOT NULL,
    data_creazione DATETIME,
    autore_id BIGINT NOT NULL,
    ticket_id BIGINT NOT NULL,
    CONSTRAINT fk_commento_autore FOREIGN KEY (autore_id) REFERENCES utenti(id),
    CONSTRAINT fk_commento_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE
);

CREATE TABLE storico_stato (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    stato_precedente VARCHAR(20),
    stato_nuovo VARCHAR(20),
    data_modifica DATETIME,
    modificato_da BIGINT NOT NULL,
    CONSTRAINT fk_storico_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
    CONSTRAINT fk_storico_utente FOREIGN KEY (modificato_da) REFERENCES utenti(id)
);

INSERT INTO categorie (nome, descrizione)
VALUES ('Tecnico', 'Problemi tecnici relativi alla piattaforma');
