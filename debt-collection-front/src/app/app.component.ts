import { Component, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, NgClass } from '@angular/common';

type State = 'Vencida' | 'Por vencer' | 'Al día';
interface Debt {
  id: number;
  debtor: string;
  document: string;
  concept: string;
  amount: number;
  days: number;
  state: State;
  agent: string;
  selected?: boolean;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, NgClass],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  search = signal('');
  statusFilter = signal<'Todas' | State>('Todas');
  selectedAgent = signal('Valentina Ruiz');
  private allDebts: Debt[] = [
    { id: 1048, debtor: 'María Fernanda López', document: 'CC 1032456789', concept: 'Crédito personal', amount: 3250000, days: 42, state: 'Vencida', agent: 'Valentina Ruiz' },
    { id: 1047, debtor: 'Constructora Andina SAS', document: 'NIT 900457812', concept: 'Factura 0248', amount: 8750000, days: 18, state: 'Vencida', agent: 'Carlos Medina' },
    { id: 1046, debtor: 'Javier Andrés Mora', document: 'CC 79654218', concept: 'Tarjeta débito', amount: 1240000, days: 6, state: 'Por vencer', agent: 'Sin asignar' },
    { id: 1045, debtor: 'Comercial del Norte LTDA', document: 'NIT 890124530', concept: 'Línea de crédito', amount: 15300000, days: 2, state: 'Por vencer', agent: 'Sin asignar' },
    { id: 1044, debtor: 'Santiago Herrera', document: 'CC 1022387914', concept: 'Préstamo vehículo', amount: 4680000, days: 0, state: 'Al día', agent: 'Ana Torres' },
    { id: 1043, debtor: 'Lucía Pérez Gómez', document: 'CC 52987134', concept: 'Crédito personal', amount: 2100000, days: 31, state: 'Vencida', agent: 'Carlos Medina' },
  ];
  debts = signal<Debt[]>(this.allDebts);
  agents = ['Valentina Ruiz', 'Carlos Medina', 'Ana Torres', 'Mateo Silva'];

  filteredDebts = computed(() => {
    const term = this.search().toLowerCase();
    const status = this.statusFilter();
    return this.debtList().filter((debt) => {
      const matchesText = `${debt.debtor} ${debt.document} ${debt.concept}`.toLowerCase().includes(term);
      const matchesStatus = status === 'Todas' || debt.state === status;
      return matchesText && matchesStatus;
    });
  });

  debtList = signal<Debt[]>(this.allDebts);
  selected = computed(() => this.debtList().filter((debt) => debt.selected));
  assignedValue = computed(() => this.selected().reduce((total, debt) => total + debt.amount, 0));
  overdueValue = computed(() => this.debtList().filter((debt) => debt.state === 'Vencida').reduce((total, debt) => total + debt.amount, 0));
  overdueCount = computed(() => this.debtList().filter((debt) => debt.state === 'Vencida').length);

  setStatus(status: 'Todas' | State): void {
    this.statusFilter.set(status);
  }

  toggleAll(event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    this.filteredDebts().forEach((debt) => debt.selected = checked);
  }

  assignSelected(): void {
    this.selected().forEach((debt) => {
      debt.agent = this.selectedAgent();
      debt.selected = false;
    });
  }

  assignOne(debt: Debt): void {
    debt.agent = this.selectedAgent();
  }
}
