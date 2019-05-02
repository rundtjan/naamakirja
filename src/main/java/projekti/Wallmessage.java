/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallmessage extends AbstractPersistable<Long>{
    
    
    private Long omistajaId;
    
    
    private String kirjoittaja;
    
    private String kirjoittajanProfiili;
    
    private String sisalto;
    
    private LocalDateTime aikaleima;
    
    //private boolean onkoKommentteja;
    
    @ElementCollection(targetClass=Long.class)
    //@LazyCollection(LazyCollectionOption.FALSE)
    private Set<Long> tykkaajienId;
    
    @OneToMany(cascade=CascadeType.ALL)
    //@LazyCollection(LazyCollectionOption.FALSE)  
    private List<MessageComment> kommentit;
}
