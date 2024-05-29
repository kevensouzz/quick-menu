import { Routes } from '@angular/router';
import { GenerateComponent } from './pages/generate/generate.component';
import { ScanComponent } from './pages/scan/scan.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AuthComponent } from './pages/auth/auth.component';
import { HomeComponent } from './pages/home/home.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AccountComponent } from './pages/account/account.component';
import { MenuComponent } from './pages/menu/menu.component';
import { NewMenuComponent } from './pages/generate/new-menu/new-menu.component';
import { UpdateMenuComponent } from './pages/generate/update-menu/update-menu.component';

export const routes: Routes = [
  { path: "home", component: HomeComponent },
  { path: "", pathMatch: "full", redirectTo: "home" },
  { path: "scan", component: ScanComponent },
  { path: "scan/:code", component: MenuComponent },
  { path: "generate", component: GenerateComponent },
  { path: "generate/new", component: NewMenuComponent },
  { path: "generate/update", component: UpdateMenuComponent },
  { path: "generate/account", component: AccountComponent },
  { path: "generate/auth", component: AuthComponent },
  { path: "admin", component: AdminComponent },
  { path: "**", component: NotFoundComponent }
];
