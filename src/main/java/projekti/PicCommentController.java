package projekti;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    @Autowired
    private LikeControlRepository likeControlRepository;
    
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
    public PictureComment create(@RequestBody PictureComment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        comment.setTekija(tekija.getNimi());
        comment.setAikaleima(LocalDateTime.now());
        if (comment.getSisalto().isEmpty()){
            comment.setSisalto("empty");
            return comment;
        }
        System.out.println("saving" + comment);
        Picture p = pictureRepository.findById(comment.getParentPictureId()).get();
        List<PictureComment> kommentit = p.getKommentit();
        if (kommentit.size() == 10){
            kommentit.remove(0);
        }
        kommentit.add(comment);
        p.setKommentit(kommentit);
        pictureRepository.save(p);
        return comment; 
    }
    
    @PostMapping("/picLike/{likeId}")
    public void like(@PathVariable Long likeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        Picture p = pictureRepository.findById(likeId).get();
        if (likeControlRepository.findByTykkaajaIdAndTykatty(tekija.getId(), "P"+likeId).isEmpty()){
            System.out.println("hyvaksyy tykkays " + likeId);
            p.setTykkaykset(p.getTykkaykset()+1);
            pictureRepository.save(p);
            LikeControl l = new LikeControl(tekija.getId(), "P"+likeId);
            likeControlRepository.save(l);
        } else {
            p.setTykkaykset(p.getTykkaykset()-1);
            Long deletoidaan = likeControlRepository.findByTykkaajaIdAndTykatty(tekija.getId(), "P"+likeId).get(0).getId();
            likeControlRepository.deleteById(deletoidaan);
        }
        return; 
    }
    
    @PostMapping("kuva/poistakuva/{picId}")
    public void delete(@PathVariable Long picId) {
        pictureRepository.deleteById(picId);
        return ;

    }
}

