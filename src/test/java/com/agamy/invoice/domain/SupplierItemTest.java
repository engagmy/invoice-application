package com.agamy.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.agamy.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierItem.class);
        SupplierItem supplierItem1 = new SupplierItem();
        supplierItem1.setId(1L);
        SupplierItem supplierItem2 = new SupplierItem();
        supplierItem2.setId(supplierItem1.getId());
        assertThat(supplierItem1).isEqualTo(supplierItem2);
        supplierItem2.setId(2L);
        assertThat(supplierItem1).isNotEqualTo(supplierItem2);
        supplierItem1.setId(null);
        assertThat(supplierItem1).isNotEqualTo(supplierItem2);
    }
}
