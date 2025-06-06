#!/bin/bash
if [ -f ".env" ]; then
  export $(grep -v '^#' .env | xargs)
else
  echo "Error: .env file not found."
  exit 1
fi

: "${SCHEMA:?SCHEMA not set}"
: "${DB_NAME:?DB_NAME not set}"

echo "Initializing database..."

if [[ -f "$DB_NAME" ]]; then
  mv "$DB_NAME" "$DB_NAME.bak"
  echo "Existing database backed up to '$DB_NAME.bak'"
fi

if [[ ! -f "$SCHEMA" ]]; then
    echo "Error: Schema file '$SCHEMA' not found."
    exit 1
fi

if sqlite3 "$DB_NAME" < "$SCHEMA"; then
    echo "Database '$DB_NAME' initialized successfully."
else
    echo "Error: Failed to initialize database."
    exit 1
fi
