import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
    {path: '', pathMatch: 'full', component: LoginComponent },
    {path: 'containers', loadChildren: () => import('./containers/containers.routes').then(c => c.routes) },
    {path: 'index', loadChildren: () => import('./index/index.routes').then(c => c.routes) },
    {path: '**', component: LoginComponent }
];
