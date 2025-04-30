import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReaction } from '../reaction.model';
import { ReactionService } from '../service/reaction.service';

const reactionResolve = (route: ActivatedRouteSnapshot): Observable<null | IReaction> => {
  const id = route.params.id;
  if (id) {
    return inject(ReactionService)
      .find(id)
      .pipe(
        mergeMap((reaction: HttpResponse<IReaction>) => {
          if (reaction.body) {
            return of(reaction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reactionResolve;
