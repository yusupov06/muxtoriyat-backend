import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISource, NewSource } from '../source.model';

export type PartialUpdateSource = Partial<ISource> & Pick<ISource, 'id'>;

export type EntityResponseType = HttpResponse<ISource>;
export type EntityArrayResponseType = HttpResponse<ISource[]>;

@Injectable({ providedIn: 'root' })
export class SourceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sources');

  create(source: NewSource): Observable<EntityResponseType> {
    return this.http.post<ISource>(this.resourceUrl, source, { observe: 'response' });
  }

  update(source: ISource): Observable<EntityResponseType> {
    return this.http.put<ISource>(`${this.resourceUrl}/${this.getSourceIdentifier(source)}`, source, { observe: 'response' });
  }

  partialUpdate(source: PartialUpdateSource): Observable<EntityResponseType> {
    return this.http.patch<ISource>(`${this.resourceUrl}/${this.getSourceIdentifier(source)}`, source, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSourceIdentifier(source: Pick<ISource, 'id'>): number {
    return source.id;
  }

  compareSource(o1: Pick<ISource, 'id'> | null, o2: Pick<ISource, 'id'> | null): boolean {
    return o1 && o2 ? this.getSourceIdentifier(o1) === this.getSourceIdentifier(o2) : o1 === o2;
  }

  addSourceToCollectionIfMissing<Type extends Pick<ISource, 'id'>>(
    sourceCollection: Type[],
    ...sourcesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sources: Type[] = sourcesToCheck.filter(isPresent);
    if (sources.length > 0) {
      const sourceCollectionIdentifiers = sourceCollection.map(sourceItem => this.getSourceIdentifier(sourceItem));
      const sourcesToAdd = sources.filter(sourceItem => {
        const sourceIdentifier = this.getSourceIdentifier(sourceItem);
        if (sourceCollectionIdentifiers.includes(sourceIdentifier)) {
          return false;
        }
        sourceCollectionIdentifiers.push(sourceIdentifier);
        return true;
      });
      return [...sourcesToAdd, ...sourceCollection];
    }
    return sourceCollection;
  }
}
