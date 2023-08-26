import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInvoice, Invoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';

@Component({
  selector: 'inv-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    value: [null, [Validators.required]],
  });

  constructor(protected invoiceService: InvoiceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      number: invoice.number,
      value: invoice.value,
    });
  }

  protected createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      value: this.editForm.get(['value'])!.value,
    };
  }
}
