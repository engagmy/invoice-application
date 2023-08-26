package com.agamy.invoice.repository;

import com.agamy.invoice.domain.SupplierItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SupplierItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierItemRepository extends JpaRepository<SupplierItem, Long>, JpaSpecificationExecutor<SupplierItem> {}
