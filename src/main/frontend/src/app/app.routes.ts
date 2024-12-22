import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ContainersComponent } from './containers/containers.component';
import { IndexComponent } from './index/index.component';

export const routes: Routes = [
    {path: '', component: LoginComponent },
    {path: 'containers', component: ContainersComponent },
    {path: 'index', component: IndexComponent },
];
