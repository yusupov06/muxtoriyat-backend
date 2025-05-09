import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../reaction.test-samples';

import { ReactionFormService } from './reaction-form.service';

describe('Reaction Form Service', () => {
  let service: ReactionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReactionFormService);
  });

  describe('Service methods', () => {
    describe('createReactionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReactionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            deviceId: expect.any(Object),
            targetId: expect.any(Object),
            reactionType: expect.any(Object),
          }),
        );
      });

      it('passing IReaction should create a new form with FormGroup', () => {
        const formGroup = service.createReactionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            deviceId: expect.any(Object),
            targetId: expect.any(Object),
            reactionType: expect.any(Object),
          }),
        );
      });
    });

    describe('getReaction', () => {
      it('should return NewReaction for default Reaction initial value', () => {
        const formGroup = service.createReactionFormGroup(sampleWithNewData);

        const reaction = service.getReaction(formGroup) as any;

        expect(reaction).toMatchObject(sampleWithNewData);
      });

      it('should return NewReaction for empty Reaction initial value', () => {
        const formGroup = service.createReactionFormGroup();

        const reaction = service.getReaction(formGroup) as any;

        expect(reaction).toMatchObject({});
      });

      it('should return IReaction', () => {
        const formGroup = service.createReactionFormGroup(sampleWithRequiredData);

        const reaction = service.getReaction(formGroup) as any;

        expect(reaction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReaction should not enable id FormControl', () => {
        const formGroup = service.createReactionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReaction should disable id FormControl', () => {
        const formGroup = service.createReactionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
