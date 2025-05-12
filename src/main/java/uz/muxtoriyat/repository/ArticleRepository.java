package uz.muxtoriyat.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.muxtoriyat.domain.Article;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    @Query("select article from Article article where article.author.login = ?#{authentication.name}")
    List<Article> findByAuthorIsCurrentUser();

    Optional<Article> findByIdAndAuthor_Id(Long id, Long authorId);
}
