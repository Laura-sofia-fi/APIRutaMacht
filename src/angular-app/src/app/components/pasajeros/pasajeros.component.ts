import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Pasajero } from '../../models/pasajero.model';
import { PasajeroService } from '../../services/pasajero.service';
import { ToastService } from '../../services/toast.service';

function vacio(): Pasajero {
  return {
    id: '', nombre: '', apellido: '', tipoDocumento: '', documento: '',
    telefono: '', correo: '', fechaRegistro: new Date().toISOString().slice(0, 10),
  };
}

@Component({
  selector: 'app-pasajeros',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pasajeros.component.html',
})
export class PasajerosComponent implements OnInit {
  private service = inject(PasajeroService);
  private toast = inject(ToastService);

  pasajeros: Pasajero[] = [];
  busqueda = '';
  modalAbierto = false;
  editandoId: string | null = null;
  form: Pasajero = vacio();

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: (res) => (this.pasajeros = res.pasajeros || []),
      error: (e) => this.toast.show('No se pudieron cargar los pasajeros: ' + this.msg(e), true),
    });
  }

  buscar(): void {
    const termino = this.busqueda.trim();
    if (!termino) {
      this.cargar();
      return;
    }
    this.service.buscar(termino).subscribe({
      next: (res) => (this.pasajeros = res.pasajero ? [res.pasajero] : []),
      error: (e) => {
        this.pasajeros = [];
        this.toast.show(this.msg(e), true);
      },
    });
  }

  limpiarBusqueda(): void {
    this.busqueda = '';
    this.cargar();
  }

  abrirNuevo(): void {
    this.editandoId = null;
    this.form = vacio();
    this.modalAbierto = true;
  }

  abrirEditar(p: Pasajero): void {
    this.editandoId = p.id;
    this.form = { ...p };
    this.modalAbierto = true;
  }

  cerrar(): void {
    this.modalAbierto = false;
  }

  guardar(): void {
    const requeridos: (keyof Pasajero)[] = [
      'id', 'nombre', 'apellido', 'tipoDocumento', 'documento',
      'telefono', 'correo', 'fechaRegistro',
    ];
    for (const campo of requeridos) {
      if (!this.form[campo]) {
        this.toast.show(`El campo "${campo}" es obligatorio.`, true);
        return;
      }
    }

    if (this.editandoId) {
      this.service.actualizar(this.editandoId, this.form).subscribe({
        next: () => { this.toast.show('Pasajero actualizado correctamente.'); this.cerrar(); this.cargar(); },
        error: (e) => this.toast.show(this.msg(e), true),
      });
    } else {
      this.service.crear(this.form).subscribe({
        next: () => { this.toast.show('Pasajero creado correctamente.'); this.cerrar(); this.cargar(); },
        error: (e) => this.toast.show(this.msg(e), true),
      });
    }
  }

  eliminar(p: Pasajero): void {
    if (!confirm('¿Eliminar este pasajero? Esta acción no se puede deshacer.')) return;
    this.service.eliminar(p.id).subscribe({
      next: () => { this.toast.show('Pasajero eliminado correctamente.'); this.cargar(); },
      error: (e) => this.toast.show(this.msg(e), true),
    });
  }

  private msg(e: any): string {
    return e?.error?.mensaje || `Error ${e?.status ?? ''}`.trim();
  }
}
