import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '3039e522-65f1-4b8d-8ac0-dd8e491720dc',
};

export const sampleWithPartialData: IAuthority = {
  name: '99f9ed93-47bf-49ea-bac1-d89271ebb16b',
};

export const sampleWithFullData: IAuthority = {
  name: '2a724a41-2eb2-42e3-82cb-0a52f417f513',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
