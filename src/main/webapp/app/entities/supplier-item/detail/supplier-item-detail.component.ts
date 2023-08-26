import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISupplierItem } from '../supplier-item.model';

@Component({
  selector: 'inv-supplier-item-detail',
  templateUrl: './supplier-item-detail.component.html',
})
export class SupplierItemDetailComponent implements OnInit {
  supplierItem: ISupplierItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierItem }) => {
      this.supplierItem = supplierItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
