package com.agamy.invoice.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceItem.
 */
@Entity
@Table(name = "invoice_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InvoiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @ManyToOne
    private Invoice invoice;

    @ManyToOne
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvoiceItem id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public InvoiceItem value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public InvoiceItem invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Item getItem() {
        return this.item;
    }

    public InvoiceItem item(Item item) {
        this.setItem(item);
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceItem)) {
            return false;
        }
        return id != null && id.equals(((InvoiceItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceItem{" +
            "id=" + getId() +
            ", value=" + getValue() +
            "}";
    }
}
