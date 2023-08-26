import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceItem, InvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';

@Injectable({ providedIn: 'root' })
export class InvoiceItemRoutingResolveService implements Resolve<IInvoiceItem> {
  constructor(protected service: InvoiceItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invoiceItem: HttpResponse<InvoiceItem>) => {
          if (invoiceItem.body) {
            return of(invoiceItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvoiceItem());
  }
}
