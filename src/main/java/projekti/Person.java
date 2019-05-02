/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person extends AbstractPersistable<Long>{
    
    @NotEmpty
    @Size(min = 5, max = 30)
    private String kayttajatunnus;
    
    @NotEmpty
    private String salasana;
    
    @NotEmpty
    @Size(min = 4, max = 150)
    private String nimi;
    
    //@NotEmpty
    //@Email
    private String email;
    
    @NotEmpty
    @Size(min = 4, max = 50)
    private String profiili;
    
    @ManyToMany
    private List<Person> kaverit;
    
    private Long profiilikuvaId;
    
}
