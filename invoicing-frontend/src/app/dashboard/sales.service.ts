import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  name: string;
  manufacturer: string;
  price: number;
  entryDate: string;
}

export interface Sale {
  id: number;
  invoiceId: string;
  customer: string;
  date: string;
  status: string;
  products: Product[];
}

@Injectable({
  providedIn: 'root'
})
export class SalesService {
  private baseUrl = 'http://localhost:8080/v1/api/sales';

  constructor(private http: HttpClient) { }

  getSales(): Observable<Sale[]> {
    return this.http.get<Sale[]>(this.baseUrl, { withCredentials: true });
  }

  createSale(saleData: any): Observable<Sale> {
    return this.http.post<Sale>(this.baseUrl, saleData, { withCredentials: true });
  }
}