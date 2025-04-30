import { IReaction, NewReaction } from './reaction.model';

export const sampleWithRequiredData: IReaction = {
  id: 16739,
};

export const sampleWithPartialData: IReaction = {
  id: 1661,
  reactionType: 'LIKE',
};

export const sampleWithFullData: IReaction = {
  id: 14400,
  deviceId: 'around gee emulsify',
  reactionType: 'LIKE',
};

export const sampleWithNewData: NewReaction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
