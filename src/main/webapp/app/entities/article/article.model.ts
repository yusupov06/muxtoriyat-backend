import { ICategory } from 'app/entities/category/category.model';

export interface IArticle {
  id: number;
  name?: string | null;
  title?: string | null;
  description?: string | null;
  content?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
