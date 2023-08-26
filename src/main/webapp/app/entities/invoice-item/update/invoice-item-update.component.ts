import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInvoiceItem, InvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

@Component({
  selector: 'inv-invoice-item-update',
  templateUrl: './invoice-item-update.component.html',
})
export class InvoiceItemUpdateComponent implements OnInit {
  isSaving = false;

  invoicesSharedCollection: IInvoice[] = [];
  itemsSharedCollection: IItem[] = [];

  editForm = this.fb.group({
    id: [],
    value: [null, [Validators.required]],
    invoice: [],
    item: [],
  });

  constructor(
    protected invoiceItemService: InvoiceItemService,
    protected invoiceService: InvoiceService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceItem }) => {
      this.updateForm(invoiceItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceItem = this.createFromForm();
    if (invoiceItem.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceItemService.update(invoiceItem));
    } else {
      this.subscribeToSaveResponse(this.invoiceItemService.create(invoiceItem));
    }
  }

  trackInvoiceById(index: number, item: IInvoice): number {
    return item.id!;
  }

  trackItemById(index: number, item: IItem): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceItem>>): void {
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

  protected updateForm(invoiceItem: IInvoiceItem): void {
    this.editForm.patchValue({
      id: invoiceItem.id,
      value: invoiceItem.value,
      invoice: invoiceItem.invoice,
      item: invoiceItem.item,
    });

    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing(this.invoicesSharedCollection, invoiceItem.invoice);
    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing(this.itemsSharedCollection, invoiceItem.item);
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing(invoices, this.editForm.get('invoice')!.value))
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));

    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing(items, this.editForm.get('item')!.value)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }

  protected createFromForm(): IInvoiceItem {
    return {
      ...new InvoiceItem(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
      item: this.editForm.get(['item'])!.value,
    };
  }
}
