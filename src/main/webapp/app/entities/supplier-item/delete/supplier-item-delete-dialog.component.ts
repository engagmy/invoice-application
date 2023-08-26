import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISupplierItem } from '../supplier-item.model';
import { SupplierItemService } from '../service/supplier-item.service';

@Component({
  templateUrl: './supplier-item-delete-dialog.component.html',
})
export class SupplierItemDeleteDialogComponent {
  supplierItem?: ISupplierItem;

  constructor(protected supplierItemService: SupplierItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.supplierItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
