import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';
import { FileType } from 'app/entities/enumerations/file-type.model';
import { FileService } from '../service/file.service';
import { IFile } from '../file.model';
import { FileFormGroup, FileFormService } from './file-form.service';

@Component({
  standalone: true,
  selector: 'jhi-file-update',
  templateUrl: './file-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FileUpdateComponent implements OnInit {
  isSaving = false;
  file: IFile | null = null;
  fileTypeValues = Object.keys(FileType);

  sourcesSharedCollection: ISource[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected fileService = inject(FileService);
  protected fileFormService = inject(FileFormService);
  protected sourceService = inject(SourceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FileFormGroup = this.fileFormService.createFileFormGroup();

  compareSource = (o1: ISource | null, o2: ISource | null): boolean => this.sourceService.compareSource(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ file }) => {
      this.file = file;
      if (file) {
        this.updateForm(file);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('muxtoriyatApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const file = this.fileFormService.getFile(this.editForm);
    if (file.id !== null) {
      this.subscribeToSaveResponse(this.fileService.update(file));
    } else {
      this.subscribeToSaveResponse(this.fileService.create(file));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFile>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(file: IFile): void {
    this.file = file;
    this.fileFormService.resetForm(this.editForm, file);

    this.sourcesSharedCollection = this.sourceService.addSourceToCollectionIfMissing<ISource>(this.sourcesSharedCollection, file.source);
  }

  protected loadRelationshipsOptions(): void {
    this.sourceService
      .query()
      .pipe(map((res: HttpResponse<ISource[]>) => res.body ?? []))
      .pipe(map((sources: ISource[]) => this.sourceService.addSourceToCollectionIfMissing<ISource>(sources, this.file?.source)))
      .subscribe((sources: ISource[]) => (this.sourcesSharedCollection = sources));
  }
}
