package uz.muxtoriyat.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.muxtoriyat.domain.Reaction;

/**
 * Spring Data JPA repository for the Reaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long>, JpaSpecificationExecutor<Reaction> {}
