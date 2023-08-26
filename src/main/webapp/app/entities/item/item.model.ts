export interface IItem {
  id?: number;
  name?: string;
  active?: boolean | null;
}

export class Item implements IItem {
  constructor(public id?: number, public name?: string, public active?: boolean | null) {
    this.active = this.active ?? false;
  }
}

export function getItemIdentifier(item: IItem): number | undefined {
  return item.id;
}
