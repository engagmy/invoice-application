import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISupplierItem, SupplierItem } from '../supplier-item.model';
import { SupplierItemService } from '../service/supplier-item.service';

@Injectable({ providedIn: 'root' })
export class SupplierItemRoutingResolveService implements Resolve<ISupplierItem> {
  constructor(protected service: SupplierItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISupplierItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((supplierItem: HttpResponse<SupplierItem>) => {
          if (supplierItem.body) {
            return of(supplierItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SupplierItem());
  }
}
