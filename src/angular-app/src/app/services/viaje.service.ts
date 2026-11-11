import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE } from '../api-base';
import { CrearViajeRequest, Viaje } from '../models/viaje.model';

interface ListaViajesResp { mensaje: string; viajes: Viaje[]; }
interface ViajeResp { mensaje: string; viaje: Viaje; }

@Injectable({ providedIn: 'root' })
export class ViajeService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/api/viajes`;

  buscar(origen = '', destino = ''): Observable<ListaViajesResp> {
    let params = new HttpParams();
    if (origen.trim()) params = params.set('origen', origen.trim());
    if (destino.trim()) params = params.set('destino', destino.trim());
    return this.http.get<ListaViajesResp>(this.base, { params });
  }

  publicar(request: CrearViajeRequest): Observable<ViajeResp> {
    return this.http.post<ViajeResp>(this.base, request);
  }

  cancelar(id: string): Observable<ViajeResp> {
    return this.http.patch<ViajeResp>(`${this.base}/${encodeURIComponent(id)}/cancelar`, {});
  }
}
