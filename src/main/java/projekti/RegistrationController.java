package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String start(Model model, @ModelAttribute Person person) {
        //System.out.println("reaction registration");

        model.addAttribute("virhe", null);
        return "registration";
    }

       
    @PostMapping("/registration")
    public String register(@Valid
            @ModelAttribute Person person,
            BindingResult bindingResult, Model model) {
        System.out.println(person.getKayttajatunnus());
            if(bindingResult.hasErrors()) {
                System.out.println("has errors");
                return "registration";
    }
        if (personRepository.findByKayttajatunnus(person.getKayttajatunnus()) != null) {
            System.out.println("löytyi");
            model.addAttribute("virhe", "Käyttäjätunnus on jo käytössä");
            return "registration";   
        }
        
        if (personRepository.findByProfiili(person.getProfiili()) != null) {
            System.out.println("löytyi");
            model.addAttribute("virhe", "Profiili on jo käytössä");
            return "registration";   
        }
        
        person.setSalasana(passwordEncoder.encode(person.getSalasana()));
        personRepository.save(person);
        return "redirect:/" + person.getProfiili();
        }

}
