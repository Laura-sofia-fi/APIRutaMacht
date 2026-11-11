export interface Viaje {
  id: string;
  conductorId: string;
  vehiculoId: string;
  origen: string;
  destino: string;
  paradas: string[];
  fechaHoraSalida: string;
  cupos: number;
  cuposDisponibles: number;
  aportePorCupo: number;
  estado: 'PROGRAMADO' | 'CANCELADO' | 'FINALIZADO';
  creadoEn: string;
}

export interface CrearViajeRequest {
  conductorId: string;
  vehiculoId: string;
  origen: string;
  destino: string;
  paradas: string[];
  fechaHoraSalida: string;
  cupos: number;
  aportePorCupo: number;
}
