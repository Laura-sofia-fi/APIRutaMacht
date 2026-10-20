import { Component, OnInit, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Conductor } from '../../models/conductor.model';
import { ConductorService } from '../../services/conductor.service';
import { ToastService } from '../../services/toast.service';
import { TIPOS_DOCUMENTO } from '../../models/tipos-documento';

function vacio(): Conductor {
  return {
    id: '', nombre: '', apellido: '', tipoDocumento: '', documento: '',
    telefono: '', correo: '', licencia: '', categoriaLicencia: '',
    fechaVenciLicencia: '', estado: true,
  };
}

@Component({
  selector: 'app-conductores',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './conductores.component.html',
})
export class ConductoresComponent implements OnInit {
  private service = inject(ConductorService);
  private toast = inject(ToastService);
  private cdr = inject(ChangeDetectorRef);

  tiposDocumento = TIPOS_DOCUMENTO;

  conductores: Conductor[] = [];
  busqueda = '';
  modalAbierto = false;
  editandoId: string | null = null;
  form: Conductor = vacio();

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.service.listar().subscribe({
      next: (res) => {
        this.conductores = res.conductores || [];
        this.cdr.detectChanges();
      },
      error: (e) => this.toast.show('No se pudieron cargar los conductores: ' + this.msg(e), true),
    });
  }

  // Filtra en vivo solo por ID: muestra lo que coincida con lo que se va escribiendo
  get filtrados(): Conductor[] {
    const t = this.normalizar(this.busqueda);
    if (!t) return this.conductores;
    return this.conductores.filter((x) => this.normalizar(x.id).includes(t));
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

  abrirEditar(c: Conductor): void {
    this.editandoId = c.id ?? null;
    this.form = { ...c };
    this.modalAbierto = true;
  }

  cerrar(): void {
    this.modalAbierto = false;
  }

  guardar(): void {
    const requeridos: (keyof Conductor)[] = [
      'id', 'nombre', 'apellido', 'tipoDocumento', 'documento',
      'telefono', 'correo', 'licencia', 'categoriaLicencia', 'fechaVenciLicencia',
    ];
    for (const campo of requeridos) {
      if (!this.form[campo]) {
        this.toast.show(`El campo "${campo}" es obligatorio.`, true);
        return;
      }
    }

    if (this.editandoId) {
      this.service.actualizar(this.editandoId, this.form).subscribe({
        next: () => { this.toast.show('Conductor actualizado correctamente.'); this.cerrar(); this.cargar(); },
        error: (e) => this.toast.show(this.msg(e), true),
      });
    } else {
      this.service.crear(this.form).subscribe({
        next: () => { this.toast.show('Conductor creado correctamente.'); this.cerrar(); this.cargar(); },
        error: (e) => this.toast.show(this.msg(e), true),
      });
    }
  }

  eliminar(c: Conductor): void {
    if (!c.id || !confirm('¿Eliminar este conductor? Esta acción no se puede deshacer.')) return;
    this.service.eliminar(c.id).subscribe({
      next: () => { this.toast.show('Conductor eliminado correctamente.'); this.cargar(); },
      error: (e) => this.toast.show(this.msg(e), true),
    });
  }

  private msg(e: any): string {
    return e?.error?.mensaje || `Error ${e?.status ?? ''}`.trim();
  }
}