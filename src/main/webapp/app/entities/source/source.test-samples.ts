import { ISource, NewSource } from './source.model';

export const sampleWithRequiredData: ISource = {
  id: 30907,
};

export const sampleWithPartialData: ISource = {
  id: 32539,
  name: 'huzzah',
  description: 'hateful',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  fileUrl: 'consequently towards consequently',
  fileType: 'IMAGE',
};

export const sampleWithFullData: ISource = {
  id: 8920,
  name: 'acidly plus',
  description: 'sniveling coincide',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  fileUrl: 'where in tut',
  fileContent: '../fake-data/blob/hipster.png',
  fileContentContentType: 'unknown',
  fileType: 'IMAGE',
};

export const sampleWithNewData: NewSource = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
