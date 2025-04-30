import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'muxtoriyatApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'article',
    data: { pageTitle: 'muxtoriyatApp.article.home.title' },
    loadChildren: () => import('./article/article.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'muxtoriyatApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'file',
    data: { pageTitle: 'muxtoriyatApp.file.home.title' },
    loadChildren: () => import('./file/file.routes'),
  },
  {
    path: 'reaction',
    data: { pageTitle: 'muxtoriyatApp.reaction.home.title' },
    loadChildren: () => import('./reaction/reaction.routes'),
  },
  {
    path: 'source',
    data: { pageTitle: 'muxtoriyatApp.source.home.title' },
    loadChildren: () => import('./source/source.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
