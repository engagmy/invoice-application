import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';
import { InvoiceItemDeleteDialogComponent } from '../delete/invoice-item-delete-dialog.component';

@Component({
  selector: 'inv-invoice-item',
  templateUrl: './invoice-item.component.html',
})
export class InvoiceItemComponent implements OnInit {
  invoiceItems?: IInvoiceItem[];
  isLoading = false;

  constructor(protected invoiceItemService: InvoiceItemService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.invoiceItemService.query().subscribe(
      (res: HttpResponse<IInvoiceItem[]>) => {
        this.isLoading = false;
        this.invoiceItems = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInvoiceItem): number {
    return item.id!;
  }

  delete(invoiceItem: IInvoiceItem): void {
    const modalRef = this.modalService.open(InvoiceItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.invoiceItem = invoiceItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
