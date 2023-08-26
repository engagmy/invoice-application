jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SupplierItemService } from '../service/supplier-item.service';
import { ISupplierItem, SupplierItem } from '../supplier-item.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

import { SupplierItemUpdateComponent } from './supplier-item-update.component';

describe('Component Tests', () => {
  describe('SupplierItem Management Update Component', () => {
    let comp: SupplierItemUpdateComponent;
    let fixture: ComponentFixture<SupplierItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let supplierItemService: SupplierItemService;
    let supplierService: SupplierService;
    let itemService: ItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SupplierItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SupplierItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SupplierItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      supplierItemService = TestBed.inject(SupplierItemService);
      supplierService = TestBed.inject(SupplierService);
      itemService = TestBed.inject(ItemService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Supplier query and add missing value', () => {
        const supplierItem: ISupplierItem = { id: 456 };
        const supplier: ISupplier = { id: 20779 };
        supplierItem.supplier = supplier;

        const supplierCollection: ISupplier[] = [{ id: 63118 }];
        jest.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
        const additionalSuppliers = [supplier];
        const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
        jest.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ supplierItem });
        comp.ngOnInit();

        expect(supplierService.query).toHaveBeenCalled();
        expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(supplierCollection, ...additionalSuppliers);
        expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Item query and add missing value', () => {
        const supplierItem: ISupplierItem = { id: 456 };
        const item: IItem = { id: 64635 };
        supplierItem.item = item;

        const itemCollection: IItem[] = [{ id: 87514 }];
        jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
        const additionalItems = [item];
        const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
        jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ supplierItem });
        comp.ngOnInit();

        expect(itemService.query).toHaveBeenCalled();
        expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(itemCollection, ...additionalItems);
        expect(comp.itemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const supplierItem: ISupplierItem = { id: 456 };
        const supplier: ISupplier = { id: 33614 };
        supplierItem.supplier = supplier;
        const item: IItem = { id: 5941 };
        supplierItem.item = item;

        activatedRoute.data = of({ supplierItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(supplierItem));
        expect(comp.suppliersSharedCollection).toContain(supplier);
        expect(comp.itemsSharedCollection).toContain(item);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SupplierItem>>();
        const supplierItem = { id: 123 };
        jest.spyOn(supplierItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ supplierItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: supplierItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(supplierItemService.update).toHaveBeenCalledWith(supplierItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SupplierItem>>();
        const supplierItem = new SupplierItem();
        jest.spyOn(supplierItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ supplierItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: supplierItem }));
        saveSubject.complete();

        // THEN
        expect(supplierItemService.create).toHaveBeenCalledWith(supplierItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SupplierItem>>();
        const supplierItem = { id: 123 };
        jest.spyOn(supplierItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ supplierItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(supplierItemService.update).toHaveBeenCalledWith(supplierItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSupplierById', () => {
        it('Should return tracked Supplier primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSupplierById(0, entity);
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
