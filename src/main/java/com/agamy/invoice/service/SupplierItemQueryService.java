package com.agamy.invoice.service;

import com.agamy.invoice.domain.*; // for static metamodels
import com.agamy.invoice.domain.SupplierItem;
import com.agamy.invoice.repository.SupplierItemRepository;
import com.agamy.invoice.service.criteria.SupplierItemCriteria;
import com.agamy.invoice.service.dto.SupplierItemDTO;
import com.agamy.invoice.service.mapper.SupplierItemMapper;
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
 * Service for executing complex queries for {@link SupplierItem} entities in the database.
 * The main input is a {@link SupplierItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SupplierItemDTO} or a {@link Page} of {@link SupplierItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierItemQueryService extends QueryService<SupplierItem> {

    private final Logger log = LoggerFactory.getLogger(SupplierItemQueryService.class);

    private final SupplierItemRepository supplierItemRepository;

    private final SupplierItemMapper supplierItemMapper;

    public SupplierItemQueryService(SupplierItemRepository supplierItemRepository, SupplierItemMapper supplierItemMapper) {
        this.supplierItemRepository = supplierItemRepository;
        this.supplierItemMapper = supplierItemMapper;
    }

    /**
     * Return a {@link List} of {@link SupplierItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierItemDTO> findByCriteria(SupplierItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SupplierItem> specification = createSpecification(criteria);
        return supplierItemMapper.toDto(supplierItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SupplierItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierItemDTO> findByCriteria(SupplierItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierItem> specification = createSpecification(criteria);
        return supplierItemRepository.findAll(specification, page).map(supplierItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SupplierItem> specification = createSpecification(criteria);
        return supplierItemRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierItem> createSpecification(SupplierItemCriteria criteria) {
        Specification<SupplierItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierItem_.id));
            }
            if (criteria.getSupplierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupplierId(),
                            root -> root.join(SupplierItem_.supplier, JoinType.LEFT).get(Supplier_.id)
                        )
                    );
            }
            if (criteria.getItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getItemId(), root -> root.join(SupplierItem_.item, JoinType.LEFT).get(Item_.id))
                    );
            }
        }
        return specification;
    }
}
