# TicketFlow REST API

Base URL locale: `http://localhost:8080`

Le rotte protette richiedono:

```http
Authorization: Bearer <JWT>
```

## Autenticazione

| Metodo | Endpoint | Accesso | Descrizione |
|---|---|---|---|
| POST | `/auth/register` | Pubblico | Registra un nuovo UTENTE |
| POST | `/auth/login` | Pubblico | Autentica e restituisce il JWT |

### Registrazione
```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "email": "mario@example.com",
  "password": "Password123!"
}
```

### Login
```json
{
  "email": "mario@example.com",
  "password": "Password123!"
}
```

## Ticket

| Metodo | Endpoint | Ruolo | Descrizione |
|---|---|---|---|
| POST | `/tickets` | UTENTE | Crea un ticket |
| GET | `/tickets/mine` | Autenticato | Restituisce i ticket dell'utente autenticato |
| GET | `/tickets/open` | OPERATORE | Restituisce i ticket OPEN |
| PATCH | `/tickets/{id}/take` | OPERATORE | Prende in carico un ticket |
| GET | `/tickets/assigned` | OPERATORE | Restituisce i ticket assegnati all'operatore |
| PATCH | `/tickets/{id}/close` | OPERATORE | Chiude un ticket assegnato all'operatore |
| GET | `/tickets/{id}/history` | OPERATORE | Visualizza lo storico degli stati |
| POST | `/tickets/{id}/comments` | UTENTE / OPERATORE | Aggiunge un commento se autorizzato sul ticket |
| GET | `/tickets/{id}/comments` | UTENTE / OPERATORE | Legge i commenti se autorizzato sul ticket |

### Creazione ticket
```json
{
  "titolo": "Errore accesso applicazione",
  "descrizione": "Non riesco ad accedere alla piattaforma",
  "priorita": "MEDIUM",
  "categoriaId": 1
}
```

Priorità supportate: `LOW`, `MEDIUM`, `HIGH`.

Stati: `OPEN`, `IN_PROGRESS`, `CLOSED`.

### Nuovo commento
```json
{
  "testo": "Sto verificando il problema."
}
```

## Errori

La validazione dei DTO produce `400 Bad Request`; le risorse inesistenti producono `404 Not Found`; le operazioni non consentite dalla logica applicativa producono `403 Forbidden`. Spring Security applica inoltre i vincoli di ruolo alle rotte protette.
