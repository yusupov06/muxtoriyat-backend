export interface ICategory {
  id: number;
  name?: string | null;
  description?: string | null;
  order?: number | null;
  parent?: Pick<ICategory, 'id' | 'name'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
