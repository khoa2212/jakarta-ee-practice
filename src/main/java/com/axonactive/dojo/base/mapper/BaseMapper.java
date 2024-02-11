package com.axonactive.dojo.base.mapper;

import java.util.List;

public interface BaseMapper<D, E> {
    D toDTO(E entity);

    E toEntity(D dto);

    List<D> toListDTO(List<E> list);
}
