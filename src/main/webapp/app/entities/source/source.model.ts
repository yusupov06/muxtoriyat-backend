import { ICategory } from 'app/entities/category/category.model';
import { FileType } from 'app/entities/enumerations/file-type.model';

export interface ISource {
  id: number;
  name?: string | null;
  description?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  fileUrl?: string | null;
  fileContent?: string | null;
  fileContentContentType?: string | null;
  fileType?: keyof typeof FileType | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewSource = Omit<ISource, 'id'> & { id: null };
