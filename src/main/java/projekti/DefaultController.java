package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
    
    @Autowired
    PersonRepository personRepository;

    @GetMapping("/")
    public String handleDefault() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals("anonymousUser")){
            String kayttajatunnus = auth.getName();
            return "redirect:/" + personRepository.findByKayttajatunnus(kayttajatunnus).getProfiili();        
        }
        return "start";
    }
    
    @GetMapping("/login")
    public String handleLogin() {
        return "login";
    }

}
