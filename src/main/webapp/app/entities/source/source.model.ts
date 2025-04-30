import { ICategory } from 'app/entities/category/category.model';

export interface ISource {
  id: number;
  name?: string | null;
  description?: string | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewSource = Omit<ISource, 'id'> & { id: null };
