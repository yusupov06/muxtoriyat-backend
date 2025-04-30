import { ISource, NewSource } from './source.model';

export const sampleWithRequiredData: ISource = {
  id: 14328,
};

export const sampleWithPartialData: ISource = {
  id: 32510,
  description: 'yet blah stock',
};

export const sampleWithFullData: ISource = {
  id: 27159,
  name: 'barge cool',
  description: 'consequently yuck loyally',
};

export const sampleWithNewData: NewSource = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
