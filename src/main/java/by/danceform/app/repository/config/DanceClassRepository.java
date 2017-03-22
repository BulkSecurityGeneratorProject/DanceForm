package by.danceform.app.repository.config;

import by.danceform.app.domain.config.DanceClass;
import by.danceform.app.repository.AbstractEntityRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the DanceClass entity.
 */
public interface DanceClassRepository extends AbstractEntityRepository<DanceClass, Long> {

    static final Long DEFAULT_DANCE_CLASS_ID = 1L;

    @Query("SELECT dc FROM DanceClass dc WHERE dc.id = 1")
    DanceClass findDefault();
}
