import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReaction, NewReaction } from '../reaction.model';

export type PartialUpdateReaction = Partial<IReaction> & Pick<IReaction, 'id'>;

export type EntityResponseType = HttpResponse<IReaction>;
export type EntityArrayResponseType = HttpResponse<IReaction[]>;

@Injectable({ providedIn: 'root' })
export class ReactionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reactions');

  create(reaction: NewReaction): Observable<EntityResponseType> {
    return this.http.post<IReaction>(this.resourceUrl, reaction, { observe: 'response' });
  }

  update(reaction: IReaction): Observable<EntityResponseType> {
    return this.http.put<IReaction>(`${this.resourceUrl}/${this.getReactionIdentifier(reaction)}`, reaction, { observe: 'response' });
  }

  partialUpdate(reaction: PartialUpdateReaction): Observable<EntityResponseType> {
    return this.http.patch<IReaction>(`${this.resourceUrl}/${this.getReactionIdentifier(reaction)}`, reaction, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReaction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReaction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReactionIdentifier(reaction: Pick<IReaction, 'id'>): number {
    return reaction.id;
  }

  compareReaction(o1: Pick<IReaction, 'id'> | null, o2: Pick<IReaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getReactionIdentifier(o1) === this.getReactionIdentifier(o2) : o1 === o2;
  }

  addReactionToCollectionIfMissing<Type extends Pick<IReaction, 'id'>>(
    reactionCollection: Type[],
    ...reactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reactions: Type[] = reactionsToCheck.filter(isPresent);
    if (reactions.length > 0) {
      const reactionCollectionIdentifiers = reactionCollection.map(reactionItem => this.getReactionIdentifier(reactionItem));
      const reactionsToAdd = reactions.filter(reactionItem => {
        const reactionIdentifier = this.getReactionIdentifier(reactionItem);
        if (reactionCollectionIdentifiers.includes(reactionIdentifier)) {
          return false;
        }
        reactionCollectionIdentifiers.push(reactionIdentifier);
        return true;
      });
      return [...reactionsToAdd, ...reactionCollection];
    }
    return reactionCollection;
  }
}
