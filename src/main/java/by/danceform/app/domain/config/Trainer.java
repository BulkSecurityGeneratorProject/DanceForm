package by.danceform.app.domain.config;


import by.danceform.app.domain.AbstractEntity;
import by.danceform.app.domain.INamedEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

/**
 * A Trainer.
 */
@Entity
@Table(name = "trainer")
public class Trainer extends AbstractEntity<Long> implements INamedEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "surname", length = 32, nullable = false)
    private String surname;

    @Column(name = "visible")
    private Boolean visible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Trainer{" +
               "id=" + id +
               ", name='" + name + "'" +
               ", surname='" + surname + "'" +
               ", visible='" + visible + "'" +
               '}';
    }
}
