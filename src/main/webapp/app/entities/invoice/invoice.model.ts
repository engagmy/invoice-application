export interface IInvoice {
  id?: number;
  number?: string;
  value?: number;
}

export class Invoice implements IInvoice {
  constructor(public id?: number, public number?: string, public value?: number) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
