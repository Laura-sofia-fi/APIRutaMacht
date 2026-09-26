import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConductoresComponent } from './components/conductores/conductores.component';
import { VehiculosComponent } from './components/vehiculos/vehiculos.component';
import { StatusService } from './services/status.service';
import { ToastService } from './services/toast.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ConductoresComponent, VehiculosComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private statusService = inject(StatusService);
  toastService = inject(ToastService);

  tab: 'conductores' | 'vehiculos' = 'conductores';
  apiArriba: boolean | null = null;

  ngOnInit(): void {
    this.revisarEstado();
    setInterval(() => this.revisarEstado(), 15000);
  }

  revisarEstado(): void {
    this.statusService.check().subscribe({
      next: () => (this.apiArriba = true),
      error: () => (this.apiArriba = false),
    });
  }
}
