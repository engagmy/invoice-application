package com.agamy.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agamy.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItem.class);
        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setId(1L);
        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setId(invoiceItem1.getId());
        assertThat(invoiceItem1).isEqualTo(invoiceItem2);
        invoiceItem2.setId(2L);
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);
        invoiceItem1.setId(null);
        assertThat(invoiceItem1).isNotEqualTo(invoiceItem2);
    }
}
