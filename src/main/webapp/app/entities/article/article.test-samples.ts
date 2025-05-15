import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 27792,
  name: 'yowza nor intently',
};

export const sampleWithPartialData: IArticle = {
  id: 28,
  name: 'valuable blah fashion',
  title: 'um in iterate',
};

export const sampleWithFullData: IArticle = {
  id: 18987,
  name: 'clearly even overload',
  title: 'develop tomography',
  description: 'wherever flint anenst',
  content: '../fake-data/blob/hipster.txt',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  visibility: 'PUBLIC',
};

export const sampleWithNewData: NewArticle = {
  name: 'celsius drag',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
