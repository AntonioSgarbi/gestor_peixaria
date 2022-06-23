import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {PessoaModule} from './pages/pessoa/pessoa.module';
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {NavbarModule} from "./components/navbar/navbar.module";
import {HttpClientModule} from "@angular/common/http";
import {HomeModule} from "./pages/home/home.module";
import {ProdutoModule} from "./pages/produto/produto.module";
import {EstoqueModule} from "./pages/estoque/estoque.module";
import {InterceptorModule} from "./interceptor/interceptor.module";
import { LoginComponent } from './pages/login/login.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    PessoaModule,
    NavbarModule,
    HomeModule,
    ProdutoModule,
    EstoqueModule,
    InterceptorModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule

  ],
  providers: [
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'pt-BR'
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
