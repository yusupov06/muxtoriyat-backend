import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReaction } from '../reaction.model';
import { ReactionService } from '../service/reaction.service';

@Component({
  standalone: true,
  templateUrl: './reaction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReactionDeleteDialogComponent {
  reaction?: IReaction;

  protected reactionService = inject(ReactionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reactionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
