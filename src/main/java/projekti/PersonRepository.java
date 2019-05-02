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

public interface PersonRepository extends JpaRepository<Person, Long>{
    @EntityGraph(attributePaths = {"kaverit"})
    Person findByKayttajatunnus(String kayttajatunnus);
    @EntityGraph(attributePaths = {"kaverit"})
    Person findByProfiili(String profiili);
    @EntityGraph(attributePaths = {"kaverit"})
    List<Person> findByNimiContainingIgnoreCase(String hakusana);
}
