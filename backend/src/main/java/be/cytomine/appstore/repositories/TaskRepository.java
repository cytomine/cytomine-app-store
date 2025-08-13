package be.cytomine.appstore.repositories;

import java.util.UUID;

import be.cytomine.appstore.models.task.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Task findByNamespaceAndVersion(String namespace, String version);

    @Modifying
    @Transactional
    @Query("DELETE FROM Task")
    void deleteAllTasks();
}
