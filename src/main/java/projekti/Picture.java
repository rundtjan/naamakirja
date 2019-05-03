/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

// muut annotaatiot
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture extends AbstractPersistable<Long> {

    private String mediaType;
    private String kuvaus;
    private Long koko;
    private Long omistajaId;
    private int tykkaykset;

    @OneToMany(cascade=CascadeType.ALL)
    private List<PictureComment> kommentit;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

}
