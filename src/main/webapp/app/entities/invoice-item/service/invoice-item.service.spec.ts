import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInvoiceItem, InvoiceItem } from '../invoice-item.model';

import { InvoiceItemService } from './invoice-item.service';

describe('Service Tests', () => {
  describe('InvoiceItem Service', () => {
    let service: InvoiceItemService;
    let httpMock: HttpTestingController;
    let elemDefault: IInvoiceItem;
    let expectedResult: IInvoiceItem | IInvoiceItem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InvoiceItemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        value: 0,
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

      it('should create a InvoiceItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new InvoiceItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a InvoiceItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            value: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a InvoiceItem', () => {
        const patchObject = Object.assign(
          {
            value: 1,
          },
          new InvoiceItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of InvoiceItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            value: 1,
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

      it('should delete a InvoiceItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInvoiceItemToCollectionIfMissing', () => {
        it('should add a InvoiceItem to an empty array', () => {
          const invoiceItem: IInvoiceItem = { id: 123 };
          expectedResult = service.addInvoiceItemToCollectionIfMissing([], invoiceItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invoiceItem);
        });

        it('should not add a InvoiceItem to an array that contains it', () => {
          const invoiceItem: IInvoiceItem = { id: 123 };
          const invoiceItemCollection: IInvoiceItem[] = [
            {
              ...invoiceItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addInvoiceItemToCollectionIfMissing(invoiceItemCollection, invoiceItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a InvoiceItem to an array that doesn't contain it", () => {
          const invoiceItem: IInvoiceItem = { id: 123 };
          const invoiceItemCollection: IInvoiceItem[] = [{ id: 456 }];
          expectedResult = service.addInvoiceItemToCollectionIfMissing(invoiceItemCollection, invoiceItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invoiceItem);
        });

        it('should add only unique InvoiceItem to an array', () => {
          const invoiceItemArray: IInvoiceItem[] = [{ id: 123 }, { id: 456 }, { id: 97674 }];
          const invoiceItemCollection: IInvoiceItem[] = [{ id: 123 }];
          expectedResult = service.addInvoiceItemToCollectionIfMissing(invoiceItemCollection, ...invoiceItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const invoiceItem: IInvoiceItem = { id: 123 };
          const invoiceItem2: IInvoiceItem = { id: 456 };
          expectedResult = service.addInvoiceItemToCollectionIfMissing([], invoiceItem, invoiceItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(invoiceItem);
          expect(expectedResult).toContain(invoiceItem2);
        });

        it('should accept null and undefined values', () => {
          const invoiceItem: IInvoiceItem = { id: 123 };
          expectedResult = service.addInvoiceItemToCollectionIfMissing([], null, invoiceItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(invoiceItem);
        });

        it('should return initial array if no InvoiceItem is added', () => {
          const invoiceItemCollection: IInvoiceItem[] = [{ id: 123 }];
          expectedResult = service.addInvoiceItemToCollectionIfMissing(invoiceItemCollection, undefined, null);
          expect(expectedResult).toEqual(invoiceItemCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
