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
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendrequestRepository extends JpaRepository<Friendrequest, Long> {
    List<Friendrequest> findByVastaanottajaIdAndKasitelty(Long id, Boolean kasitelty);
    Friendrequest findByVastaanottajaIdAndTekija(Long id, Person tekija);
}
