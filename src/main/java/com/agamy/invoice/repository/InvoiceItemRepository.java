package com.agamy.invoice.repository;

import com.agamy.invoice.domain.InvoiceItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InvoiceItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long>, JpaSpecificationExecutor<InvoiceItem> {}
