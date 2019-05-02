package projekti;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PicCommentController {

    @Autowired
    private PictureCommentRepository pictureCommentRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private PictureRepository pictureRepository;
    
    @PostMapping("/kuva/profiilikuvaksi/{kuvaId}")
    public void profilePicture(@PathVariable Long kuvaId) {
        System.out.println("any reaction");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person omistaja = personRepository.findByKayttajatunnus(kayttajatunnus);
        omistaja.setProfiilikuvaId(kuvaId);
        personRepository.save(omistaja);
        return;
    }
    
    @PostMapping("/piccomments")
    public void create(@RequestBody PictureComment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        comment.setTekija(tekija.getNimi());
        comment.setAikaleima(LocalDateTime.now());
        System.out.println("saving" + comment);
        Picture p = pictureRepository.findById(comment.getParentPictureId()).get();
        List<PictureComment> kommentit = p.getKommentit();
        if (kommentit.size() == 10){
            kommentit.remove(0);
        }
        kommentit.add(comment);
        p.setKommentit(kommentit);
        pictureRepository.save(p);
        return; 
    }
    
    @PostMapping("/picLike/{likeId}")
    public void like(@PathVariable Long likeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        Picture p = pictureRepository.findById(likeId).get();
        Set<Long> tykkaajat = p.getTykkaajienId();
        if (!tykkaajat.contains(tekija.getId())){
            System.out.println("hyvaksyy tykkays " + likeId);
            tykkaajat.add(tekija.getId());
            p.setTykkaajienId(tykkaajat);
            pictureRepository.save(p);
        }
        return; 
    }
    
    @PostMapping("kuva/poistakuva/{picId}")
    public void delete(@PathVariable Long picId) {
        pictureRepository.deleteById(picId);
        return ;

    }
}

