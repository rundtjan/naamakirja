package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonController {

    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    WallmessageRepository wallmessageRepository;
    
    @Autowired
    FriendrequestRepository friendrequestRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    
    @GetMapping("/kotiin")
    public String goHome(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        return "redirect:/" + personRepository.findByKayttajatunnus(kayttajatunnus).getProfiili();
    }
    
    @GetMapping("/{profiili}")
    public String showProfile(Model model, @PathVariable String profiili, HttpSession session) {
        //System.out.println(LocalDateTime.now());
        Person person = personRepository.findByProfiili(profiili);
        if (person != null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String kayttajatunnus = auth.getName();
            //System.out.println(kayttajatunnus);
            if (kayttajatunnus.equals(person.getKayttajatunnus())){//looking at own profile
                //System.out.println("found friendrequests " + friendrequestRepository.findByVastaanottajaIdAndKasitelty(person.getId(), false).size());
                model.addAttribute("friendrequests", friendrequestRepository.findByVastaanottajaIdAndKasitelty(person.getId(), false));
                model.addAttribute("ownProfile", true);
            } else {//check if looking at kaveri
                model.addAttribute("ownProfile", false);
                List<Person> kaverit = person.getKaverit();
                boolean kaveri = false;
                for (int i = 0; i < kaverit.size(); i++){
                    Person tsekataan = kaverit.get(i);
                    if (tsekataan.getKayttajatunnus().equals(kayttajatunnus)){
                        kaveri = true;
                        System.out.println("yes, it's a friend");
                    }
                }
                if (kaveri) {
                    model.addAttribute("kaveri", true);
                    model.addAttribute("showFRbutton", false);
                }
                
                if (friendrequestRepository.findByVastaanottajaIdAndTekija(person.getId(), personRepository.findByKayttajatunnus(kayttajatunnus)) == null && friendrequestRepository.findByVastaanottajaIdAndTekija(personRepository.findByKayttajatunnus(kayttajatunnus).getId(), person) == null && !kaveri) {
                    model.addAttribute("showFRbutton", true);
                } else {
                    model.addAttribute("showFRbutton", false);
                }
                
            }
            Pageable pageable = PageRequest.of(0, 25, Sort.by("aikaleima").descending());
            model.addAttribute("wall", wallmessageRepository.findByOmistajaId(person.getId(), pageable));
            model.addAttribute("person", person);
            session.setAttribute("profiili", profiili);
            return "profiili";
        }
        
        
        
        model.addAttribute("virhe", "This person does not exist.");
        return "profiili";
    }
    
    /*@PostMapping("/person/wallmessage")
    public String add(HttpSession session, Model model, @RequestParam String viesti) {
        String profiili = session.getAttribute("profiili").toString();
        Person person = personRepository.findByProfiili(profiili);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        Wallmessage message = new Wallmessage();
        message.setSisalto(viesti);
        message.setOmistajaId(person.getId());
        message.setKirjoittaja(person.getNimi());
        message.setAikaleima(LocalDateTime.now());
        wallmessageRepository.save(message);
        System.out.println(profiili + ", " + viesti + ", " + person.getNimi());
        return "redirect:/" + session.getAttribute("profiili");
    }*/
    
    @PostMapping("/person/{profiili}")
    public String add(Model model, @PathVariable String profiili, @RequestParam String viesti) {
        System.out.println("profiili " + profiili);
        Person person = personRepository.findByProfiili(profiili);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        Wallmessage message = new Wallmessage();
        message.setSisalto(viesti);
        message.setOmistajaId(person.getId());
        message.setKirjoittaja(tekija.getNimi());
        message.setKirjoittajanProfiili(tekija.getProfiili());
        LocalDateTime nyt = LocalDateTime.now();
        System.out.println(nyt);
        message.setAikaleima(LocalDateTime.now());
        wallmessageRepository.save(message);       
        return "redirect:/" + profiili;
    }
    
    @PostMapping("/person/friendrequest/{profiili}")
    public String addFriendrequest(Model model, @PathVariable String profiili){
        System.out.println("friendrequest reaction");
        Person vastaanottaja = personRepository.findByProfiili(profiili);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        Person tekija = personRepository.findByKayttajatunnus(kayttajatunnus);
        if (friendrequestRepository.findByVastaanottajaIdAndTekija(vastaanottaja.getId(), tekija) == null){
            Friendrequest f = new Friendrequest();
            f.setTekija(tekija);
            f.setVastaanottajaId(vastaanottaja.getId());
            f.setAikaleima(LocalDateTime.now());
            friendrequestRepository.save(f);
            System.out.println("friendrequest done");
        }
        return "redirect:/" + profiili;
    }
    
    @PostMapping("/person/accept/{profiili}/{friendprofiili}")
    public String acceptFriendrequest(Model model, @PathVariable String profiili, @PathVariable String friendprofiili){
        System.out.println("acceptingfriendrequest from " + friendprofiili + " to " + profiili);
        Person vastaanottaja = personRepository.findByProfiili(profiili);
        Person tekija = personRepository.findByProfiili(friendprofiili);
        Friendrequest f = friendrequestRepository.findByVastaanottajaIdAndTekija(vastaanottaja.getId(), tekija);
        f.setKasitelty(true);
        friendrequestRepository.save(f);
        List<Person> vastaanottajakaverit = vastaanottaja.getKaverit();
        vastaanottajakaverit.add(tekija);
        vastaanottaja.setKaverit(vastaanottajakaverit);
        personRepository.save(vastaanottaja);
        List<Person> tekijakaverit = tekija.getKaverit();
        tekijakaverit.add(vastaanottaja);
        tekija.setKaverit(tekijakaverit);
        personRepository.save(tekija);
        return "redirect:/" + profiili;
    }
}
