import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    {path: '', pathMatch: 'full', component: LoginComponent },
    {path: 'containers', loadChildren: () => import('./pages/containers/containers.routes').then(c => c.routes), canActivate: [authGuard] },
    {path: 'index', loadChildren: () => import('./pages/index/index.routes').then(c => c.routes), canActivate: [authGuard] }, 
    {path: '**', redirectTo: '' }
];
