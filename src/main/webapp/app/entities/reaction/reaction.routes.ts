import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReactionResolve from './route/reaction-routing-resolve.service';

const reactionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/reaction.component').then(m => m.ReactionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/reaction-detail.component').then(m => m.ReactionDetailComponent),
    resolve: {
      reaction: ReactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/reaction-update.component').then(m => m.ReactionUpdateComponent),
    resolve: {
      reaction: ReactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/reaction-update.component').then(m => m.ReactionUpdateComponent),
    resolve: {
      reaction: ReactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reactionRoute;
