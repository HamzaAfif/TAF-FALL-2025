import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-busy-spinner',
  templateUrl: './busy-spinner.component.html',
  styleUrls: ['./busy-spinner.component.less']
})
export class BusySpinnerComponent {

  @Input() message: string = 'Veuillez patienter';

  constructor() { }

}