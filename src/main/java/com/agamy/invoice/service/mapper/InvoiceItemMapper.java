package com.agamy.invoice.service.mapper;

import com.agamy.invoice.domain.*;
import com.agamy.invoice.service.dto.InvoiceItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceItem} and its DTO {@link InvoiceItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { InvoiceMapper.class, ItemMapper.class })
public interface InvoiceItemMapper extends EntityMapper<InvoiceItemDTO, InvoiceItem> {
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "id")
    @Mapping(target = "item", source = "item", qualifiedByName = "id")
    InvoiceItemDTO toDto(InvoiceItem s);
}
