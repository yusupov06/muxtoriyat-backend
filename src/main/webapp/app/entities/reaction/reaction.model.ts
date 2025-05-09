import { ReactionType } from 'app/entities/enumerations/reaction-type.model';

export interface IReaction {
  id: number;
  deviceId?: string | null;
  targetId?: number | null;
  reactionType?: keyof typeof ReactionType | null;
}

export type NewReaction = Omit<IReaction, 'id'> & { id: null };
