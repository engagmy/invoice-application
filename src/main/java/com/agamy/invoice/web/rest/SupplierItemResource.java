package com.agamy.invoice.web.rest;

import com.agamy.invoice.repository.SupplierItemRepository;
import com.agamy.invoice.service.SupplierItemQueryService;
import com.agamy.invoice.service.SupplierItemService;
import com.agamy.invoice.service.criteria.SupplierItemCriteria;
import com.agamy.invoice.service.dto.SupplierItemDTO;
import com.agamy.invoice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agamy.invoice.domain.SupplierItem}.
 */
@RestController
@RequestMapping("/api")
public class SupplierItemResource {

    private final Logger log = LoggerFactory.getLogger(SupplierItemResource.class);

    private static final String ENTITY_NAME = "supplierItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierItemService supplierItemService;

    private final SupplierItemRepository supplierItemRepository;

    private final SupplierItemQueryService supplierItemQueryService;

    public SupplierItemResource(
        SupplierItemService supplierItemService,
        SupplierItemRepository supplierItemRepository,
        SupplierItemQueryService supplierItemQueryService
    ) {
        this.supplierItemService = supplierItemService;
        this.supplierItemRepository = supplierItemRepository;
        this.supplierItemQueryService = supplierItemQueryService;
    }

    /**
     * {@code POST  /supplier-items} : Create a new supplierItem.
     *
     * @param supplierItemDTO the supplierItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierItemDTO, or with status {@code 400 (Bad Request)} if the supplierItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/supplier-items")
    public ResponseEntity<SupplierItemDTO> createSupplierItem(@RequestBody SupplierItemDTO supplierItemDTO) throws URISyntaxException {
        log.debug("REST request to save SupplierItem : {}", supplierItemDTO);
        if (supplierItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierItemDTO result = supplierItemService.save(supplierItemDTO);
        return ResponseEntity
            .created(new URI("/api/supplier-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-items/:id} : Updates an existing supplierItem.
     *
     * @param id the id of the supplierItemDTO to save.
     * @param supplierItemDTO the supplierItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierItemDTO,
     * or with status {@code 400 (Bad Request)} if the supplierItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/supplier-items/{id}")
    public ResponseEntity<SupplierItemDTO> updateSupplierItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupplierItemDTO supplierItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SupplierItem : {}, {}", id, supplierItemDTO);
        if (supplierItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SupplierItemDTO result = supplierItemService.save(supplierItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /supplier-items/:id} : Partial updates given fields of an existing supplierItem, field will ignore if it is null
     *
     * @param id the id of the supplierItemDTO to save.
     * @param supplierItemDTO the supplierItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierItemDTO,
     * or with status {@code 400 (Bad Request)} if the supplierItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/supplier-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SupplierItemDTO> partialUpdateSupplierItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupplierItemDTO supplierItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SupplierItem partially : {}, {}", id, supplierItemDTO);
        if (supplierItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierItemDTO> result = supplierItemService.partialUpdate(supplierItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-items} : get all the supplierItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierItems in body.
     */
    @GetMapping("/supplier-items")
    public ResponseEntity<List<SupplierItemDTO>> getAllSupplierItems(SupplierItemCriteria criteria) {
        log.debug("REST request to get SupplierItems by criteria: {}", criteria);
        List<SupplierItemDTO> entityList = supplierItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /supplier-items/count} : count all the supplierItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/supplier-items/count")
    public ResponseEntity<Long> countSupplierItems(SupplierItemCriteria criteria) {
        log.debug("REST request to count SupplierItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-items/:id} : get the "id" supplierItem.
     *
     * @param id the id of the supplierItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/supplier-items/{id}")
    public ResponseEntity<SupplierItemDTO> getSupplierItem(@PathVariable Long id) {
        log.debug("REST request to get SupplierItem : {}", id);
        Optional<SupplierItemDTO> supplierItemDTO = supplierItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierItemDTO);
    }

    /**
     * {@code DELETE  /supplier-items/:id} : delete the "id" supplierItem.
     *
     * @param id the id of the supplierItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/supplier-items/{id}")
    public ResponseEntity<Void> deleteSupplierItem(@PathVariable Long id) {
        log.debug("REST request to delete SupplierItem : {}", id);
        supplierItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
