import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISupplierItem } from '../supplier-item.model';
import { SupplierItemService } from '../service/supplier-item.service';
import { SupplierItemDeleteDialogComponent } from '../delete/supplier-item-delete-dialog.component';

@Component({
  selector: 'inv-supplier-item',
  templateUrl: './supplier-item.component.html',
})
export class SupplierItemComponent implements OnInit {
  supplierItems?: ISupplierItem[];
  isLoading = false;

  constructor(protected supplierItemService: SupplierItemService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.supplierItemService.query().subscribe(
      (res: HttpResponse<ISupplierItem[]>) => {
        this.isLoading = false;
        this.supplierItems = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISupplierItem): number {
    return item.id!;
  }

  delete(supplierItem: ISupplierItem): void {
    const modalRef = this.modalService.open(SupplierItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.supplierItem = supplierItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
