/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

/**
 *
 * @author ÄGAREN
 */
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>{
    @EntityGraph(attributePaths = {"tykkaajienId", "kommentit"})
    List<Picture> findByOmistajaId(Long id);
}
