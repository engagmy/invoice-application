package com.agamy.invoice.service.mapper;

import com.agamy.invoice.domain.*;
import com.agamy.invoice.service.dto.SupplierItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SupplierItem} and its DTO {@link SupplierItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { SupplierMapper.class, ItemMapper.class })
public interface SupplierItemMapper extends EntityMapper<SupplierItemDTO, SupplierItem> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "id")
    @Mapping(target = "item", source = "item", qualifiedByName = "id")
    SupplierItemDTO toDto(SupplierItem s);
}
