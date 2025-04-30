import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFile, NewFile } from '../file.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFile for edit and NewFileFormGroupInput for create.
 */
type FileFormGroupInput = IFile | PartialWithRequiredKeyOf<NewFile>;

type FileFormDefaults = Pick<NewFile, 'id'>;

type FileFormGroupContent = {
  id: FormControl<IFile['id'] | NewFile['id']>;
  name: FormControl<IFile['name']>;
  description: FormControl<IFile['description']>;
  url: FormControl<IFile['url']>;
  content: FormControl<IFile['content']>;
  contentContentType: FormControl<IFile['contentContentType']>;
  fileType: FormControl<IFile['fileType']>;
  source: FormControl<IFile['source']>;
};

export type FileFormGroup = FormGroup<FileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FileFormService {
  createFileFormGroup(file: FileFormGroupInput = { id: null }): FileFormGroup {
    const fileRawValue = {
      ...this.getFormDefaults(),
      ...file,
    };
    return new FormGroup<FileFormGroupContent>({
      id: new FormControl(
        { value: fileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(fileRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(fileRawValue.description),
      url: new FormControl(fileRawValue.url),
      content: new FormControl(fileRawValue.content),
      contentContentType: new FormControl(fileRawValue.contentContentType),
      fileType: new FormControl(fileRawValue.fileType),
      source: new FormControl(fileRawValue.source),
    });
  }

  getFile(form: FileFormGroup): IFile | NewFile {
    return form.getRawValue() as IFile | NewFile;
  }

  resetForm(form: FileFormGroup, file: FileFormGroupInput): void {
    const fileRawValue = { ...this.getFormDefaults(), ...file };
    form.reset(
      {
        ...fileRawValue,
        id: { value: fileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FileFormDefaults {
    return {
      id: null,
    };
  }
}
