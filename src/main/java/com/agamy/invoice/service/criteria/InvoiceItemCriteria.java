package com.agamy.invoice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.agamy.invoice.domain.InvoiceItem} entity. This class is used
 * in {@link com.agamy.invoice.web.rest.InvoiceItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoice-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter value;

    private LongFilter invoiceId;

    private LongFilter itemId;

    public InvoiceItemCriteria() {}

    public InvoiceItemCriteria(InvoiceItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.invoiceId = other.invoiceId == null ? null : other.invoiceId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public InvoiceItemCriteria copy() {
        return new InvoiceItemCriteria(this);
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

    public BigDecimalFilter getValue() {
        return value;
    }

    public BigDecimalFilter value() {
        if (value == null) {
            value = new BigDecimalFilter();
        }
        return value;
    }

    public void setValue(BigDecimalFilter value) {
        this.value = value;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public LongFilter invoiceId() {
        if (invoiceId == null) {
            invoiceId = new LongFilter();
        }
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
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
        final InvoiceItemCriteria that = (InvoiceItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(invoiceId, that.invoiceId) &&
            Objects.equals(itemId, that.itemId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, invoiceId, itemId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
            (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }
}
