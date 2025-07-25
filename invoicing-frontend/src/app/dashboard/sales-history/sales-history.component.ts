import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SaleFormDialogComponent } from '../sale-form-dialog/sale-form-dialog.component';
import { Sale, SalesService, Product } from '../sales.service';

// Módulos de Angular Material
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-sales-history',
  standalone: true,
  imports: [
    CommonModule, CurrencyPipe, DatePipe, MatTableModule, MatButtonModule,
    MatIconModule, MatChipsModule, MatCardModule, MatDialogModule
  ],
  templateUrl: './sales-history.component.html',
  styleUrls: ['./sales-history.component.scss']
})
export class SalesHistoryComponent implements OnInit {

  displayedColumns: string[] = ['invoiceId', 'customer', 'date', 'amount', 'status', 'actions'];
  dataSource: Sale[] = [];

  constructor(private salesService: SalesService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadSales();
  }

  loadSales(): void {
    this.salesService.getSales().subscribe(data => {
      this.dataSource = data;
    });
  }

  openAddSaleDialog(): void {
    const dialogRef = this.dialog.open(SaleFormDialogComponent, {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.salesService.createSale(result).subscribe(() => {
          console.log('Venta creada exitosamente');
          this.loadSales(); 
        });
      }
    });
  }

  calculateTotal(products: Product[]): number {
    if (!products) return 0;
    return products.reduce((acc, product) => acc + product.price, 0);
  }

  getStatusColor(status: string): string {
    return status?.toLowerCase() === 'paid' ? 'primary' : 'warn';
  }
}