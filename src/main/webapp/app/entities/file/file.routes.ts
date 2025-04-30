import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FileResolve from './route/file-routing-resolve.service';

const fileRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/file.component').then(m => m.FileComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/file-detail.component').then(m => m.FileDetailComponent),
    resolve: {
      file: FileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/file-update.component').then(m => m.FileUpdateComponent),
    resolve: {
      file: FileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/file-update.component').then(m => m.FileUpdateComponent),
    resolve: {
      file: FileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fileRoute;
