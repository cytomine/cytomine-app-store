-- 1. create a full-text search extension
CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- 2. create a materialized view for search
create materialized view search as
SELECT t.identifier,
       t.name,
       t.namespace,
       t.name_short,
       t.description,
       t.image_name,
       t.version,
-- 3. aggregate author info
       string_agg(a.first_name, ', ') AS first_names,
       string_agg(a.last_name, ', ') AS last_names,
       string_agg(a.organization, ', ') AS organizations,
       string_agg(a.email, ', ') AS emails,
-- 4. inculde aggregated data in the search index and fuzzy search
       to_tsvector('english',
                   COALESCE(t.name, '') || ' ' ||
                   COALESCE(t.namespace, '') || ' ' ||
                   COALESCE(t.name_short, '') || ' ' ||
                   COALESCE(t.description, '') || ' ' ||
                   COALESCE(t.image_name, '') || ' ' ||
                   COALESCE(t.version, '') || ' ' ||
                   COALESCE(string_agg(a.first_name, ' '), '') || ' ' ||
                   COALESCE(string_agg(a.last_name, ' '), '') || ' ' ||
                   COALESCE(string_agg(a.organization, ' '), '') || ' ' ||
                   COALESCE(string_agg(a.email, ' '), '')) AS search_vector ,
       COALESCE(t.name, '') || ' ' ||
       COALESCE(t.namespace, '') || ' ' ||
       COALESCE(t.name_short, '') || ' ' ||
       COALESCE(t.description, '') || ' ' ||
       COALESCE(t.image_name, '') || ' ' ||
       COALESCE(string_agg(a.first_name, ' '), '') || ' ' ||
       COALESCE(string_agg(a.last_name, ' '), '') || ' ' ||
       COALESCE(string_agg(a.organization, ' '), '') AS fuzzy_search_text
FROM task t
         JOIN task_authors ta ON t.identifier = ta.task_identifier
         JOIN author a ON ta.authors_id = a.id
GROUP BY t.identifier, t.name, t.namespace, t.name_short, t.description, t.image_name, t.version;
-- 5. a description
comment on materialized view search is 'for search';
-- 6. change owner
alter materialized view search owner to appstore;
-- 7. create a generalized inverted index
CREATE INDEX idx_search_vector ON search USING GIN(search_vector);
-- 8. another one for the fuzzy search
CREATE INDEX idx_fuzzy_search ON search USING GIN(fuzzy_search_text gin_trgm_ops);
--9. create a unique index on the identifier to allow for refresh
CREATE UNIQUE INDEX search_identifier_idx ON search (identifier);

