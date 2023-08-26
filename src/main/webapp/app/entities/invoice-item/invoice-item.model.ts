import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IItem } from 'app/entities/item/item.model';

export interface IInvoiceItem {
  id?: number;
  value?: number;
  invoice?: IInvoice | null;
  item?: IItem | null;
}

export class InvoiceItem implements IInvoiceItem {
  constructor(public id?: number, public value?: number, public invoice?: IInvoice | null, public item?: IItem | null) {}
}

export function getInvoiceItemIdentifier(invoiceItem: IInvoiceItem): number | undefined {
  return invoiceItem.id;
}
