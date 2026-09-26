import { Injectable, signal } from '@angular/core';

export interface ToastMsg {
  id: number;
  text: string;
  err: boolean;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts = signal<ToastMsg[]>([]);
  private counter = 0;

  show(text: string, err = false): void {
    const id = ++this.counter;
    this.toasts.update((t) => [...t, { id, text, err }]);
    setTimeout(() => this.toasts.update((t) => t.filter((x) => x.id !== id)), 3500);
  }
}
