package com.agamy.invoice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceItemMapperTest {

    private InvoiceItemMapper invoiceItemMapper;

    @BeforeEach
    public void setUp() {
        invoiceItemMapper = new InvoiceItemMapperImpl();
    }
}
