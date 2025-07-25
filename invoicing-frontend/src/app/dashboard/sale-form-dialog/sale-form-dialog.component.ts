import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ProductService } from '../product.service';
import { Product } from '../sales.service';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-sale-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './sale-form-dialog.component.html',
  styleUrls: ['./sale-form-dialog.component.scss']
})
export class SaleFormDialogComponent implements OnInit {
  saleForm: FormGroup;
  products: Product[] = [];

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    public dialogRef: MatDialogRef<SaleFormDialogComponent>
  ) {
    this.saleForm = this.fb.group({
      customer: ['', Validators.required],
      status: ['Paid', Validators.required],
      productIds: [[], Validators.required]
    });
  }

  ngOnInit(): void {
    this.productService.getProducts().subscribe(data => {
      this.products = data;
    });
  }

  onSave(): void {
    if (this.saleForm.valid) {
      this.dialogRef.close(this.saleForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}