import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ReactionService } from '../service/reaction.service';
import { IReaction } from '../reaction.model';
import { ReactionFormService } from './reaction-form.service';

import { ReactionUpdateComponent } from './reaction-update.component';

describe('Reaction Management Update Component', () => {
  let comp: ReactionUpdateComponent;
  let fixture: ComponentFixture<ReactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reactionFormService: ReactionFormService;
  let reactionService: ReactionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactionUpdateComponent],
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
      .overrideTemplate(ReactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reactionFormService = TestBed.inject(ReactionFormService);
    reactionService = TestBed.inject(ReactionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reaction: IReaction = { id: 456 };

      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      expect(comp.reaction).toEqual(reaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReaction>>();
      const reaction = { id: 123 };
      jest.spyOn(reactionFormService, 'getReaction').mockReturnValue(reaction);
      jest.spyOn(reactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reaction }));
      saveSubject.complete();

      // THEN
      expect(reactionFormService.getReaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reactionService.update).toHaveBeenCalledWith(expect.objectContaining(reaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReaction>>();
      const reaction = { id: 123 };
      jest.spyOn(reactionFormService, 'getReaction').mockReturnValue({ id: null });
      jest.spyOn(reactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reaction }));
      saveSubject.complete();

      // THEN
      expect(reactionFormService.getReaction).toHaveBeenCalled();
      expect(reactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReaction>>();
      const reaction = { id: 123 };
      jest.spyOn(reactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reactionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
