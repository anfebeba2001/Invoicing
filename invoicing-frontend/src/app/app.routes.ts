import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'auth',
        loadChildren: () => import('./auth/auth.routes').then(mod => mod.AUTH_ROUTES)
    },
    {
        path: '',
        redirectTo: 'auth', 
        pathMatch: 'full'
    }
];