import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISupplierItem, SupplierItem } from '../supplier-item.model';
import { SupplierItemService } from '../service/supplier-item.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

@Component({
  selector: 'inv-supplier-item-update',
  templateUrl: './supplier-item-update.component.html',
})
export class SupplierItemUpdateComponent implements OnInit {
  isSaving = false;

  suppliersSharedCollection: ISupplier[] = [];
  itemsSharedCollection: IItem[] = [];

  editForm = this.fb.group({
    id: [],
    supplier: [],
    item: [],
  });

  constructor(
    protected supplierItemService: SupplierItemService,
    protected supplierService: SupplierService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierItem }) => {
      this.updateForm(supplierItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplierItem = this.createFromForm();
    if (supplierItem.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierItemService.update(supplierItem));
    } else {
      this.subscribeToSaveResponse(this.supplierItemService.create(supplierItem));
    }
  }

  trackSupplierById(index: number, item: ISupplier): number {
    return item.id!;
  }

  trackItemById(index: number, item: IItem): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplierItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(supplierItem: ISupplierItem): void {
    this.editForm.patchValue({
      id: supplierItem.id,
      supplier: supplierItem.supplier,
      item: supplierItem.item,
    });

    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing(
      this.suppliersSharedCollection,
      supplierItem.supplier
    );
    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing(this.itemsSharedCollection, supplierItem.item);
  }

  protected loadRelationshipsOptions(): void {
    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing(suppliers, this.editForm.get('supplier')!.value)
        )
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));

    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing(items, this.editForm.get('item')!.value)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }

  protected createFromForm(): ISupplierItem {
    return {
      ...new SupplierItem(),
      id: this.editForm.get(['id'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
      item: this.editForm.get(['item'])!.value,
    };
  }
}
