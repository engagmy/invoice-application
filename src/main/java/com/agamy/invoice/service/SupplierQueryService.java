package com.agamy.invoice.service;

import com.agamy.invoice.domain.*; // for static metamodels
import com.agamy.invoice.domain.Supplier;
import com.agamy.invoice.repository.SupplierRepository;
import com.agamy.invoice.service.criteria.SupplierCriteria;
import com.agamy.invoice.service.dto.SupplierDTO;
import com.agamy.invoice.service.mapper.SupplierMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Supplier} entities in the database.
 * The main input is a {@link SupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SupplierDTO} or a {@link Page} of {@link SupplierDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierQueryService extends QueryService<Supplier> {

    private final Logger log = LoggerFactory.getLogger(SupplierQueryService.class);

    private final SupplierRepository supplierRepository;

    private final SupplierMapper supplierMapper;

    public SupplierQueryService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    /**
     * Return a {@link List} of {@link SupplierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierDTO> findByCriteria(SupplierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierMapper.toDto(supplierRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SupplierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierDTO> findByCriteria(SupplierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.findAll(specification, page).map(supplierMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Supplier> createSpecification(SupplierCriteria criteria) {
        Specification<Supplier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Supplier_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Supplier_.name));
            }
        }
        return specification;
    }
}
