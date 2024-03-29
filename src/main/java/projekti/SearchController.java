/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

/**
 *
 * @author ÄGAREN
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    FriendrequestRepository friendrequestRepository;

    
    @GetMapping("/hae")
    public String showSearch(Model model){
        model.addAttribute("henkilot", null);
        return "search";
    }
    
    @PostMapping("/hae")
    public String doSearch(@RequestParam String hakusana) {
        if (hakusana.isEmpty()){
            return "redirect:/hae/" + "naytakaikki";
        }
        return "redirect:/hae/" + hakusana;
    }
    
    @GetMapping("/hae/{hakusana}")
    public String showSearch(Model model, @PathVariable String hakusana){
        if (hakusana.equals("naytakaikki")){
            model.addAttribute("henkilot", personRepository.findAll());
            return "search";
        }
        model.addAttribute("henkilot", personRepository.findByNimiContainingIgnoreCase(hakusana));
        return "search";
    }
    

}

