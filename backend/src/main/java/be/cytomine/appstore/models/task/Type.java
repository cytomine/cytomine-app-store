package be.cytomine.appstore.models.task;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import be.cytomine.appstore.models.BaseEntity;

@Entity
@Table(name = "type")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = false)
public class Type extends BaseEntity {
    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID identifier;

    private String id; // as found in the descriptor

    private String charset;

}
