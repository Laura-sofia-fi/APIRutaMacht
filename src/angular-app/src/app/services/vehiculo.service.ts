import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehiculo } from '../models/vehiculo.model';
import { API_BASE } from '../api-base';

interface ListaVehiculosResp { mensaje: string; vehiculos: Vehiculo[]; }
interface VehiculoResp { mensaje: string; vehiculo: Vehiculo; }

@Injectable({ providedIn: 'root' })
export class VehiculoService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/api/vehiculo`;

  listar(): Observable<ListaVehiculosResp> {
    return this.http.get<ListaVehiculosResp>(this.base);
  }
  buscar(placa: string): Observable<VehiculoResp> {
    return this.http.get<VehiculoResp>(`${this.base}/placa/${encodeURIComponent(placa)}`);
  }
  crear(v: Vehiculo): Observable<VehiculoResp> {
    return this.http.post<VehiculoResp>(this.base, v);
  }
  actualizar(id: string, v: Vehiculo): Observable<VehiculoResp> {
    return this.http.put<VehiculoResp>(`${this.base}/${encodeURIComponent(id)}`, v);
  }
  eliminar(id: string): Observable<{ mensaje: string }> {
    return this.http.delete<{ mensaje: string }>(`${this.base}/${encodeURIComponent(id)}`);
  }
}
