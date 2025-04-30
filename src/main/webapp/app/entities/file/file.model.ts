import { ISource } from 'app/entities/source/source.model';
import { FileType } from 'app/entities/enumerations/file-type.model';

export interface IFile {
  id: number;
  name?: string | null;
  description?: string | null;
  url?: string | null;
  content?: string | null;
  contentContentType?: string | null;
  fileType?: keyof typeof FileType | null;
  source?: Pick<ISource, 'id'> | null;
}

export type NewFile = Omit<IFile, 'id'> & { id: null };
