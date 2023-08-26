import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SupplierItemComponent } from './list/supplier-item.component';
import { SupplierItemDetailComponent } from './detail/supplier-item-detail.component';
import { SupplierItemUpdateComponent } from './update/supplier-item-update.component';
import { SupplierItemDeleteDialogComponent } from './delete/supplier-item-delete-dialog.component';
import { SupplierItemRoutingModule } from './route/supplier-item-routing.module';

@NgModule({
  imports: [SharedModule, SupplierItemRoutingModule],
  declarations: [SupplierItemComponent, SupplierItemDetailComponent, SupplierItemUpdateComponent, SupplierItemDeleteDialogComponent],
  entryComponents: [SupplierItemDeleteDialogComponent],
})
export class SupplierItemModule {}
