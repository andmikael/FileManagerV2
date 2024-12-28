import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    {path: '', pathMatch: 'full', component: LoginComponent },
    {path: 'containers', loadChildren: () => import('./containers/containers.routes').then(c => c.routes), canActivate: [authGuard] },
    {path: 'index', loadChildren: () => import('./index/index.routes').then(c => c.routes), canActivate: [authGuard] }, 
    {path: '**', redirectTo: '' }
];
