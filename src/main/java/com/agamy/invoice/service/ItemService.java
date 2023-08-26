package com.agamy.invoice.service;

import com.agamy.invoice.domain.Item;
import com.agamy.invoice.repository.ItemRepository;
import com.agamy.invoice.service.dto.ItemDTO;
import com.agamy.invoice.service.mapper.ItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * Partially update a item.
     *
     * @param itemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

        return itemRepository
            .findById(itemDTO.getId())
            .map(
                existingItem -> {
                    itemMapper.partialUpdate(existingItem, itemDTO);

                    return existingItem;
                }
            )
            .map(itemRepository::save)
            .map(itemMapper::toDto);
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id).map(itemMapper::toDto);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }
}
