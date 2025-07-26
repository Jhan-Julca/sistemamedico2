import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class VentasService {
  private apiUrl = 'http://localhost:8010/api/venta';
  constructor(private http: HttpClient) {}

  getVentasResumen(): Observable<any> {
    // Ejemplo: obtener ventas y transformar para gráfico
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(ventas => {
        // Agrupar ventas por mes y sumar totales
        const resumen: { [mes: string]: number } = {};
        ventas.forEach(v => {
          const mes = new Date(v.fechaRegistro).toLocaleString('default', { month: 'short', year: 'numeric' });
          resumen[mes] = (resumen[mes] || 0) + v.precioTotal;
        });
        const labels = Object.keys(resumen);
        const data = Object.values(resumen);
        return {
          labels,
          datasets: [{ data, label: 'Ventas', backgroundColor: '#1976d2' }],
          options: { responsive: true },
          legend: true,
          type: 'bar'
        };
      })
    );
  }
}