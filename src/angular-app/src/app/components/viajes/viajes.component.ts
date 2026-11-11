import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Conductor } from '../../models/conductor.model';
import { Vehiculo } from '../../models/vehiculo.model';
import { CrearViajeRequest, Viaje } from '../../models/viaje.model';
import { ConductorService } from '../../services/conductor.service';
import { VehiculoService } from '../../services/vehiculo.service';
import { ViajeService } from '../../services/viaje.service';
import { ToastService } from '../../services/toast.service';

interface FormularioViaje {
  conductorId: string;
  vehiculoId: string;
  origen: string;
  destino: string;
  paradasTexto: string;
  fechaHoraSalida: string;
  cupos: number;
  aportePorCupo: number;
}

function formularioVacio(): FormularioViaje {
  return {
    conductorId: '', vehiculoId: '', origen: '', destino: '', paradasTexto: '',
    fechaHoraSalida: '', cupos: 1, aportePorCupo: 0,
  };
}

@Component({
  selector: 'app-viajes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './viajes.component.html',
})
export class ViajesComponent implements OnInit {
  private conductoresApi = inject(ConductorService);
  private vehiculosApi = inject(VehiculoService);
  private viajesApi = inject(ViajeService);
  private toast = inject(ToastService);
  private cdr = inject(ChangeDetectorRef);

  conductores: Conductor[] = [];
  vehiculos: Vehiculo[] = [];
  viajes: Viaje[] = [];
  form = formularioVacio();
  busquedaOrigen = '';
  busquedaDestino = '';
  modalAbierto = false;
  guardando = false;

  get vehiculosAptos(): Vehiculo[] {
    return this.vehiculos.filter(v => (v.capacidadTotal ?? 0) >= 2);
  }

  get maximoCupos(): number {
    const vehiculo = this.vehiculos.find(v => v.id === this.form.vehiculoId);
    return Math.max(0, (vehiculo?.capacidadTotal ?? 1) - 1);
  }

  get fechaMinima(): string {
    const local = new Date(Date.now() - new Date().getTimezoneOffset() * 60000);
    return local.toISOString().slice(0, 16);
  }

  ngOnInit(): void {
    this.cargarCatalogos();
    this.buscar();
  }

  cargarCatalogos(): void {
    this.conductoresApi.listar().subscribe({
      next: res => {
        this.conductores = (res.conductores || []).filter(c => c.estado !== false);
        this.cdr.detectChanges();
      },
      error: e => this.toast.show('No se pudieron cargar los conductores: ' + this.mensaje(e), true),
    });
    this.vehiculosApi.listar().subscribe({
      next: res => {
        this.vehiculos = res.vehiculos || [];
        this.cdr.detectChanges();
      },
      error: e => this.toast.show('No se pudieron cargar los vehículos: ' + this.mensaje(e), true),
    });
  }

  buscar(): void {
    this.viajesApi.buscar(this.busquedaOrigen, this.busquedaDestino).subscribe({
      next: res => { this.viajes = res.viajes || []; this.cdr.detectChanges(); },
      error: e => this.toast.show('No se pudieron cargar los viajes: ' + this.mensaje(e), true),
    });
  }

  abrirNuevo(): void {
    this.form = formularioVacio();
    this.modalAbierto = true;
  }

  cerrar(): void {
    this.modalAbierto = false;
    this.guardando = false;
  }

  alCambiarVehiculo(): void {
    if (this.form.cupos > this.maximoCupos) this.form.cupos = this.maximoCupos || 1;
  }

  guardar(): void {
    if (!this.form.conductorId || !this.form.vehiculoId || !this.form.origen.trim() ||
        !this.form.destino.trim() || !this.form.fechaHoraSalida) {
      this.toast.show('Completa conductor, vehículo, origen, destino y fecha de salida.', true);
      return;
    }
    if (this.form.origen.trim().toLowerCase() === this.form.destino.trim().toLowerCase()) {
      this.toast.show('El origen y el destino deben ser diferentes.', true);
      return;
    }
    if (this.form.cupos < 1 || this.form.cupos > this.maximoCupos) {
      this.toast.show(`El vehículo permite máximo ${this.maximoCupos} cupos para pasajeros.`, true);
      return;
    }
    if (this.form.aportePorCupo < 0) {
      this.toast.show('El aporte por cupo no puede ser negativo.', true);
      return;
    }

    const request: CrearViajeRequest = {
      conductorId: this.form.conductorId,
      vehiculoId: this.form.vehiculoId,
      origen: this.form.origen.trim(),
      destino: this.form.destino.trim(),
      paradas: this.form.paradasTexto.split('\n').map(p => p.trim()).filter(Boolean),
      fechaHoraSalida: new Date(this.form.fechaHoraSalida).toISOString(),
      cupos: this.form.cupos,
      aportePorCupo: this.form.aportePorCupo,
    };

    this.guardando = true;
    this.viajesApi.publicar(request).subscribe({
      next: () => {
        this.toast.show('Viaje publicado correctamente.');
        this.cerrar();
        this.buscar();
      },
      error: e => { this.guardando = false; this.toast.show(this.mensaje(e), true); },
    });
  }

  cancelar(viaje: Viaje): void {
    if (!confirm(`¿Cancelar el viaje ${viaje.origen} → ${viaje.destino}?`)) return;
    this.viajesApi.cancelar(viaje.id).subscribe({
      next: () => { this.toast.show('Viaje cancelado.'); this.buscar(); },
      error: e => this.toast.show(this.mensaje(e), true),
    });
  }

  nombreConductor(id: string): string {
    const conductor = this.conductores.find(c => c.id === id);
    return conductor ? `${conductor.nombre} ${conductor.apellido}` : id;
  }

  placaVehiculo(id: string): string {
    return this.vehiculos.find(v => v.id === id)?.placa || id;
  }

  private mensaje(e: any): string {
    return e?.error?.mensaje || `Error ${e?.status ?? ''}`.trim();
  }
}
