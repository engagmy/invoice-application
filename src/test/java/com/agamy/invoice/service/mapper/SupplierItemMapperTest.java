package com.agamy.invoice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierItemMapperTest {

    private SupplierItemMapper supplierItemMapper;

    @BeforeEach
    public void setUp() {
        supplierItemMapper = new SupplierItemMapperImpl();
    }
}
