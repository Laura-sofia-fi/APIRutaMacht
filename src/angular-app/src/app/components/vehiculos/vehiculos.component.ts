import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Vehiculo } from '../../models/vehiculo.model';
import { VehiculoService } from '../../services/vehiculo.service';
import { ToastService } from '../../services/toast.service';

function vacio(): Vehiculo {
  return { placa: '', marca: '', modelo: '', anio: new Date().getFullYear(), color: '', tipoCombustible: '', numeroChasis: '' };
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

  vehiculos: Vehiculo[] = [];
  modalAbierto = false;
  editandoPlaca: string | null = null;
  form: Vehiculo = vacio();

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: (res) => (this.vehiculos = res.vehiculos || []),
      error: (e) => this.toast.show('No se pudieron cargar los vehículos: ' + this.msg(e), true),
    });
  }

  abrirNuevo(): void {
    this.editandoPlaca = null;
    this.form = vacio();
    this.modalAbierto = true;
  }

  abrirEditar(v: Vehiculo): void {
    this.editandoPlaca = v.placa;
    this.form = { ...v };
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

    if (this.editandoPlaca) {
      this.service.actualizar(this.editandoPlaca, this.form).subscribe({
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
    this.service.eliminar(v.placa).subscribe({
      next: () => { this.toast.show('Vehículo eliminado correctamente.'); this.cargar(); },
      error: (e) => this.toast.show(this.msg(e), true),
    });
  }

  private msg(e: any): string {
    return e?.error?.mensaje || `Error ${e?.status ?? ''}`.trim();
  }
}
