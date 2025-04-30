import { IArticle } from 'app/entities/article/article.model';
import { ReactionType } from 'app/entities/enumerations/reaction-type.model';

export interface IReaction {
  id: number;
  deviceId?: string | null;
  reactionType?: keyof typeof ReactionType | null;
  article?: Pick<IArticle, 'id'> | null;
}

export type NewReaction = Omit<IReaction, 'id'> & { id: null };
