import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Vehiculo } from '../../models/vehiculo.model';
import { VehiculoService } from '../../services/vehiculo.service';
import { ToastService } from '../../services/toast.service';

function vacio(): Vehiculo {
  return { id: '', placa: '', marca: '', modelo: '', anio: new Date().getFullYear(), color: '', tipoCombustible: '', numeroChasis: '', capacidadTotal: 5 };
}

@Component({
  selector: 'app-vehiculos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vehiculos.component.html',
})
export class VehiculosComponent implements OnInit {
  private service = inject(VehiculoService);
  private toast = inject(ToastService);
  private cdr = inject(ChangeDetectorRef);

  vehiculos: Vehiculo[] = [];
  busqueda = '';
  modalAbierto = false;
  editandoId: string | null = null;
  form: Vehiculo = vacio();

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: (res) => {
        this.vehiculos = res.vehiculos || [];
        this.cdr.detectChanges();
      },
      error: (e) => this.toast.show('No se pudieron cargar los vehículos: ' + this.msg(e), true),
    });
  }

  // Filtra en vivo solo por placa: muestra lo que coincida con lo que se va escribiendo
  get filtrados(): Vehiculo[] {
    const t = this.normalizar(this.busqueda);
    if (!t) return this.vehiculos;
    return this.vehiculos.filter((x) => this.normalizar(x.placa).includes(t));
  }

  limpiarBusqueda(): void {
    this.busqueda = '';
  }

  // Ignora mayúsculas y tildes: "garcia" encuentra "García"
  private normalizar(v: unknown): string {
    return String(v ?? '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').trim();
  }

  abrirNuevo(): void {
    this.editandoId = null;
    this.form = vacio();
    this.modalAbierto = true;
  }

  abrirEditar(v: Vehiculo): void {
    this.editandoId = v.id;
    this.form = { ...v, capacidadTotal: v.capacidadTotal ?? null };
    this.modalAbierto = true;
  }

  cerrar(): void {
    this.modalAbierto = false;
  }

  guardar(): void {
    const requeridos: (keyof Vehiculo)[] = ['placa', 'marca', 'modelo', 'color', 'tipoCombustible', 'numeroChasis'];
    for (const campo of requeridos) {
      if (!this.form[campo]) {
        this.toast.show(`El campo "${campo}" es obligatorio.`, true);
        return;
      }
    }
    if (!this.form.anio || this.form.anio <= 0) {
      this.toast.show('El campo "año" debe ser mayor a 0.', true);
      return;
    }
    if (!this.form.capacidadTotal || this.form.capacidadTotal < 2) {
      this.toast.show('El vehículo debe tener al menos 2 asientos en total, incluido el conductor.', true);
      return;
    }

    if (this.editandoId) {
      this.service.actualizar(this.editandoId, this.form).subscribe({
        next: () => { this.toast.show('Vehículo actualizado correctamente.'); this.cerrar(); this.cargar(); },
        error: (e) => this.toast.show(this.msg(e), true),
      });
    } else {
      this.service.crear(this.form).subscribe({
        next: () => { this.toast.show('Vehículo creado correctamente.'); this.cerrar(); this.cargar(); },
        error: (e) => this.toast.show(this.msg(e), true),
      });
    }
  }

  eliminar(v: Vehiculo): void {
    if (!confirm('¿Eliminar este vehículo? Esta acción no se puede deshacer.')) return;
    this.service.eliminar(v.id).subscribe({
      next: () => { this.toast.show('Vehículo eliminado correctamente.'); this.cargar(); },
      error: (e) => this.toast.show(this.msg(e), true),
    });
  }

  private msg(e: any): string {
    return e?.error?.mensaje || `Error ${e?.status ?? ''}`.trim();
  }
}
