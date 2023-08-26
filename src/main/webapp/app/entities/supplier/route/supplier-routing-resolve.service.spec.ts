jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISupplier, Supplier } from '../supplier.model';
import { SupplierService } from '../service/supplier.service';

import { SupplierRoutingResolveService } from './supplier-routing-resolve.service';

describe('Service Tests', () => {
  describe('Supplier routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SupplierRoutingResolveService;
    let service: SupplierService;
    let resultSupplier: ISupplier | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SupplierRoutingResolveService);
      service = TestBed.inject(SupplierService);
      resultSupplier = undefined;
    });

    describe('resolve', () => {
      it('should return ISupplier returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSupplier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSupplier).toEqual({ id: 123 });
      });

      it('should return new ISupplier if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSupplier = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSupplier).toEqual(new Supplier());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Supplier })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSupplier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSupplier).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
