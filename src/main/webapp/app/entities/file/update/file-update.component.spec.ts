import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';
import { FileService } from '../service/file.service';
import { IFile } from '../file.model';
import { FileFormService } from './file-form.service';

import { FileUpdateComponent } from './file-update.component';

describe('File Management Update Component', () => {
  let comp: FileUpdateComponent;
  let fixture: ComponentFixture<FileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileFormService: FileFormService;
  let fileService: FileService;
  let sourceService: SourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FileUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileFormService = TestBed.inject(FileFormService);
    fileService = TestBed.inject(FileService);
    sourceService = TestBed.inject(SourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Source query and add missing value', () => {
      const file: IFile = { id: 456 };
      const source: ISource = { id: 30711 };
      file.source = source;

      const sourceCollection: ISource[] = [{ id: 462 }];
      jest.spyOn(sourceService, 'query').mockReturnValue(of(new HttpResponse({ body: sourceCollection })));
      const additionalSources = [source];
      const expectedCollection: ISource[] = [...additionalSources, ...sourceCollection];
      jest.spyOn(sourceService, 'addSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ file });
      comp.ngOnInit();

      expect(sourceService.query).toHaveBeenCalled();
      expect(sourceService.addSourceToCollectionIfMissing).toHaveBeenCalledWith(
        sourceCollection,
        ...additionalSources.map(expect.objectContaining),
      );
      expect(comp.sourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const file: IFile = { id: 456 };
      const source: ISource = { id: 32510 };
      file.source = source;

      activatedRoute.data = of({ file });
      comp.ngOnInit();

      expect(comp.sourcesSharedCollection).toContain(source);
      expect(comp.file).toEqual(file);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFile>>();
      const file = { id: 123 };
      jest.spyOn(fileFormService, 'getFile').mockReturnValue(file);
      jest.spyOn(fileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ file });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: file }));
      saveSubject.complete();

      // THEN
      expect(fileFormService.getFile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileService.update).toHaveBeenCalledWith(expect.objectContaining(file));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFile>>();
      const file = { id: 123 };
      jest.spyOn(fileFormService, 'getFile').mockReturnValue({ id: null });
      jest.spyOn(fileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ file: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: file }));
      saveSubject.complete();

      // THEN
      expect(fileFormService.getFile).toHaveBeenCalled();
      expect(fileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFile>>();
      const file = { id: 123 };
      jest.spyOn(fileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ file });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSource', () => {
      it('Should forward to sourceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sourceService, 'compareSource');
        comp.compareSource(entity, entity2);
        expect(sourceService.compareSource).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
