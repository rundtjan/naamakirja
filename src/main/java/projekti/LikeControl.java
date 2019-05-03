
package projekti;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeControl extends AbstractPersistable<Long>{
    private Long tykkaajaId;
    private String tykatty;// P tai W plus sen id
}
