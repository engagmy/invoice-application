import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceItem, getInvoiceItemIdentifier } from '../invoice-item.model';

export type EntityResponseType = HttpResponse<IInvoiceItem>;
export type EntityArrayResponseType = HttpResponse<IInvoiceItem[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceItem: IInvoiceItem): Observable<EntityResponseType> {
    return this.http.post<IInvoiceItem>(this.resourceUrl, invoiceItem, { observe: 'response' });
  }

  update(invoiceItem: IInvoiceItem): Observable<EntityResponseType> {
    return this.http.put<IInvoiceItem>(`${this.resourceUrl}/${getInvoiceItemIdentifier(invoiceItem) as number}`, invoiceItem, {
      observe: 'response',
    });
  }

  partialUpdate(invoiceItem: IInvoiceItem): Observable<EntityResponseType> {
    return this.http.patch<IInvoiceItem>(`${this.resourceUrl}/${getInvoiceItemIdentifier(invoiceItem) as number}`, invoiceItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvoiceItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvoiceItemToCollectionIfMissing(
    invoiceItemCollection: IInvoiceItem[],
    ...invoiceItemsToCheck: (IInvoiceItem | null | undefined)[]
  ): IInvoiceItem[] {
    const invoiceItems: IInvoiceItem[] = invoiceItemsToCheck.filter(isPresent);
    if (invoiceItems.length > 0) {
      const invoiceItemCollectionIdentifiers = invoiceItemCollection.map(invoiceItemItem => getInvoiceItemIdentifier(invoiceItemItem)!);
      const invoiceItemsToAdd = invoiceItems.filter(invoiceItemItem => {
        const invoiceItemIdentifier = getInvoiceItemIdentifier(invoiceItemItem);
        if (invoiceItemIdentifier == null || invoiceItemCollectionIdentifiers.includes(invoiceItemIdentifier)) {
          return false;
        }
        invoiceItemCollectionIdentifiers.push(invoiceItemIdentifier);
        return true;
      });
      return [...invoiceItemsToAdd, ...invoiceItemCollection];
    }
    return invoiceItemCollection;
  }
}
