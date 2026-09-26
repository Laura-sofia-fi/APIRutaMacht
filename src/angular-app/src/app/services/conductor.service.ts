import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conductor } from '../models/conductor.model';
import { API_BASE } from '../api-base';

interface ListaConductoresResp { mensaje: string; conductores: Conductor[]; }
interface ConductorResp { mensaje: string; conductor: Conductor; }

@Injectable({ providedIn: 'root' })
export class ConductorService {
  private http = inject(HttpClient);
  private base = `${API_BASE}/api/conductor`;

  listar(): Observable<ListaConductoresResp> {
    return this.http.get<ListaConductoresResp>(this.base);
  }
  crear(c: Conductor): Observable<ConductorResp> {
    return this.http.post<ConductorResp>(this.base, c);
  }
  actualizar(id: string, c: Conductor): Observable<ConductorResp> {
    return this.http.put<ConductorResp>(`${this.base}/${id}`, c);
  }
  eliminar(id: string): Observable<{ mensaje: string }> {
    return this.http.delete<{ mensaje: string }>(`${this.base}/${id}`);
  }
}
