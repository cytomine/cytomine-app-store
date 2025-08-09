package be.cytomine.appstore.repositories;

import be.cytomine.appstore.models.Search;
import be.cytomine.appstore.models.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import be.cytomine.appstore.utils.TaskUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SearchRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SearchRepository searchRepository;

    @BeforeEach
    public void setUp() {
        // Create sample data for testing
        Task task1 = TaskUtils.createTask(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"),
            "Image Analysis App", "uliege.be", "ia-app", "An app for image analysis.",
            "cytomine/image-analysis:1.0.0", "1.0.0", "John", "Doe", "University of Liege",
            "john@uliege.be");
        Task task2 = TaskUtils.createTask(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e2"),
            "Data Annotation Tool", "cytomine", "da-tool", "A tool for data annotation.",
            "cytomine/data-annotation:2.0.0", "2.0.0", "Jane", "Smith", "Cytomine",
            "jane@cytomine.be");
        Task task3 = TaskUtils.createTask(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e3"),
            "Report Generation Engine", "reports", "rg-engine", "Generates PDF reports.",
            "cytomine/report-gen:3.0.0", "3.0.0", "Mike", "Jones", "Reports Inc.",
            "mike@reports.org");
        Task task4 = TaskUtils.createTask(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e4"),
            "image_analysis_tool", "uliege", "iat", "This tool is for analyzing images.",
            "cytomine/image-analysis:1.1.0", "1.1.0", "John", "Doe", "Uliege", "john@uliege.be");
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);
        entityManager.persist(task4);
        entityManager.flush();

        searchRepository.refreshSearchViewConcurrently();
    }

    @Test
    public void whenFullTextQueryMatches_shouldReturnCorrectResults() {
        // Test a full-text query that matches multiple items, and check the ordering
        List<Search> results = searchRepository.findByAdvancedSearch("image&analysis", "image analysis");

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getIdentifier()).isEqualTo(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")); // Should be first due to higher rank
        assertThat(results.get(1).getIdentifier()).isEqualTo(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e4"));
    }

    @Test
    public void whenFuzzyQueryMatches_shouldReturnCorrectResults() {
        // Test a query with a typo that should be caught by fuzzy search
        List<Search> results = searchRepository.findByAdvancedSearch("anlyis", "anlyis");

        assertThat(results).hasSize(3);
        assertThat(results.get(0).getIdentifier()).isEqualTo(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e2")); // Should be first due to higher rank
        assertThat(results.get(1).getIdentifier()).isEqualTo(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e4"));
        assertThat(results.get(2).getIdentifier()).isEqualTo(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851"));
    }

    @Test
    public void whenSubstringQueryMatches_shouldReturnCorrectResults() {
        // Test a query for a partial word or substring
        List<Search> results = searchRepository.findByAdvancedSearch("ulieg", "ulieg");

        assertThat(results).hasSize(2);
        assertThat(results.get(1).getIdentifier()).isEqualTo(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")); // Should be first due to higher rank
        assertThat(results.get(0).getIdentifier()).isEqualTo(UUID.fromString("76e5c8de-8ec4-4c6c-9b47-ec1a563e03e4"));
    }
}