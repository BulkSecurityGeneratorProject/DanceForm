package by.danceform.app.service.competition;

import by.danceform.app.converter.competition.CompetitionCategoryConverter;
import by.danceform.app.domain.competition.CompetitionCategory;
import by.danceform.app.domain.config.DanceClass;
import by.danceform.app.domain.config.enums.DanceCategoryEnum;
import by.danceform.app.dto.competition.CompetitionCategoryDTO;
import by.danceform.app.dto.couple.RegisteredCoupleDTO;
import by.danceform.app.repository.competition.CompetitionCategoryRepository;
import by.danceform.app.repository.config.DanceClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CompetitionCategory.
 */
@Service
@Transactional
public class CompetitionCategoryService {

    private final Logger log = LoggerFactory.getLogger(CompetitionCategoryService.class);

    @Inject
    private CompetitionCategoryRepository competitionCategoryRepository;

    @Inject
    private CompetitionCategoryConverter competitionCategoryConverter;

    @Inject
    private DanceClassRepository danceClassRepository;

    /**
     * Save a competitionCategory.
     *
     * @param competitionCategoryDTO the entity to save
     * @return the persisted entity
     */
    public CompetitionCategoryDTO save(CompetitionCategoryDTO competitionCategoryDTO) {
        log.debug("Request to save CompetitionCategory : {}", competitionCategoryDTO);
        CompetitionCategory competitionCategory = competitionCategoryConverter.convertToEntity(competitionCategoryDTO);
        competitionCategory = competitionCategoryRepository.save(competitionCategory);
        CompetitionCategoryDTO result = competitionCategoryConverter.convertToDto(competitionCategory);
        return result;
    }

    /**
     * Get all the competitionCategories.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CompetitionCategoryDTO> findAll() {
        log.debug("Request to get all CompetitionCategories");
        List<CompetitionCategoryDTO> result = competitionCategoryRepository.findAllWithEagerRelationships()
            .stream()
            .map(competitionCategoryConverter::convertToDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     * Get all the competitionCategories.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CompetitionCategoryDTO> findByCompetitionId(Long competitionId) {
        log.debug("Request to get all CompetitionCategories");
        List<CompetitionCategoryDTO> result = competitionCategoryRepository.findAllByCompetitionId(competitionId)
            .stream()
            .map(competitionCategoryConverter::convertToDto)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     * Get one competitionCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CompetitionCategoryDTO findOne(Long id) {
        log.debug("Request to get CompetitionCategory : {}", id);
        CompetitionCategory competitionCategory = competitionCategoryRepository.findOneWithEagerRelationships(id);
        CompetitionCategoryDTO competitionCategoryDTO = competitionCategoryConverter.convertToDto(competitionCategory);
        return competitionCategoryDTO;
    }

    /**
     * Delete the  competitionCategory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CompetitionCategory : {}", id);
        competitionCategoryRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<CompetitionCategoryDTO> findAvailableByCompetitionId(RegisteredCoupleDTO registeredCoupleDTO,
                                                                     Long competitionId) {
        log.debug("Request to get CompetitionCategory : {}", competitionId);
        List<CompetitionCategory> allCategories = competitionCategoryRepository.findAllWithEagerRelationships();
        Set<CompetitionCategory> availableCategories = new HashSet<>();
        for(CompetitionCategory existingCategory : allCategories) {
            if(existingCategory.isCheckMaxAge()) {

            }
            if(existingCategory.isCheckMaxAge()) {

            }
            if(checkDanceClasses(existingCategory.getDanceClasses(),
                Objects.equals(existingCategory.getDanceCategory().getId(), DanceCategoryEnum.LA.getValue()) ?
                    registeredCoupleDTO.getPartner1DanceClassLA().getId() :
                    registeredCoupleDTO.getPartner1DanceClassST().getId(),
                Objects.equals(existingCategory.getDanceCategory().getId(), DanceCategoryEnum.LA.getValue()) ?
                    registeredCoupleDTO.getPartner2DanceClassLA().getId() :
                    registeredCoupleDTO.getPartner2DanceClassST().getId())) {
                availableCategories.add(existingCategory);
            }
        }
        return competitionCategoryConverter.convertToDtos(new ArrayList<>(availableCategories));
    }

    private boolean checkDanceClasses(Set<DanceClass> categoryClasses,
                                      Long partner1DanceClassId,
                                      Long partner2DanceClassId) {
        List<Long> categoryClassesIds = categoryClasses.stream()
            .map(categoryClass -> categoryClass.getId())
            .collect(Collectors.toList());
        DanceClass partner1Class = danceClassRepository.findOne(partner1DanceClassId);
        DanceClass partner2Class = danceClassRepository.findOne(partner2DanceClassId);
        List<Long> partner1AvailableClasses = new ArrayList<>();
        List<Long> partner2AvailableClasses = new ArrayList<>();
        for(Long categoryClassId : categoryClassesIds) {
            if(Objects.equals(categoryClassId, partner1Class.getId())) {
                partner1AvailableClasses.add(categoryClassId);
            }
            if(Objects.equals(categoryClassId, partner2Class.getId())) {
                partner2AvailableClasses.add(categoryClassId);
            }
        }
        if(!partner1AvailableClasses.isEmpty() && !partner2AvailableClasses.isEmpty()) {
            return true;
        }
        return false;
    }

}
