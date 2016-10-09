package by.danceform.app.repository.competition;

import by.danceform.app.domain.competition.CompetitionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the CompetitionCategory entity.
 */
@SuppressWarnings("unused")
public interface CompetitionCategoryRepository extends JpaRepository<CompetitionCategory, Long> {

    @Query(
        "select distinct competitionCategory from CompetitionCategory competitionCategory left join fetch competitionCategory.danceClasses left join fetch competitionCategory.ageCategories")
    List<CompetitionCategory> findAllWithEagerRelationships();

    @Query(
        "select competitionCategory from CompetitionCategory competitionCategory left join fetch competitionCategory.danceClasses left join fetch competitionCategory.ageCategories where competitionCategory.id =:id")
    CompetitionCategory findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        "select competitionCategory from CompetitionCategory competitionCategory left join fetch competitionCategory.danceClasses left join fetch competitionCategory.ageCategories where competitionCategory.competitionId=:competitionId")
    List<CompetitionCategory> findAllByCompetitionId(@Param("competitionId") Long competitionId);

}
