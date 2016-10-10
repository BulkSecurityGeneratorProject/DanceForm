package by.danceform.app.converter.config;

import by.danceform.app.converter.AbstractConverter;
import by.danceform.app.domain.config.DanceClass;
import by.danceform.app.dto.config.DanceClassDTO;
import by.danceform.app.repository.config.DanceClassRepository;
import by.danceform.app.web.rest.config.DanceClassResource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by dimonn12 on 07.10.2016.
 */
@Component("danceClassConverter")
public class DanceClassConverter extends AbstractConverter<DanceClass, DanceClassDTO, Long> {

    @Inject
    private DanceClassRepository danceClassRepository;

    @Override
    protected DanceClassDTO convertEntityToDto(DanceClass entity, DanceClassDTO dto) {
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSymbol(entity.getSymbol());
        dto.setTransferScore(entity.getTransferScore());
        dto.setWeight(entity.getWeight());
        if(null != entity.getNextDanceClass()) {
            dto.setNextDanceClassId(entity.getNextDanceClass().getId());
        }
        return dto;
    }

    @Override
    protected DanceClass convertDtoToEntity(DanceClassDTO dto, DanceClass entity) {
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setWeight(dto.getWeight());
        entity.setTransferScore(dto.getTransferScore());
        entity.setSymbol(dto.getSymbol());
        entity.setNextDanceClass(danceClassRepository.findOne(dto.getNextDanceClassId()));
        return entity;
    }

    @Override
    protected DanceClassDTO getNewDTO() {
        return new DanceClassDTO();
    }

    @Override
    protected DanceClass getNewEntity() {
        return new DanceClass();
    }
}
