package com.agamy.invoice.web.rest;

import com.agamy.invoice.repository.InvoiceItemRepository;
import com.agamy.invoice.service.InvoiceItemQueryService;
import com.agamy.invoice.service.InvoiceItemService;
import com.agamy.invoice.service.criteria.InvoiceItemCriteria;
import com.agamy.invoice.service.dto.InvoiceItemDTO;
import com.agamy.invoice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agamy.invoice.domain.InvoiceItem}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceItemResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceItemResource.class);

    private static final String ENTITY_NAME = "invoiceItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceItemService invoiceItemService;

    private final InvoiceItemRepository invoiceItemRepository;

    private final InvoiceItemQueryService invoiceItemQueryService;

    public InvoiceItemResource(
        InvoiceItemService invoiceItemService,
        InvoiceItemRepository invoiceItemRepository,
        InvoiceItemQueryService invoiceItemQueryService
    ) {
        this.invoiceItemService = invoiceItemService;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemQueryService = invoiceItemQueryService;
    }

    /**
     * {@code POST  /invoice-items} : Create a new invoiceItem.
     *
     * @param invoiceItemDTO the invoiceItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceItemDTO, or with status {@code 400 (Bad Request)} if the invoiceItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-items")
    public ResponseEntity<InvoiceItemDTO> createInvoiceItem(@Valid @RequestBody InvoiceItemDTO invoiceItemDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceItem : {}", invoiceItemDTO);
        if (invoiceItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceItemDTO result = invoiceItemService.save(invoiceItemDTO);
        return ResponseEntity
            .created(new URI("/api/invoice-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-items/:id} : Updates an existing invoiceItem.
     *
     * @param id the id of the invoiceItemDTO to save.
     * @param invoiceItemDTO the invoiceItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItemDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-items/{id}")
    public ResponseEntity<InvoiceItemDTO> updateInvoiceItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceItemDTO invoiceItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceItem : {}, {}", id, invoiceItemDTO);
        if (invoiceItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceItemDTO result = invoiceItemService.save(invoiceItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-items/:id} : Partial updates given fields of an existing invoiceItem, field will ignore if it is null
     *
     * @param id the id of the invoiceItemDTO to save.
     * @param invoiceItemDTO the invoiceItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceItemDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<InvoiceItemDTO> partialUpdateInvoiceItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceItemDTO invoiceItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceItem partially : {}, {}", id, invoiceItemDTO);
        if (invoiceItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceItemDTO> result = invoiceItemService.partialUpdate(invoiceItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-items} : get all the invoiceItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceItems in body.
     */
    @GetMapping("/invoice-items")
    public ResponseEntity<List<InvoiceItemDTO>> getAllInvoiceItems(InvoiceItemCriteria criteria) {
        log.debug("REST request to get InvoiceItems by criteria: {}", criteria);
        List<InvoiceItemDTO> entityList = invoiceItemQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /invoice-items/count} : count all the invoiceItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/invoice-items/count")
    public ResponseEntity<Long> countInvoiceItems(InvoiceItemCriteria criteria) {
        log.debug("REST request to count InvoiceItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-items/:id} : get the "id" invoiceItem.
     *
     * @param id the id of the invoiceItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-items/{id}")
    public ResponseEntity<InvoiceItemDTO> getInvoiceItem(@PathVariable Long id) {
        log.debug("REST request to get InvoiceItem : {}", id);
        Optional<InvoiceItemDTO> invoiceItemDTO = invoiceItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceItemDTO);
    }

    /**
     * {@code DELETE  /invoice-items/:id} : delete the "id" invoiceItem.
     *
     * @param id the id of the invoiceItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-items/{id}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceItem : {}", id);
        invoiceItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
