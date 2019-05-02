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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PictureController {
    
    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    PictureRepository pictureRepository;
    
    @GetMapping("/kuvat/{profiili}")
    public String showPics(Model model, @PathVariable String profiili) {
        Person omistaja = personRepository.findByProfiili(profiili);
        List<Picture> pics = pictureRepository.findByOmistajaId(omistaja.getId());
        model.addAttribute("person", omistaja); 
        model.addAttribute("pics", pics);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        if (omistaja.getKayttajatunnus().equals(kayttajatunnus)){
            model.addAttribute("ownPage", true);
        } else {//ei oma sivu
            model.addAttribute("ownPage", false);
                List<Person> kaverit = omistaja.getKaverit();
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
                }
        }
        return "kuvat";
    }
    
    @GetMapping("/omatKuvat")
    public String omiinKuviin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String kayttajatunnus = auth.getName();
        return "redirect:/kuvat/" + personRepository.findByKayttajatunnus(kayttajatunnus).getProfiili();
    }
    
    @PostMapping("/kuvat/{profiili}")
    public String save(Model model, @PathVariable String profiili, @RequestParam("file") MultipartFile file, @RequestParam("kuvaus") String kuvaus) throws IOException {
    Person omistaja = personRepository.findByProfiili(profiili);
    if (pictureRepository.findByOmistajaId(omistaja.getId()).size() > 9){
        model.addAttribute("virhe", true);
        model.addAttribute("virheviesti", "Sinun pitää poistaa kuvia jotta voisit tallentaa lisää niitä!");
                List<Picture> pics = pictureRepository.findByOmistajaId(omistaja.getId());
        model.addAttribute("person", omistaja); 
        model.addAttribute("pics", pics);
                    model.addAttribute("ownPage", true);
        return "kuvat";
    }
    Picture pic = new Picture();
    String mediatype = file.getContentType();
    if (mediatype.equals("image/jpeg") || mediatype.equals("image/png") || mediatype.equals("image/gif") || mediatype.equals("image/bmp")){
        pic.setMediaType(mediatype);
    } else {
        return "kuvat";
    }
    pic.setKoko(file.getSize());
    pic.setContent(file.getBytes());
    pic.setOmistajaId(omistaja.getId());
    pic.setKuvaus(kuvaus);

    pictureRepository.save(pic);

    return "redirect:/kuvat/" + profiili;
}
    
    @GetMapping("/kuvat/kuva/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        Picture pic = pictureRepository.getOne(id);

  
        final HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.IMAGE_GIF);
        headers.setContentType(MediaType.parseMediaType(pic.getMediaType()) );
        return new ResponseEntity<>(pic.getContent(), headers, HttpStatus.CREATED);
}
    
}
