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
public class WallcommentController {

    @Autowired
    private MessageCommentRepository messageCommentRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private WallmessageRepository wallmessageRepository;

    @GetMapping("/wallcomments/{parentId}")
    public List<MessageComment> list(@PathVariable Long parentId) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("aikaleima").descending());
        return messageCommentRepository.findByParentMessageId(parentId, pageable);
    }

    @PostMapping("/wallcomments")
    public void create(@RequestBody MessageComment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        comment.setTekija(tekija.getNimi());
        comment.setAikaleima(LocalDateTime.now());
        System.out.println("saving" + comment);
        Wallmessage w = wallmessageRepository.findById(comment.getParentMessageId()).get();
        List<MessageComment> kommentit = w.getKommentit();
        if (kommentit.size() == 10){
            kommentit.remove(0);
        }
        kommentit.add(comment);
        w.setKommentit(kommentit);
        wallmessageRepository.save(w);
        return; 
    }
    
    @PostMapping("/wallLike/{likeId}")
    public void like(@PathVariable Long likeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        Wallmessage m = wallmessageRepository.findById(likeId).get();
        Set<Long> tykkaajat = m.getTykkaajienId();
        System.out.println(tekija.getId());
        System.out.println(tykkaajat);
        if (!tykkaajat.contains(tekija.getId())){
            System.out.println("hyvaksyy tykkays " + likeId);
            tykkaajat.add(tekija.getId());
            m.setTykkaajienId(tykkaajat);
            System.out.println(tykkaajat);
            wallmessageRepository.save(m);
        }
        return; 
    }
}

