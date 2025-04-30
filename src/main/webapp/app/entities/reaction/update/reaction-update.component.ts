import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';
import { ReactionType } from 'app/entities/enumerations/reaction-type.model';
import { ReactionService } from '../service/reaction.service';
import { IReaction } from '../reaction.model';
import { ReactionFormGroup, ReactionFormService } from './reaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reaction-update',
  templateUrl: './reaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReactionUpdateComponent implements OnInit {
  isSaving = false;
  reaction: IReaction | null = null;
  reactionTypeValues = Object.keys(ReactionType);

  articlesSharedCollection: IArticle[] = [];

  protected reactionService = inject(ReactionService);
  protected reactionFormService = inject(ReactionFormService);
  protected articleService = inject(ArticleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReactionFormGroup = this.reactionFormService.createReactionFormGroup();

  compareArticle = (o1: IArticle | null, o2: IArticle | null): boolean => this.articleService.compareArticle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reaction }) => {
      this.reaction = reaction;
      if (reaction) {
        this.updateForm(reaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reaction = this.reactionFormService.getReaction(this.editForm);
    if (reaction.id !== null) {
      this.subscribeToSaveResponse(this.reactionService.update(reaction));
    } else {
      this.subscribeToSaveResponse(this.reactionService.create(reaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReaction>>): void {
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

  protected updateForm(reaction: IReaction): void {
    this.reaction = reaction;
    this.reactionFormService.resetForm(this.editForm, reaction);

    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing<IArticle>(
      this.articlesSharedCollection,
      reaction.article,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(map((articles: IArticle[]) => this.articleService.addArticleToCollectionIfMissing<IArticle>(articles, this.reaction?.article)))
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));
  }
}
