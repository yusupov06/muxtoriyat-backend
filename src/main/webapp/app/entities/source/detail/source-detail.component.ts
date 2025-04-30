import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISource } from '../source.model';

@Component({
  standalone: true,
  selector: 'jhi-source-detail',
  templateUrl: './source-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SourceDetailComponent {
  source = input<ISource | null>(null);

  previousState(): void {
    window.history.back();
  }
}
