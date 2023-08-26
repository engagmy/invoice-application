package com.agamy.invoice.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.agamy.invoice.domain.InvoiceItem} entity.
 */
public class InvoiceItemDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal value;

    private InvoiceDTO invoice;

    private ItemDTO item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
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
        if (!(o instanceof InvoiceItemDTO)) {
            return false;
        }

        InvoiceItemDTO invoiceItemDTO = (InvoiceItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", invoice=" + getInvoice() +
            ", item=" + getItem() +
            "}";
    }
}
