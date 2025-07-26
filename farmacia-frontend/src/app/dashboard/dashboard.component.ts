import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { VentasService } from '../ventas/ventas.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  user = this.auth.getUser();
  isVendedorOrAdmin = false;
  ventasData: any;
  loading = false;
  error: string | null = null;

  constructor(private auth: AuthService, private ventasService: VentasService) {}

  ngOnInit() {
    this.isVendedorOrAdmin = this.auth.hasRole(['ADMIN', 'VENDEDOR']);
    if (this.isVendedorOrAdmin) {
      this.loading = true;
      this.ventasService.getVentasResumen().subscribe({
        next: data => {
          this.ventasData = data;
          this.loading = false;
        },
        error: err => {
          this.error = 'No se pudo cargar el resumen de ventas';
          this.loading = false;
        }
      });
    }
  }
}