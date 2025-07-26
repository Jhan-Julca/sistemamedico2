import { Component, OnInit } from '@angular/core';
import { VentasService } from './ventas.service';

@Component({
  selector: 'app-ventas',
  templateUrl: './ventas.component.html',
  styleUrls: ['./ventas.component.scss']
})
export class VentasComponent implements OnInit {
  ventas: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(private ventasService: VentasService) {}

  ngOnInit() {
    this.loading = true;
    this.ventasService.getVentasResumen().subscribe({
      next: data => {
        this.ventas = data;
        this.loading = false;
      },
      error: err => {
        this.error = 'No se pudo cargar las ventas';
        this.loading = false;
      }
    });
  }
}