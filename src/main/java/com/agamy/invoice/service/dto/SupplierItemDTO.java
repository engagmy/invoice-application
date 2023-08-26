package com.agamy.invoice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.agamy.invoice.domain.SupplierItem} entity.
 */
public class SupplierItemDTO implements Serializable {

    private Long id;

    private SupplierDTO supplier;

    private ItemDTO item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierItemDTO)) {
            return false;
        }

        SupplierItemDTO supplierItemDTO = (SupplierItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, supplierItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierItemDTO{" +
            "id=" + getId() +
            ", supplier=" + getSupplier() +
            ", item=" + getItem() +
            "}";
    }
}
