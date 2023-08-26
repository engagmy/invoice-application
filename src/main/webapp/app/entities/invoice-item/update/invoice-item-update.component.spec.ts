jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InvoiceItemService } from '../service/invoice-item.service';
import { IInvoiceItem, InvoiceItem } from '../invoice-item.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

import { InvoiceItemUpdateComponent } from './invoice-item-update.component';

describe('Component Tests', () => {
  describe('InvoiceItem Management Update Component', () => {
    let comp: InvoiceItemUpdateComponent;
    let fixture: ComponentFixture<InvoiceItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let invoiceItemService: InvoiceItemService;
    let invoiceService: InvoiceService;
    let itemService: ItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvoiceItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InvoiceItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      invoiceItemService = TestBed.inject(InvoiceItemService);
      invoiceService = TestBed.inject(InvoiceService);
      itemService = TestBed.inject(ItemService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Invoice query and add missing value', () => {
        const invoiceItem: IInvoiceItem = { id: 456 };
        const invoice: IInvoice = { id: 60228 };
        invoiceItem.invoice = invoice;

        const invoiceCollection: IInvoice[] = [{ id: 6667 }];
        jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
        const additionalInvoices = [invoice];
        const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
        jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoiceItem });
        comp.ngOnInit();

        expect(invoiceService.query).toHaveBeenCalled();
        expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(invoiceCollection, ...additionalInvoices);
        expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Item query and add missing value', () => {
        const invoiceItem: IInvoiceItem = { id: 456 };
        const item: IItem = { id: 86174 };
        invoiceItem.item = item;

        const itemCollection: IItem[] = [{ id: 40707 }];
        jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
        const additionalItems = [item];
        const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
        jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ invoiceItem });
        comp.ngOnInit();

        expect(itemService.query).toHaveBeenCalled();
        expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(itemCollection, ...additionalItems);
        expect(comp.itemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const invoiceItem: IInvoiceItem = { id: 456 };
        const invoice: IInvoice = { id: 37019 };
        invoiceItem.invoice = invoice;
        const item: IItem = { id: 44483 };
        invoiceItem.item = item;

        activatedRoute.data = of({ invoiceItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(invoiceItem));
        expect(comp.invoicesSharedCollection).toContain(invoice);
        expect(comp.itemsSharedCollection).toContain(item);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<InvoiceItem>>();
        const invoiceItem = { id: 123 };
        jest.spyOn(invoiceItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoiceItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invoiceItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(invoiceItemService.update).toHaveBeenCalledWith(invoiceItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<InvoiceItem>>();
        const invoiceItem = new InvoiceItem();
        jest.spyOn(invoiceItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoiceItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: invoiceItem }));
        saveSubject.complete();

        // THEN
        expect(invoiceItemService.create).toHaveBeenCalledWith(invoiceItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<InvoiceItem>>();
        const invoiceItem = { id: 123 };
        jest.spyOn(invoiceItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ invoiceItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(invoiceItemService.update).toHaveBeenCalledWith(invoiceItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInvoiceById', () => {
        it('Should return tracked Invoice primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInvoiceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackItemById', () => {
        it('Should return tracked Item primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
