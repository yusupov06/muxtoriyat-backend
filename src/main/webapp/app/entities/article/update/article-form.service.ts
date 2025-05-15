import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IArticle, NewArticle } from '../article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticle for edit and NewArticleFormGroupInput for create.
 */
type ArticleFormGroupInput = IArticle | PartialWithRequiredKeyOf<NewArticle>;

type ArticleFormDefaults = Pick<NewArticle, 'id'>;

type ArticleFormGroupContent = {
  id: FormControl<IArticle['id'] | NewArticle['id']>;
  name: FormControl<IArticle['name']>;
  title: FormControl<IArticle['title']>;
  description: FormControl<IArticle['description']>;
  content: FormControl<IArticle['content']>;
  image: FormControl<IArticle['image']>;
  imageContentType: FormControl<IArticle['imageContentType']>;
  visibility: FormControl<IArticle['visibility']>;
  category: FormControl<IArticle['category']>;
  author: FormControl<IArticle['author']>;
};

export type ArticleFormGroup = FormGroup<ArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleFormService {
  createArticleFormGroup(article: ArticleFormGroupInput = { id: null }): ArticleFormGroup {
    const articleRawValue = {
      ...this.getFormDefaults(),
      ...article,
    };
    return new FormGroup<ArticleFormGroupContent>({
      id: new FormControl(
        { value: articleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(articleRawValue.name, {
        validators: [Validators.required],
      }),
      title: new FormControl(articleRawValue.title),
      description: new FormControl(articleRawValue.description),
      content: new FormControl(articleRawValue.content),
      image: new FormControl(articleRawValue.image),
      imageContentType: new FormControl(articleRawValue.imageContentType),
      visibility: new FormControl(articleRawValue.visibility),
      category: new FormControl(articleRawValue.category),
      author: new FormControl(articleRawValue.author),
    });
  }

  getArticle(form: ArticleFormGroup): IArticle | NewArticle {
    return form.getRawValue() as IArticle | NewArticle;
  }

  resetForm(form: ArticleFormGroup, article: ArticleFormGroupInput): void {
    const articleRawValue = { ...this.getFormDefaults(), ...article };
    form.reset(
      {
        ...articleRawValue,
        id: { value: articleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArticleFormDefaults {
    return {
      id: null,
    };
  }
}
