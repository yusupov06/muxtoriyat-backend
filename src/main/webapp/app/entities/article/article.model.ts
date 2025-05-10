import { ICategory } from 'app/entities/category/category.model';
import { IUser } from 'app/entities/user/user.model';
import { VisibilityType } from 'app/entities/enumerations/visibility-type.model';

export interface IArticle {
  id: number;
  name?: string | null;
  title?: string | null;
  description?: string | null;
  content?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  visibility?: keyof typeof VisibilityType | null;
  category?: Pick<ICategory, 'id'> | null;
  author?: Pick<IUser, 'id'> | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
