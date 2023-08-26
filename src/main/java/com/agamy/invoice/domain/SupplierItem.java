package com.agamy.invoice.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupplierItem.
 */
@Entity
@Table(name = "supplier_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SupplierItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SupplierItem id(Long id) {
        this.id = id;
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public SupplierItem supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Item getItem() {
        return this.item;
    }

    public SupplierItem item(Item item) {
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
        if (!(o instanceof SupplierItem)) {
            return false;
        }
        return id != null && id.equals(((SupplierItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierItem{" +
            "id=" + getId() +
            "}";
    }
}
