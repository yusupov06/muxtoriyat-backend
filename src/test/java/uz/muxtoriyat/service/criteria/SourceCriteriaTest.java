package uz.muxtoriyat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SourceCriteriaTest {

    @Test
    void newSourceCriteriaHasAllFiltersNullTest() {
        var sourceCriteria = new SourceCriteria();
        assertThat(sourceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void sourceCriteriaFluentMethodsCreatesFiltersTest() {
        var sourceCriteria = new SourceCriteria();

        setAllFilters(sourceCriteria);

        assertThat(sourceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void sourceCriteriaCopyCreatesNullFilterTest() {
        var sourceCriteria = new SourceCriteria();
        var copy = sourceCriteria.copy();

        assertThat(sourceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(sourceCriteria)
        );
    }

    @Test
    void sourceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var sourceCriteria = new SourceCriteria();
        setAllFilters(sourceCriteria);

        var copy = sourceCriteria.copy();

        assertThat(sourceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(sourceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var sourceCriteria = new SourceCriteria();

        assertThat(sourceCriteria).hasToString("SourceCriteria{}");
    }

    private static void setAllFilters(SourceCriteria sourceCriteria) {
        sourceCriteria.id();
        sourceCriteria.name();
        sourceCriteria.description();
        sourceCriteria.fileUrl();
        sourceCriteria.fileType();
        sourceCriteria.categoryId();
        sourceCriteria.distinct();
    }

    private static Condition<SourceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getFileUrl()) &&
                condition.apply(criteria.getFileType()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SourceCriteria> copyFiltersAre(SourceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getFileUrl(), copy.getFileUrl()) &&
                condition.apply(criteria.getFileType(), copy.getFileType()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
