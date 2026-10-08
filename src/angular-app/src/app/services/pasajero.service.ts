import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pasajero } from '../models/pasajero.model';
import { API_BASE } from '../api-base';

interface ListaPasajerosResp { mensaje: string; pasajeros: Pasajero[]; }
interface PasajeroResp { mensaje: string; pasajero: Pasajero; }

@Injectable({ providedIn: 'root' })
export class PasajeroService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/api/pasajero`;

  listar(): Observable<ListaPasajerosResp> {
    return this.http.get<ListaPasajerosResp>(this.base);
  }
  buscar(id: string): Observable<PasajeroResp> {
    return this.http.get<PasajeroResp>(`${this.base}/${id}`);
  }
  crear(p: Pasajero): Observable<PasajeroResp> {
    return this.http.post<PasajeroResp>(this.base, p);
  }
  actualizar(id: string, p: Pasajero): Observable<PasajeroResp> {
    return this.http.put<PasajeroResp>(`${this.base}/${id}`, p);
  }
  eliminar(id: string): Observable<{ mensaje: string }> {
    return this.http.delete<{ mensaje: string }>(`${this.base}/${id}`);
  }
}
