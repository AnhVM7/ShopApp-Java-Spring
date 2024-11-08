import { Component, ViewChild } from '@angular/core';
import { FooterComponent } from "../footer/footer.component";
import { HeaderComponent } from "../header/header.component";
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FooterComponent, HeaderComponent, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  // khai bao cac bien tuong ung voi cac truong du lieu trong form
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor(){
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
  }
  onPhoneChange(){
    console.log(`phone typed: ${this.phone}`)

  }
  register(){
    const message = `phone: ${this.phone}` 
                    + `password: ${this.password}`
                    + `retypePassword: ${this.retypePassword}`
                    + `fullName: ${this.fullName}`
                    + `address: ${this.address}`
                    + `isAccepted: ${this.isAccepted}`;

    alert(message);
  }

  checkPasswordMatch(){
    if(this.password !== this.retypePassword){
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch' : true});
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }
}
