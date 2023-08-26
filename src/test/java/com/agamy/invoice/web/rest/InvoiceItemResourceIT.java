package com.agamy.invoice.web.rest;

import static com.agamy.invoice.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agamy.invoice.IntegrationTest;
import com.agamy.invoice.domain.Invoice;
import com.agamy.invoice.domain.InvoiceItem;
import com.agamy.invoice.domain.Item;
import com.agamy.invoice.repository.InvoiceItemRepository;
import com.agamy.invoice.service.criteria.InvoiceItemCriteria;
import com.agamy.invoice.service.dto.InvoiceItemDTO;
import com.agamy.invoice.service.mapper.InvoiceItemMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InvoiceItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemResourceIT {

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/invoice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private InvoiceItemMapper invoiceItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createEntity(EntityManager em) {
        InvoiceItem invoiceItem = new InvoiceItem().value(DEFAULT_VALUE);
        return invoiceItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createUpdatedEntity(EntityManager em) {
        InvoiceItem invoiceItem = new InvoiceItem().value(UPDATED_VALUE);
        return invoiceItem;
    }

    @BeforeEach
    public void initTest() {
        invoiceItem = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();
        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);
        restInvoiceItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createInvoiceItemWithExistingId() throws Exception {
        // Create the InvoiceItem with an existing ID
        invoiceItem.setId(1L);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceItemRepository.findAll().size();
        // set the field null
        invoiceItem.setValue(null);

        // Create the InvoiceItem, which fails.
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        restInvoiceItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));
    }

    @Test
    @Transactional
    void getInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId().intValue()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getInvoiceItemsByIdFiltering() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        Long id = invoiceItem.getId();

        defaultInvoiceItemShouldBeFound("id.equals=" + id);
        defaultInvoiceItemShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceItemShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value equals to DEFAULT_VALUE
        defaultInvoiceItemShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the invoiceItemList where value equals to UPDATED_VALUE
        defaultInvoiceItemShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value not equals to DEFAULT_VALUE
        defaultInvoiceItemShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the invoiceItemList where value not equals to UPDATED_VALUE
        defaultInvoiceItemShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultInvoiceItemShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the invoiceItemList where value equals to UPDATED_VALUE
        defaultInvoiceItemShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value is not null
        defaultInvoiceItemShouldBeFound("value.specified=true");

        // Get all the invoiceItemList where value is null
        defaultInvoiceItemShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value is greater than or equal to DEFAULT_VALUE
        defaultInvoiceItemShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the invoiceItemList where value is greater than or equal to UPDATED_VALUE
        defaultInvoiceItemShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value is less than or equal to DEFAULT_VALUE
        defaultInvoiceItemShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the invoiceItemList where value is less than or equal to SMALLER_VALUE
        defaultInvoiceItemShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value is less than DEFAULT_VALUE
        defaultInvoiceItemShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the invoiceItemList where value is less than UPDATED_VALUE
        defaultInvoiceItemShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItemList where value is greater than DEFAULT_VALUE
        defaultInvoiceItemShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the invoiceItemList where value is greater than SMALLER_VALUE
        defaultInvoiceItemShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Invoice invoice = InvoiceResourceIT.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoiceItem.setInvoice(invoice);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long invoiceId = invoice.getId();

        // Get all the invoiceItemList where invoice equals to invoiceId
        defaultInvoiceItemShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the invoiceItemList where invoice equals to (invoiceId + 1)
        defaultInvoiceItemShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllInvoiceItemsByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Item item = ItemResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        invoiceItem.setItem(item);
        invoiceItemRepository.saveAndFlush(invoiceItem);
        Long itemId = item.getId();

        // Get all the invoiceItemList where item equals to itemId
        defaultInvoiceItemShouldBeFound("itemId.equals=" + itemId);

        // Get all the invoiceItemList where item equals to (itemId + 1)
        defaultInvoiceItemShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceItemShouldBeFound(String filter) throws Exception {
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));

        // Check, that the count call also returns 1
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceItemShouldNotBeFound(String filter) throws Exception {
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = invoiceItemRepository.findById(invoiceItem.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceItem are not directly saved in db
        em.detach(updatedInvoiceItem);
        updatedInvoiceItem.value(UPDATED_VALUE);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(updatedInvoiceItem);

        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(count.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(count.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(count.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem.value(UPDATED_VALUE);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(count.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(count.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(count.incrementAndGet());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        int databaseSizeBeforeDelete = invoiceItemRepository.findAll().size();

        // Delete the invoiceItem
        restInvoiceItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
