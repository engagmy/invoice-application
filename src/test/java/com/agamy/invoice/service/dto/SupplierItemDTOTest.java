package com.agamy.invoice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agamy.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierItemDTO.class);
        SupplierItemDTO supplierItemDTO1 = new SupplierItemDTO();
        supplierItemDTO1.setId(1L);
        SupplierItemDTO supplierItemDTO2 = new SupplierItemDTO();
        assertThat(supplierItemDTO1).isNotEqualTo(supplierItemDTO2);
        supplierItemDTO2.setId(supplierItemDTO1.getId());
        assertThat(supplierItemDTO1).isEqualTo(supplierItemDTO2);
        supplierItemDTO2.setId(2L);
        assertThat(supplierItemDTO1).isNotEqualTo(supplierItemDTO2);
        supplierItemDTO1.setId(null);
        assertThat(supplierItemDTO1).isNotEqualTo(supplierItemDTO2);
    }
}
