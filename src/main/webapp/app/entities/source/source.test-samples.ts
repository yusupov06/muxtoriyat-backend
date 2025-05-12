import { ISource, NewSource } from './source.model';

export const sampleWithRequiredData: ISource = {
  id: 30711,
};

export const sampleWithPartialData: ISource = {
  id: 16369,
  name: 'compete',
};

export const sampleWithFullData: ISource = {
  id: 669,
  name: 'for usable',
  description: 'happily',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewSource = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
