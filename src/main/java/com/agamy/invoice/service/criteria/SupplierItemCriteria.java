package com.agamy.invoice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.agamy.invoice.domain.SupplierItem} entity. This class is used
 * in {@link com.agamy.invoice.web.rest.SupplierItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /supplier-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SupplierItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter supplierId;

    private LongFilter itemId;

    public SupplierItemCriteria() {}

    public SupplierItemCriteria(SupplierItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public SupplierItemCriteria copy() {
        return new SupplierItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            supplierId = new LongFilter();
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public LongFilter itemId() {
        if (itemId == null) {
            itemId = new LongFilter();
        }
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SupplierItemCriteria that = (SupplierItemCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(supplierId, that.supplierId) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, supplierId, itemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }
}
