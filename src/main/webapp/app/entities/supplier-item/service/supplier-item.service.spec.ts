import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISupplierItem, SupplierItem } from '../supplier-item.model';

import { SupplierItemService } from './supplier-item.service';

describe('Service Tests', () => {
  describe('SupplierItem Service', () => {
    let service: SupplierItemService;
    let httpMock: HttpTestingController;
    let elemDefault: ISupplierItem;
    let expectedResult: ISupplierItem | ISupplierItem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SupplierItemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SupplierItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SupplierItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SupplierItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SupplierItem', () => {
        const patchObject = Object.assign({}, new SupplierItem());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SupplierItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SupplierItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSupplierItemToCollectionIfMissing', () => {
        it('should add a SupplierItem to an empty array', () => {
          const supplierItem: ISupplierItem = { id: 123 };
          expectedResult = service.addSupplierItemToCollectionIfMissing([], supplierItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(supplierItem);
        });

        it('should not add a SupplierItem to an array that contains it', () => {
          const supplierItem: ISupplierItem = { id: 123 };
          const supplierItemCollection: ISupplierItem[] = [
            {
              ...supplierItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addSupplierItemToCollectionIfMissing(supplierItemCollection, supplierItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SupplierItem to an array that doesn't contain it", () => {
          const supplierItem: ISupplierItem = { id: 123 };
          const supplierItemCollection: ISupplierItem[] = [{ id: 456 }];
          expectedResult = service.addSupplierItemToCollectionIfMissing(supplierItemCollection, supplierItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(supplierItem);
        });

        it('should add only unique SupplierItem to an array', () => {
          const supplierItemArray: ISupplierItem[] = [{ id: 123 }, { id: 456 }, { id: 40250 }];
          const supplierItemCollection: ISupplierItem[] = [{ id: 123 }];
          expectedResult = service.addSupplierItemToCollectionIfMissing(supplierItemCollection, ...supplierItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const supplierItem: ISupplierItem = { id: 123 };
          const supplierItem2: ISupplierItem = { id: 456 };
          expectedResult = service.addSupplierItemToCollectionIfMissing([], supplierItem, supplierItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(supplierItem);
          expect(expectedResult).toContain(supplierItem2);
        });

        it('should accept null and undefined values', () => {
          const supplierItem: ISupplierItem = { id: 123 };
          expectedResult = service.addSupplierItemToCollectionIfMissing([], null, supplierItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(supplierItem);
        });

        it('should return initial array if no SupplierItem is added', () => {
          const supplierItemCollection: ISupplierItem[] = [{ id: 123 }];
          expectedResult = service.addSupplierItemToCollectionIfMissing(supplierItemCollection, undefined, null);
          expect(expectedResult).toEqual(supplierItemCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
