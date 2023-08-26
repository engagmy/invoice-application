package com.agamy.invoice.repository;

import com.agamy.invoice.domain.Supplier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Supplier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {}
