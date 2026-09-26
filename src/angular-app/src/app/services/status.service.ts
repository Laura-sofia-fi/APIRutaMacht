import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE } from '../api-base';

@Injectable({ providedIn: 'root' })
export class StatusService {
  private http = inject(HttpClient);

  check() {
    return this.http.get(`${API_BASE}/api/status`, { responseType: 'text' });
  }
}
