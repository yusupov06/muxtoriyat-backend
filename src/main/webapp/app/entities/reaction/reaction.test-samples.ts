import { IReaction, NewReaction } from './reaction.model';

export const sampleWithRequiredData: IReaction = {
  id: 25789,
};

export const sampleWithPartialData: IReaction = {
  id: 14400,
  deviceId: 'around gee emulsify',
  targetId: 16199,
  reactionType: 'DISLIKE',
};

export const sampleWithFullData: IReaction = {
  id: 7097,
  deviceId: 'although hourly',
  targetId: 10958,
  reactionType: 'LIKE',
};

export const sampleWithNewData: NewReaction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
