import { Routes } from '@angular/router';
import { ScanComponent } from './pages/scan/scan.component';
import { GenerateComponent } from './pages/generate/generate.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { AuthComponent } from './pages/auth/auth.component';

export const routes: Routes = [
  { path: "scan", component: ScanComponent },
  { path: "", pathMatch: "full", redirectTo: "scan" },
  { path: "generate", component: GenerateComponent},
  { path: "generate/auth", component: AuthComponent },
  { path: "**", component: NotFoundComponent }
];
