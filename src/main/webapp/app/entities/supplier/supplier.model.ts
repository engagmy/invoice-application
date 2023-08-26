export interface ISupplier {
  id?: number;
  name?: string;
}

export class Supplier implements ISupplier {
  constructor(public id?: number, public name?: string) {}
}

export function getSupplierIdentifier(supplier: ISupplier): number | undefined {
  return supplier.id;
}
