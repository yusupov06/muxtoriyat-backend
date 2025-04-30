import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 19629,
};

export const sampleWithPartialData: ICategory = {
  id: 17987,
  name: 'obscure',
  order: 14518,
};

export const sampleWithFullData: ICategory = {
  id: 14290,
  name: 'certainly fake',
  description: 'upon along',
  order: 21246,
};

export const sampleWithNewData: NewCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
