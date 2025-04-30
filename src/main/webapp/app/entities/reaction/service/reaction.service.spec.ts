import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReaction } from '../reaction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../reaction.test-samples';

import { ReactionService } from './reaction.service';

const requireRestSample: IReaction = {
  ...sampleWithRequiredData,
};

describe('Reaction Service', () => {
  let service: ReactionService;
  let httpMock: HttpTestingController;
  let expectedResult: IReaction | IReaction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReactionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Reaction', () => {
      const reaction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reaction', () => {
      const reaction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reaction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reaction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reaction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Reaction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReactionToCollectionIfMissing', () => {
      it('should add a Reaction to an empty array', () => {
        const reaction: IReaction = sampleWithRequiredData;
        expectedResult = service.addReactionToCollectionIfMissing([], reaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reaction);
      });

      it('should not add a Reaction to an array that contains it', () => {
        const reaction: IReaction = sampleWithRequiredData;
        const reactionCollection: IReaction[] = [
          {
            ...reaction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReactionToCollectionIfMissing(reactionCollection, reaction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reaction to an array that doesn't contain it", () => {
        const reaction: IReaction = sampleWithRequiredData;
        const reactionCollection: IReaction[] = [sampleWithPartialData];
        expectedResult = service.addReactionToCollectionIfMissing(reactionCollection, reaction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reaction);
      });

      it('should add only unique Reaction to an array', () => {
        const reactionArray: IReaction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reactionCollection: IReaction[] = [sampleWithRequiredData];
        expectedResult = service.addReactionToCollectionIfMissing(reactionCollection, ...reactionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reaction: IReaction = sampleWithRequiredData;
        const reaction2: IReaction = sampleWithPartialData;
        expectedResult = service.addReactionToCollectionIfMissing([], reaction, reaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reaction);
        expect(expectedResult).toContain(reaction2);
      });

      it('should accept null and undefined values', () => {
        const reaction: IReaction = sampleWithRequiredData;
        expectedResult = service.addReactionToCollectionIfMissing([], null, reaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reaction);
      });

      it('should return initial array if no Reaction is added', () => {
        const reactionCollection: IReaction[] = [sampleWithRequiredData];
        expectedResult = service.addReactionToCollectionIfMissing(reactionCollection, undefined, null);
        expect(expectedResult).toEqual(reactionCollection);
      });
    });

    describe('compareReaction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReaction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReaction(entity1, entity2);
        const compareResult2 = service.compareReaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReaction(entity1, entity2);
        const compareResult2 = service.compareReaction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReaction(entity1, entity2);
        const compareResult2 = service.compareReaction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
