package com.agamy.invoice.service;

import com.agamy.invoice.domain.SupplierItem;
import com.agamy.invoice.repository.SupplierItemRepository;
import com.agamy.invoice.service.dto.SupplierItemDTO;
import com.agamy.invoice.service.mapper.SupplierItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SupplierItem}.
 */
@Service
@Transactional
public class SupplierItemService {

    private final Logger log = LoggerFactory.getLogger(SupplierItemService.class);

    private final SupplierItemRepository supplierItemRepository;

    private final SupplierItemMapper supplierItemMapper;

    public SupplierItemService(SupplierItemRepository supplierItemRepository, SupplierItemMapper supplierItemMapper) {
        this.supplierItemRepository = supplierItemRepository;
        this.supplierItemMapper = supplierItemMapper;
    }

    /**
     * Save a supplierItem.
     *
     * @param supplierItemDTO the entity to save.
     * @return the persisted entity.
     */
    public SupplierItemDTO save(SupplierItemDTO supplierItemDTO) {
        log.debug("Request to save SupplierItem : {}", supplierItemDTO);
        SupplierItem supplierItem = supplierItemMapper.toEntity(supplierItemDTO);
        supplierItem = supplierItemRepository.save(supplierItem);
        return supplierItemMapper.toDto(supplierItem);
    }

    /**
     * Partially update a supplierItem.
     *
     * @param supplierItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierItemDTO> partialUpdate(SupplierItemDTO supplierItemDTO) {
        log.debug("Request to partially update SupplierItem : {}", supplierItemDTO);

        return supplierItemRepository
            .findById(supplierItemDTO.getId())
            .map(
                existingSupplierItem -> {
                    supplierItemMapper.partialUpdate(existingSupplierItem, supplierItemDTO);

                    return existingSupplierItem;
                }
            )
            .map(supplierItemRepository::save)
            .map(supplierItemMapper::toDto);
    }

    /**
     * Get all the supplierItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SupplierItemDTO> findAll() {
        log.debug("Request to get all SupplierItems");
        return supplierItemRepository.findAll().stream().map(supplierItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one supplierItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierItemDTO> findOne(Long id) {
        log.debug("Request to get SupplierItem : {}", id);
        return supplierItemRepository.findById(id).map(supplierItemMapper::toDto);
    }

    /**
     * Delete the supplierItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SupplierItem : {}", id);
        supplierItemRepository.deleteById(id);
    }
}
