/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    
    private int tykkaykset;
    
    
    @OneToMany(cascade=CascadeType.ALL)  
    private List<MessageComment> kommentit;
}
