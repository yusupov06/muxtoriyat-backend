import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISource, NewSource } from '../source.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISource for edit and NewSourceFormGroupInput for create.
 */
type SourceFormGroupInput = ISource | PartialWithRequiredKeyOf<NewSource>;

type SourceFormDefaults = Pick<NewSource, 'id'>;

type SourceFormGroupContent = {
  id: FormControl<ISource['id'] | NewSource['id']>;
  name: FormControl<ISource['name']>;
  description: FormControl<ISource['description']>;
  image: FormControl<ISource['image']>;
  imageContentType: FormControl<ISource['imageContentType']>;
  fileUrl: FormControl<ISource['fileUrl']>;
  fileContent: FormControl<ISource['fileContent']>;
  fileContentContentType: FormControl<ISource['fileContentContentType']>;
  fileType: FormControl<ISource['fileType']>;
  category: FormControl<ISource['category']>;
};

export type SourceFormGroup = FormGroup<SourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SourceFormService {
  createSourceFormGroup(source: SourceFormGroupInput = { id: null }): SourceFormGroup {
    const sourceRawValue = {
      ...this.getFormDefaults(),
      ...source,
    };
    return new FormGroup<SourceFormGroupContent>({
      id: new FormControl(
        { value: sourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(sourceRawValue.name),
      description: new FormControl(sourceRawValue.description),
      image: new FormControl(sourceRawValue.image),
      imageContentType: new FormControl(sourceRawValue.imageContentType),
      fileUrl: new FormControl(sourceRawValue.fileUrl),
      fileContent: new FormControl(sourceRawValue.fileContent),
      fileContentContentType: new FormControl(sourceRawValue.fileContentContentType),
      fileType: new FormControl(sourceRawValue.fileType),
      category: new FormControl(sourceRawValue.category),
    });
  }

  getSource(form: SourceFormGroup): ISource | NewSource {
    return form.getRawValue() as ISource | NewSource;
  }

  resetForm(form: SourceFormGroup, source: SourceFormGroupInput): void {
    const sourceRawValue = { ...this.getFormDefaults(), ...source };
    form.reset(
      {
        ...sourceRawValue,
        id: { value: sourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SourceFormDefaults {
    return {
      id: null,
    };
  }
}
