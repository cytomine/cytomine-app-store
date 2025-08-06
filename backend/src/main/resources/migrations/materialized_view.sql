
create materialized view search as
SELECT t.name,
       t.namespace,
       t.name_short,
       t.description,
       t.image_name,
       t.version,
       a.first_name,
       a.last_name,
       a.organization,
       a.email,
       to_tsvector('english',
                   COALESCE(t.name, '') || ' ' ||
                   COALESCE(t.namespace, '') || ' ' ||
                   COALESCE(t.name_short, '') || ' ' ||
                   COALESCE(t.description, '') || ' ' ||
                   COALESCE(t.image_name, '') || ' ' ||
                   COALESCE(t.version, '') || ' ' ||
                   COALESCE(a.first_name, '') || ' ' ||
                   COALESCE(a.last_name, '') || ' ' ||
                   COALESCE(a.organization, '') || ' ' ||
                   COALESCE(a.email, '')) AS search_vector
FROM task t
         JOIN task_authors ta ON t.identifier = ta.task_identifier
         JOIN author a ON ta.authors_id = a.id;

comment on materialized view search is 'for search';

alter materialized view search owner to appstore;

CREATE INDEX idx_search_vector ON search USING GIN(search_vector);

