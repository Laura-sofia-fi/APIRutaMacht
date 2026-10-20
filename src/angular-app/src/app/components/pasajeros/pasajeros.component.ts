import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Pasajero } from '../../models/pasajero.model';
import { PasajeroService } from '../../services/pasajero.service';
import { ToastService } from '../../services/toast.service';
import { TIPOS_DOCUMENTO } from '../../models/tipos-documento';

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
  private cdr = inject(ChangeDetectorRef);

  tiposDocumento = TIPOS_DOCUMENTO;

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
      next: (res) => {
        this.pasajeros = res.pasajeros || [];
        this.cdr.detectChanges();
      },
      error: (e) => this.toast.show('No se pudieron cargar los pasajeros: ' + this.msg(e), true),
    });
  }

  // Filtra en vivo solo por ID
  get filtrados(): Pasajero[] {
    const t = this.normalizar(this.busqueda);
    if (!t) return this.pasajeros;
    return this.pasajeros.filter((x) => this.normalizar(x.id).includes(t));
  }

  limpiarBusqueda(): void {
    this.busqueda = '';
  }

  // Ignora mayúsculas y tildes
  private normalizar(v: unknown): string {
    return String(v ?? '').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '').trim();
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
      next: () => {
        this.pasajeros = this.pasajeros.filter((x) => x.id !== p.id);
        this.cdr.detectChanges();
        this.toast.show('Pasajero eliminado correctamente.');
        this.cargar();
      },
      error: (e) => this.toast.show(this.msg(e), true),
    });
  }

  private msg(e: any): string {
    return e?.error?.mensaje || `Error ${e?.status ?? ''}`.trim();
  }
}