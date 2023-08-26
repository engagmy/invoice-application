import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InvoiceItemService } from '../service/invoice-item.service';

import { InvoiceItemComponent } from './invoice-item.component';

describe('Component Tests', () => {
  describe('InvoiceItem Management Component', () => {
    let comp: InvoiceItemComponent;
    let fixture: ComponentFixture<InvoiceItemComponent>;
    let service: InvoiceItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InvoiceItemComponent],
      })
        .overrideTemplate(InvoiceItemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceItemComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InvoiceItemService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.invoiceItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
