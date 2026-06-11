package upc.ecovolt.mapping.dto;

import java.util.List;

public interface GenericMapper<E, RQ, RS> {
    E toEntity(RQ requestDto);
    RS toResponseDto(E entity);
    List<RS> toResponseDtoList(List<E> entityList);
}
