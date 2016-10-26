package by.danceform.app.converter;

import by.danceform.app.dto.AbstractDomainDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dimonn12 on 07.10.2016.
 */

public abstract class Converter<T extends Serializable, D extends AbstractDomainDTO<? extends Serializable>> {

    /**
     * Convert entity object to DTO.
     *
     * @param entity entity object
     * @return DTO object
     */
    public abstract D convertToDto(T entity);

    /**
     * Convert a list of entity objects to a list of DTOs.
     *
     * @param entities a list of entity objects
     * @return a list of DTO objects
     */
    public abstract List<D> convertToDtos(List<T> entities);

    /**
     * Convert DTO object to entity.
     *
     * @param dto DTO object
     * @return entity object
     */
    public abstract T convertToEntity(D dto);

    /**
     * Convert a list of DTOs to a list of entity objects.
     *
     * @param dtos a list of DTO objects
     * @return a list of entity objects
     */
    public abstract List<T> convertToEntities(List<D> dtos);

}
