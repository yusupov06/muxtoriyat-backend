import { IFile, NewFile } from './file.model';

export const sampleWithRequiredData: IFile = {
  id: 527,
  name: 'gee',
};

export const sampleWithPartialData: IFile = {
  id: 31165,
  name: 'disappointment despite',
  description: 'well-made',
  url: 'https://bright-puppet.com',
  content: '../fake-data/blob/hipster.png',
  contentContentType: 'unknown',
  fileType: 'PDF',
};

export const sampleWithFullData: IFile = {
  id: 15404,
  name: 'than badly',
  description: 'tidy',
  url: 'https://smart-hundred.info',
  content: '../fake-data/blob/hipster.png',
  contentContentType: 'unknown',
  fileType: 'IMAGE',
};

export const sampleWithNewData: NewFile = {
  name: 'plus incidentally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
