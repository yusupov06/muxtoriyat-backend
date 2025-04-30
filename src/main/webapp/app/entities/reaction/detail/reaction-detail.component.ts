import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IReaction } from '../reaction.model';

@Component({
  standalone: true,
  selector: 'jhi-reaction-detail',
  templateUrl: './reaction-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReactionDetailComponent {
  reaction = input<IReaction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
