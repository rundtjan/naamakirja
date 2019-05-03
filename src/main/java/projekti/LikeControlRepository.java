package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikeControlRepository extends JpaRepository<LikeControl, Long>{
    List<LikeControl> findByTykkaajaIdAndTykatty(Long tykkaajaId, String tykatty);
}
