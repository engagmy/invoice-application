jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISupplierItem, SupplierItem } from '../supplier-item.model';
import { SupplierItemService } from '../service/supplier-item.service';

import { SupplierItemRoutingResolveService } from './supplier-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('SupplierItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SupplierItemRoutingResolveService;
    let service: SupplierItemService;
    let resultSupplierItem: ISupplierItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SupplierItemRoutingResolveService);
      service = TestBed.inject(SupplierItemService);
      resultSupplierItem = undefined;
    });

    describe('resolve', () => {
      it('should return ISupplierItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSupplierItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSupplierItem).toEqual({ id: 123 });
      });

      it('should return new ISupplierItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSupplierItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSupplierItem).toEqual(new SupplierItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SupplierItem })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSupplierItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSupplierItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
