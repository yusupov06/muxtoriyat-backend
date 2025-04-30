import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SourceResolve from './route/source-routing-resolve.service';

const sourceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/source.component').then(m => m.SourceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/source-detail.component').then(m => m.SourceDetailComponent),
    resolve: {
      source: SourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/source-update.component').then(m => m.SourceUpdateComponent),
    resolve: {
      source: SourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/source-update.component').then(m => m.SourceUpdateComponent),
    resolve: {
      source: SourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sourceRoute;
