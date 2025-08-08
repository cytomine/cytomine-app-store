package be.cytomine.appstore.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Entity
@Table(name = "search")
@NoArgsConstructor
public class Search implements Serializable {

    @Id
    private String identifier;

    private String name;
    private String namespace;

    @Column(name = "name_short")
    private String nameShort;

    private String description;

    @Column(name = "image_name")
    private String imageName;

    private String version;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String organization;
    private String email;

}