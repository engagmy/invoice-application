jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInvoiceItem, InvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';

import { InvoiceItemRoutingResolveService } from './invoice-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('InvoiceItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InvoiceItemRoutingResolveService;
    let service: InvoiceItemService;
    let resultInvoiceItem: IInvoiceItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InvoiceItemRoutingResolveService);
      service = TestBed.inject(InvoiceItemService);
      resultInvoiceItem = undefined;
    });

    describe('resolve', () => {
      it('should return IInvoiceItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoiceItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvoiceItem).toEqual({ id: 123 });
      });

      it('should return new IInvoiceItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoiceItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInvoiceItem).toEqual(new InvoiceItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InvoiceItem })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoiceItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvoiceItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
