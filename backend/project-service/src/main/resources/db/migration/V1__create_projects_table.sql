-- V1__create_projects_table.sql
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'ARCHIVED','CLOSED')),
    owner_id UUID NOT NULL,
    owner_full_name varchar(250) NOT NULL,
    assigned_id UUID NOT NULL,
    assigned_full_name varchar(250) NOT NULL,
    start_at TIMESTAMP,
    finish_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);