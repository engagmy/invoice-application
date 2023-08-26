import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'supplier',
        data: { pageTitle: 'invoiceApp.supplier.home.title' },
        loadChildren: () => import('./supplier/supplier.module').then(m => m.SupplierModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'invoiceApp.item.home.title' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'supplier-item',
        data: { pageTitle: 'invoiceApp.supplierItem.home.title' },
        loadChildren: () => import('./supplier-item/supplier-item.module').then(m => m.SupplierItemModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'invoiceApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'invoice-item',
        data: { pageTitle: 'invoiceApp.invoiceItem.home.title' },
        loadChildren: () => import('./invoice-item/invoice-item.module').then(m => m.InvoiceItemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
