-- liquibase formatted sql

-- changeset siddig-hamed:1754556433595-1
CREATE TABLE author (id UUID NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_date TIMESTAMP WITHOUT TIME ZONE, vrsn BIGINT, email VARCHAR(255), first_name VARCHAR(255), is_contact BOOLEAN NOT NULL, last_name VARCHAR(255), organization VARCHAR(255), CONSTRAINT author_pkey PRIMARY KEY (id));

-- changeset siddig-hamed:1754556433595-2
CREATE TABLE parameter (id UUID NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_date TIMESTAMP WITHOUT TIME ZONE, vrsn BIGINT, default_value VARCHAR(255), description VARCHAR(255), display_name VARCHAR(255), name VARCHAR(255), optional BOOLEAN NOT NULL, parameter_type SMALLINT, derived_from UUID, type_identifier UUID, CONSTRAINT parameter_pkey PRIMARY KEY (id));

-- changeset siddig-hamed:1754556433595-3
CREATE TABLE match (id UUID NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_date TIMESTAMP WITHOUT TIME ZONE, vrsn BIGINT, check_time SMALLINT, matched_id UUID NOT NULL, matching_id UUID NOT NULL, CONSTRAINT match_pkey PRIMARY KEY (id));

-- changeset siddig-hamed:1754556433595-4
CREATE TABLE task (identifier UUID NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_date TIMESTAMP WITHOUT TIME ZONE, vrsn BIGINT, cpus INTEGER NOT NULL, description VARCHAR(255), descriptor_file VARCHAR(255), gpus INTEGER NOT NULL, image_name VARCHAR(255), input_folder VARCHAR(255), name VARCHAR(255), name_short VARCHAR(255), namespace VARCHAR(255), output_folder VARCHAR(255), ram VARCHAR(255), storage_reference VARCHAR(255), version VARCHAR(255), CONSTRAINT task_pkey PRIMARY KEY (identifier));

-- changeset siddig-hamed:1754556433595-5
CREATE TABLE task_matches (task_identifier UUID NOT NULL, matches_id UUID NOT NULL);

-- changeset siddig-hamed:1754556433595-6
CREATE TABLE task_parameters (task_identifier UUID NOT NULL, parameters_id UUID NOT NULL, CONSTRAINT task_parameters_pkey PRIMARY KEY (task_identifier, parameters_id));

-- changeset siddig-hamed:1754556433595-7
CREATE TABLE task_authors (task_identifier UUID NOT NULL, authors_id UUID NOT NULL, CONSTRAINT task_authors_pkey PRIMARY KEY (task_identifier, authors_id));

-- changeset siddig-hamed:1754556433595-8
CREATE TABLE type (identifier UUID NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_date TIMESTAMP WITHOUT TIME ZONE, vrsn BIGINT, charset VARCHAR(255), id VARCHAR(255), CONSTRAINT type_pkey PRIMARY KEY (identifier));

-- changeset siddig-hamed:1754556433595-9
ALTER TABLE task_authors ADD CONSTRAINT fkmg1yvs01reo9scucsv7gcse5e FOREIGN KEY (authors_id) REFERENCES author (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-10
ALTER TABLE parameter ADD CONSTRAINT ukgbygxrnu8t0ul35ir55wv2fak UNIQUE (type_identifier);

-- changeset siddig-hamed:1754556433595-11
ALTER TABLE match ADD CONSTRAINT fk51xp1kaunfdsod4qd3xr1a9bd FOREIGN KEY (matched_id) REFERENCES parameter (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-12
ALTER TABLE match ADD CONSTRAINT fkn884yn80s5wy25yk7r20r35li FOREIGN KEY (matching_id) REFERENCES parameter (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-13
ALTER TABLE task ADD CONSTRAINT ukaooh6s7q1faei0rr9eglv1g6k UNIQUE (namespace, version);

-- changeset siddig-hamed:1754556433595-14
ALTER TABLE task_matches ADD CONSTRAINT fkbuc8jxortlcb2ny7ls07b3885 FOREIGN KEY (task_identifier) REFERENCES task (identifier) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-15
ALTER TABLE task_matches ADD CONSTRAINT fksaak2r6s6aijtqkl241e8pj5m FOREIGN KEY (matches_id) REFERENCES match (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-16
CREATE UNIQUE INDEX uk3te3osiwh9t0agnwi51db7vac ON task_matches(matches_id);

-- changeset siddig-hamed:1754556433595-17
ALTER TABLE task_parameters ADD CONSTRAINT fkdp95bwwoag4s3jfvrgwifaokm FOREIGN KEY (task_identifier) REFERENCES task (identifier) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-18
ALTER TABLE task_parameters ADD CONSTRAINT fkxv6v0i4y262vrmp31xnyesi FOREIGN KEY (parameters_id) REFERENCES parameter (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-19
ALTER TABLE task_parameters ADD CONSTRAINT ukkilg1x1lj4f3m4eemdlmjw5rb UNIQUE (parameters_id);

-- changeset siddig-hamed:1754556433595-20
ALTER TABLE task_authors ADD CONSTRAINT fkgh9w5wsdjm0o2ql35aoqsv0u7 FOREIGN KEY (task_identifier) REFERENCES task (identifier) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-21
ALTER TABLE task_authors ADD CONSTRAINT uklhe0h4r63ip5yms54ctegedyd UNIQUE (authors_id);

-- changeset siddig-hamed:1754556433595-22
ALTER TABLE parameter ADD CONSTRAINT fk4x2umyshp6msj9j8qgco5lia2 FOREIGN KEY (derived_from) REFERENCES parameter (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset siddig-hamed:1754556433595-23
ALTER TABLE parameter ADD CONSTRAINT fkrex9exdvhyhfb98mg9wng32lv FOREIGN KEY (type_identifier) REFERENCES type (identifier) ON UPDATE NO ACTION ON DELETE NO ACTION;

