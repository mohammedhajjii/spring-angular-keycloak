import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ProductsComponent} from "./ui/products/products.component";
import {AuthGuard} from "./guards/auth.guard";

const routes: Routes = [
  {
    path: 'products',
    component: ProductsComponent,
    data: {
      roles: ['USER', 'ADMIN']
    },
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
