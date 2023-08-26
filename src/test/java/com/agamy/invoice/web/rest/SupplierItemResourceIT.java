package com.agamy.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agamy.invoice.IntegrationTest;
import com.agamy.invoice.domain.Item;
import com.agamy.invoice.domain.Supplier;
import com.agamy.invoice.domain.SupplierItem;
import com.agamy.invoice.repository.SupplierItemRepository;
import com.agamy.invoice.service.criteria.SupplierItemCriteria;
import com.agamy.invoice.service.dto.SupplierItemDTO;
import com.agamy.invoice.service.mapper.SupplierItemMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SupplierItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupplierItemResourceIT {

    private static final String ENTITY_API_URL = "/api/supplier-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SupplierItemRepository supplierItemRepository;

    @Autowired
    private SupplierItemMapper supplierItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierItemMockMvc;

    private SupplierItem supplierItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierItem createEntity(EntityManager em) {
        SupplierItem supplierItem = new SupplierItem();
        return supplierItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierItem createUpdatedEntity(EntityManager em) {
        SupplierItem supplierItem = new SupplierItem();
        return supplierItem;
    }

    @BeforeEach
    public void initTest() {
        supplierItem = createEntity(em);
    }

    @Test
    @Transactional
    void createSupplierItem() throws Exception {
        int databaseSizeBeforeCreate = supplierItemRepository.findAll().size();
        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);
        restSupplierItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeCreate + 1);
        SupplierItem testSupplierItem = supplierItemList.get(supplierItemList.size() - 1);
    }

    @Test
    @Transactional
    void createSupplierItemWithExistingId() throws Exception {
        // Create the SupplierItem with an existing ID
        supplierItem.setId(1L);
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        int databaseSizeBeforeCreate = supplierItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSupplierItems() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        // Get all the supplierItemList
        restSupplierItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierItem.getId().intValue())));
    }

    @Test
    @Transactional
    void getSupplierItem() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        // Get the supplierItem
        restSupplierItemMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierItem.getId().intValue()));
    }

    @Test
    @Transactional
    void getSupplierItemsByIdFiltering() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        Long id = supplierItem.getId();

        defaultSupplierItemShouldBeFound("id.equals=" + id);
        defaultSupplierItemShouldNotBeFound("id.notEquals=" + id);

        defaultSupplierItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSupplierItemShouldNotBeFound("id.greaterThan=" + id);

        defaultSupplierItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSupplierItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSupplierItemsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);
        Supplier supplier = SupplierResourceIT.createEntity(em);
        em.persist(supplier);
        em.flush();
        supplierItem.setSupplier(supplier);
        supplierItemRepository.saveAndFlush(supplierItem);
        Long supplierId = supplier.getId();

        // Get all the supplierItemList where supplier equals to supplierId
        defaultSupplierItemShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the supplierItemList where supplier equals to (supplierId + 1)
        defaultSupplierItemShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    @Test
    @Transactional
    void getAllSupplierItemsByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);
        Item item = ItemResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        supplierItem.setItem(item);
        supplierItemRepository.saveAndFlush(supplierItem);
        Long itemId = item.getId();

        // Get all the supplierItemList where item equals to itemId
        defaultSupplierItemShouldBeFound("itemId.equals=" + itemId);

        // Get all the supplierItemList where item equals to (itemId + 1)
        defaultSupplierItemShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierItemShouldBeFound(String filter) throws Exception {
        restSupplierItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierItem.getId().intValue())));

        // Check, that the count call also returns 1
        restSupplierItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierItemShouldNotBeFound(String filter) throws Exception {
        restSupplierItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplierItem() throws Exception {
        // Get the supplierItem
        restSupplierItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSupplierItem() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();

        // Update the supplierItem
        SupplierItem updatedSupplierItem = supplierItemRepository.findById(supplierItem.getId()).get();
        // Disconnect from session so that the updates on updatedSupplierItem are not directly saved in db
        em.detach(updatedSupplierItem);
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(updatedSupplierItem);

        restSupplierItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
        SupplierItem testSupplierItem = supplierItemList.get(supplierItemList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingSupplierItem() throws Exception {
        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();
        supplierItem.setId(count.incrementAndGet());

        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierItem() throws Exception {
        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();
        supplierItem.setId(count.incrementAndGet());

        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierItem() throws Exception {
        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();
        supplierItem.setId(count.incrementAndGet());

        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierItemWithPatch() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();

        // Update the supplierItem using partial update
        SupplierItem partialUpdatedSupplierItem = new SupplierItem();
        partialUpdatedSupplierItem.setId(supplierItem.getId());

        restSupplierItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplierItem))
            )
            .andExpect(status().isOk());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
        SupplierItem testSupplierItem = supplierItemList.get(supplierItemList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateSupplierItemWithPatch() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();

        // Update the supplierItem using partial update
        SupplierItem partialUpdatedSupplierItem = new SupplierItem();
        partialUpdatedSupplierItem.setId(supplierItem.getId());

        restSupplierItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplierItem))
            )
            .andExpect(status().isOk());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
        SupplierItem testSupplierItem = supplierItemList.get(supplierItemList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingSupplierItem() throws Exception {
        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();
        supplierItem.setId(count.incrementAndGet());

        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierItem() throws Exception {
        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();
        supplierItem.setId(count.incrementAndGet());

        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierItem() throws Exception {
        int databaseSizeBeforeUpdate = supplierItemRepository.findAll().size();
        supplierItem.setId(count.incrementAndGet());

        // Create the SupplierItem
        SupplierItemDTO supplierItemDTO = supplierItemMapper.toDto(supplierItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplierItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierItem in the database
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierItem() throws Exception {
        // Initialize the database
        supplierItemRepository.saveAndFlush(supplierItem);

        int databaseSizeBeforeDelete = supplierItemRepository.findAll().size();

        // Delete the supplierItem
        restSupplierItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SupplierItem> supplierItemList = supplierItemRepository.findAll();
        assertThat(supplierItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
