package uz.muxtoriyat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArticleCriteriaTest {

    @Test
    void newArticleCriteriaHasAllFiltersNullTest() {
        var articleCriteria = new ArticleCriteria();
        assertThat(articleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void articleCriteriaFluentMethodsCreatesFiltersTest() {
        var articleCriteria = new ArticleCriteria();

        setAllFilters(articleCriteria);

        assertThat(articleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void articleCriteriaCopyCreatesNullFilterTest() {
        var articleCriteria = new ArticleCriteria();
        var copy = articleCriteria.copy();

        assertThat(articleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(articleCriteria)
        );
    }

    @Test
    void articleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var articleCriteria = new ArticleCriteria();
        setAllFilters(articleCriteria);

        var copy = articleCriteria.copy();

        assertThat(articleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(articleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var articleCriteria = new ArticleCriteria();

        assertThat(articleCriteria).hasToString("ArticleCriteria{}");
    }

    private static void setAllFilters(ArticleCriteria articleCriteria) {
        articleCriteria.id();
        articleCriteria.name();
        articleCriteria.title();
        articleCriteria.description();
        articleCriteria.visibility();
        articleCriteria.categoryId();
        articleCriteria.authorId();
        articleCriteria.distinct();
    }

    private static Condition<ArticleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getVisibility()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getAuthorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArticleCriteria> copyFiltersAre(ArticleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getVisibility(), copy.getVisibility()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getAuthorId(), copy.getAuthorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
