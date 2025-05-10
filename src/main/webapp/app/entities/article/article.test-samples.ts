import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 11289,
  name: 'gadzooks worth nor',
};

export const sampleWithPartialData: IArticle = {
  id: 25621,
  name: 'ah ick satisfy',
  title: 'vacantly runny but',
  description: 'till but likewise',
};

export const sampleWithFullData: IArticle = {
  id: 2924,
  name: 'innocently testing mechanically',
  title: 'step',
  description: 'drug',
  content: '../fake-data/blob/hipster.txt',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  visibility: 'PUBLIC',
};

export const sampleWithNewData: NewArticle = {
  name: 'grok',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
