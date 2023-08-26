import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SupplierItemService } from '../service/supplier-item.service';

import { SupplierItemComponent } from './supplier-item.component';

describe('Component Tests', () => {
  describe('SupplierItem Management Component', () => {
    let comp: SupplierItemComponent;
    let fixture: ComponentFixture<SupplierItemComponent>;
    let service: SupplierItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SupplierItemComponent],
      })
        .overrideTemplate(SupplierItemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SupplierItemComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SupplierItemService);

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
      expect(comp.supplierItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
