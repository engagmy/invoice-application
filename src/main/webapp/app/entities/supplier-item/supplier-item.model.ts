import { ISupplier } from 'app/entities/supplier/supplier.model';
import { IItem } from 'app/entities/item/item.model';

export interface ISupplierItem {
  id?: number;
  supplier?: ISupplier | null;
  item?: IItem | null;
}

export class SupplierItem implements ISupplierItem {
  constructor(public id?: number, public supplier?: ISupplier | null, public item?: IItem | null) {}
}

export function getSupplierItemIdentifier(supplierItem: ISupplierItem): number | undefined {
  return supplierItem.id;
}
