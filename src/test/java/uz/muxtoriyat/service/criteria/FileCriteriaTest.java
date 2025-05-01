package uz.muxtoriyat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FileCriteriaTest {

    @Test
    void newFileCriteriaHasAllFiltersNullTest() {
        var fileCriteria = new FileCriteria();
        assertThat(fileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void fileCriteriaFluentMethodsCreatesFiltersTest() {
        var fileCriteria = new FileCriteria();

        setAllFilters(fileCriteria);

        assertThat(fileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void fileCriteriaCopyCreatesNullFilterTest() {
        var fileCriteria = new FileCriteria();
        var copy = fileCriteria.copy();

        assertThat(fileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(fileCriteria)
        );
    }

    @Test
    void fileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var fileCriteria = new FileCriteria();
        setAllFilters(fileCriteria);

        var copy = fileCriteria.copy();

        assertThat(fileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(fileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var fileCriteria = new FileCriteria();

        assertThat(fileCriteria).hasToString("FileCriteria{}");
    }

    private static void setAllFilters(FileCriteria fileCriteria) {
        fileCriteria.id();
        fileCriteria.name();
        fileCriteria.description();
        fileCriteria.url();
        fileCriteria.fileType();
        fileCriteria.sourceId();
        fileCriteria.distinct();
    }

    private static Condition<FileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getFileType()) &&
                condition.apply(criteria.getSourceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FileCriteria> copyFiltersAre(FileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getFileType(), copy.getFileType()) &&
                condition.apply(criteria.getSourceId(), copy.getSourceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
