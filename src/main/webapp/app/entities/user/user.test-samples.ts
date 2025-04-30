import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 3577,
  login: 'RG62Tl',
};

export const sampleWithPartialData: IUser = {
  id: 21397,
  login: '&@7DiRzi\\uPyK0\\lVWRuU\\%Mon\\&M',
};

export const sampleWithFullData: IUser = {
  id: 2883,
  login: 'MQIqnH@ISy\\y4Olw\\L05\\%G',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
