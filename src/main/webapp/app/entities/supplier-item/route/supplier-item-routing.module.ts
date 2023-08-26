import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SupplierItemComponent } from '../list/supplier-item.component';
import { SupplierItemDetailComponent } from '../detail/supplier-item-detail.component';
import { SupplierItemUpdateComponent } from '../update/supplier-item-update.component';
import { SupplierItemRoutingResolveService } from './supplier-item-routing-resolve.service';

const supplierItemRoute: Routes = [
  {
    path: '',
    component: SupplierItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SupplierItemDetailComponent,
    resolve: {
      supplierItem: SupplierItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SupplierItemUpdateComponent,
    resolve: {
      supplierItem: SupplierItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SupplierItemUpdateComponent,
    resolve: {
      supplierItem: SupplierItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(supplierItemRoute)],
  exports: [RouterModule],
})
export class SupplierItemRoutingModule {}
