import { Component } from '@angular/core';

@Component({
  selector: 'app-error',
  template: `<div class='error-container'><mat-card><mat-card-title>Error</mat-card-title><mat-card-content>No tienes acceso o la ruta no existe.</mat-card-content></mat-card></div>`,
  styles: [`.error-container { display: flex; justify-content: center; align-items: center; height: 80vh; } mat-card { padding: 2rem; }`]
})
export class ErrorComponent {}