import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SupplierItemDetailComponent } from './supplier-item-detail.component';

describe('Component Tests', () => {
  describe('SupplierItem Management Detail Component', () => {
    let comp: SupplierItemDetailComponent;
    let fixture: ComponentFixture<SupplierItemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SupplierItemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ supplierItem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SupplierItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load supplierItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.supplierItem).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
