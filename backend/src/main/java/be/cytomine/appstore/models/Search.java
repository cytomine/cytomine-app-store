package be.cytomine.appstore.models;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@Table(name = "search")
@NoArgsConstructor
public class Search implements Serializable {

    @JsonIgnore
    @Id
    private UUID identifier;

    private String name;
    private String namespace;

    @Column(name = "name_short")
    private String nameShort;

    private String description;

    @Column(name = "image_name")
    private String imageName;

    private String version;
    @JsonIgnore
    @Column(name = "first_names")
    private String firstNames;
    @JsonIgnore
    @Column(name = "last_names")
    private String lastNames;
    @JsonIgnore
    private String organizations;
    @JsonIgnore
    private String emails;

}