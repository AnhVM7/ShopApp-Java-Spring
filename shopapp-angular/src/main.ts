import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';

import { HomeComponent } from './app/home/home.component';
import { OrderComponent } from './app/order/order.component';
import { HeaderComponent } from './app/header/header.component';
import { FooterComponent } from './app/footer/footer.component';
import { OrderConfirmComponent } from './app/order-confirm/order-confirm.component';
import { LoginComponent } from './app/login/login.component';
import { RegisterComponent } from './app/register/register.component';
import { DetailProductComponent } from './app/detail-product/detail-product.component';
import { FormsModule } from '@angular/forms';

Promise.all([
  bootstrapApplication(OrderComponent, appConfig),
  bootstrapApplication(HomeComponent, appConfig),
  bootstrapApplication(HeaderComponent, appConfig),
  bootstrapApplication(FooterComponent, appConfig),
  bootstrapApplication(OrderConfirmComponent, appConfig),
  bootstrapApplication(LoginComponent, appConfig),
  bootstrapApplication(RegisterComponent, appConfig),
  bootstrapApplication(DetailProductComponent, appConfig),
  bootstrapApplication(FormsModule)
])
.then(() => {
  console.log('Both components bootstrapped');
})
.catch((err) => console.error(err));
