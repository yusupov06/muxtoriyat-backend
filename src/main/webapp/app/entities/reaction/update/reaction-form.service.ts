import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReaction, NewReaction } from '../reaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReaction for edit and NewReactionFormGroupInput for create.
 */
type ReactionFormGroupInput = IReaction | PartialWithRequiredKeyOf<NewReaction>;

type ReactionFormDefaults = Pick<NewReaction, 'id'>;

type ReactionFormGroupContent = {
  id: FormControl<IReaction['id'] | NewReaction['id']>;
  deviceId: FormControl<IReaction['deviceId']>;
  reactionType: FormControl<IReaction['reactionType']>;
  article: FormControl<IReaction['article']>;
};

export type ReactionFormGroup = FormGroup<ReactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReactionFormService {
  createReactionFormGroup(reaction: ReactionFormGroupInput = { id: null }): ReactionFormGroup {
    const reactionRawValue = {
      ...this.getFormDefaults(),
      ...reaction,
    };
    return new FormGroup<ReactionFormGroupContent>({
      id: new FormControl(
        { value: reactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      deviceId: new FormControl(reactionRawValue.deviceId),
      reactionType: new FormControl(reactionRawValue.reactionType),
      article: new FormControl(reactionRawValue.article),
    });
  }

  getReaction(form: ReactionFormGroup): IReaction | NewReaction {
    return form.getRawValue() as IReaction | NewReaction;
  }

  resetForm(form: ReactionFormGroup, reaction: ReactionFormGroupInput): void {
    const reactionRawValue = { ...this.getFormDefaults(), ...reaction };
    form.reset(
      {
        ...reactionRawValue,
        id: { value: reactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReactionFormDefaults {
    return {
      id: null,
    };
  }
}
