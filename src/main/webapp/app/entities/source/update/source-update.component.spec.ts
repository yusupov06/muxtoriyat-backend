import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { SourceService } from '../service/source.service';
import { ISource } from '../source.model';
import { SourceFormService } from './source-form.service';

import { SourceUpdateComponent } from './source-update.component';

describe('Source Management Update Component', () => {
  let comp: SourceUpdateComponent;
  let fixture: ComponentFixture<SourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sourceFormService: SourceFormService;
  let sourceService: SourceService;
  let categoryService: CategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SourceUpdateComponent],
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
      .overrideTemplate(SourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sourceFormService = TestBed.inject(SourceFormService);
    sourceService = TestBed.inject(SourceService);
    categoryService = TestBed.inject(CategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const source: ISource = { id: 456 };
      const category: ICategory = { id: 18860 };
      source.category = category;

      const categoryCollection: ICategory[] = [{ id: 29770 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ source });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const source: ISource = { id: 456 };
      const category: ICategory = { id: 26956 };
      source.category = category;

      activatedRoute.data = of({ source });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.source).toEqual(source);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISource>>();
      const source = { id: 123 };
      jest.spyOn(sourceFormService, 'getSource').mockReturnValue(source);
      jest.spyOn(sourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ source });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: source }));
      saveSubject.complete();

      // THEN
      expect(sourceFormService.getSource).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sourceService.update).toHaveBeenCalledWith(expect.objectContaining(source));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISource>>();
      const source = { id: 123 };
      jest.spyOn(sourceFormService, 'getSource').mockReturnValue({ id: null });
      jest.spyOn(sourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ source: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: source }));
      saveSubject.complete();

      // THEN
      expect(sourceFormService.getSource).toHaveBeenCalled();
      expect(sourceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISource>>();
      const source = { id: 123 };
      jest.spyOn(sourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ source });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sourceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
