import { Routes } from '@angular/router';
import { SalesHistoryComponent } from './sales-history/sales-history.component';

export const DASHBOARD_ROUTES: Routes = [
    { path: 'sales-history', component: SalesHistoryComponent },
    { path: '', redirectTo: 'sales-history', pathMatch: 'full' }
];