package by.danceform.app.domain.competition;

import by.danceform.app.domain.AbstractEntity;
import by.danceform.app.domain.INamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * A Competition.
 */
@Entity
@Table(name = "competition")
public class Competition extends AbstractEntity<Long> implements INamedEntity {

    private static final long serialVersionUID = -146024457330746645L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_visible")
    private boolean visible;

    @Column(name = "organizer")
    private String organizer;

    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "location", length = 512, nullable = false)
    private String location;

    @Column(name = "banner_image_id")
    private Long bannerImageId;

    @Column(name = "details_document_id")
    private Long detailsDocumentId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getDetailsDocumentId() {
        return detailsDocumentId;
    }

    public void setDetailsDocumentId(Long detailsDocumentId) {
        this.detailsDocumentId = detailsDocumentId;
    }

    public Long getBannerImageId() {
        return bannerImageId;
    }

    public void setBannerImageId(Long bannerImageId) {
        this.bannerImageId = bannerImageId;
    }

    @Override
    public String toString() {
        return "Competition{" +
               "id=" +
               id +
               ", name='" +
               name +
               "'" +
               ", startDate='" +
               startDate +
               "'" +
               ", endDate='" +
               endDate +
               "'" +
               ", visible='" +
               visible +
               "'" +
               ", organizer='" +
               organizer +
               "'" +
               ", location='" +
               location +
               "'" +
               '}';
    }
}
