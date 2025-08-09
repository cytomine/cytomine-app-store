package be.cytomine.appstore.repositories;

import java.util.List;
import java.util.UUID;

import be.cytomine.appstore.models.Search;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface SearchRepository extends JpaRepository<Search, UUID> {
    @Query(value =
        "SELECT *  " +
        "FROM search " +
        "WHERE search_vector @@ to_tsquery('english', :fullTextQuery) " +
        "OR word_similarity(:fuzzyQuery, fuzzy_search_text) > 0.2 " +
        "OR fuzzy_search_text ILIKE '%' || :fuzzyQuery || '%'" +
        "ORDER BY ts_rank(search_vector, to_tsquery('english', :fullTextQuery)) DESC " +
        "LIMIT 10",
        nativeQuery = true)
    List<Search> findByAdvancedSearch(@Param("fullTextQuery") String fullTextQuery, @Param("fuzzyQuery") String fuzzyQuery);

    @Transactional
    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY search", nativeQuery = true)
    void refreshSearchViewConcurrently();
}
