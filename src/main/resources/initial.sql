CREATE TABLE IF NOT EXISTS books (
    id serial primary key,
    name varchar(255),
    author varchar(255),
    pages int
)