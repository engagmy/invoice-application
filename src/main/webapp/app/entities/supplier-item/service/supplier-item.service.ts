import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISupplierItem, getSupplierItemIdentifier } from '../supplier-item.model';

export type EntityResponseType = HttpResponse<ISupplierItem>;
export type EntityArrayResponseType = HttpResponse<ISupplierItem[]>;

@Injectable({ providedIn: 'root' })
export class SupplierItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/supplier-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(supplierItem: ISupplierItem): Observable<EntityResponseType> {
    return this.http.post<ISupplierItem>(this.resourceUrl, supplierItem, { observe: 'response' });
  }

  update(supplierItem: ISupplierItem): Observable<EntityResponseType> {
    return this.http.put<ISupplierItem>(`${this.resourceUrl}/${getSupplierItemIdentifier(supplierItem) as number}`, supplierItem, {
      observe: 'response',
    });
  }

  partialUpdate(supplierItem: ISupplierItem): Observable<EntityResponseType> {
    return this.http.patch<ISupplierItem>(`${this.resourceUrl}/${getSupplierItemIdentifier(supplierItem) as number}`, supplierItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISupplierItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISupplierItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSupplierItemToCollectionIfMissing(
    supplierItemCollection: ISupplierItem[],
    ...supplierItemsToCheck: (ISupplierItem | null | undefined)[]
  ): ISupplierItem[] {
    const supplierItems: ISupplierItem[] = supplierItemsToCheck.filter(isPresent);
    if (supplierItems.length > 0) {
      const supplierItemCollectionIdentifiers = supplierItemCollection.map(
        supplierItemItem => getSupplierItemIdentifier(supplierItemItem)!
      );
      const supplierItemsToAdd = supplierItems.filter(supplierItemItem => {
        const supplierItemIdentifier = getSupplierItemIdentifier(supplierItemItem);
        if (supplierItemIdentifier == null || supplierItemCollectionIdentifiers.includes(supplierItemIdentifier)) {
          return false;
        }
        supplierItemCollectionIdentifiers.push(supplierItemIdentifier);
        return true;
      });
      return [...supplierItemsToAdd, ...supplierItemCollection];
    }
    return supplierItemCollection;
  }
}
