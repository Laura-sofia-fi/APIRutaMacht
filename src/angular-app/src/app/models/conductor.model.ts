export interface Conductor {
  id?: string;
  nombre: string;
  apellido: string;
  tipoDocumento: string;
  documento: string;
  telefono: string;
  correo: string;
  licencia: string;
  categoriaLicencia: string;
  fechaVenciLicencia: string;
  estado: boolean;
}
