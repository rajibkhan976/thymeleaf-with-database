package se.ecutb.thymeleaf_db_lecture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.ecutb.thymeleaf_db_lecture.data.AppUserRepository;
import se.ecutb.thymeleaf_db_lecture.dto.CreateAppUserForm;
import se.ecutb.thymeleaf_db_lecture.entity.AppUser;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class AppUserController {

    private AppUserRepository appUserRepository;

    @Autowired
    public AppUserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    //                         domännamnet    / resurs på domän
    // /users/register/form == localhost:8080/users/register/form
    @GetMapping("users/register/form")
    public String getForm(Model model){
        model.addAttribute("form", new CreateAppUserForm());
        return "user-form";
    }

    @PostMapping("users/register/process")
    public String formProcess(@Valid @ModelAttribute("form") CreateAppUserForm form, BindingResult bindingResult){

        if(appUserRepository.findByEmailIgnoreCase(form.getEmail()).isPresent()){
            FieldError error = new FieldError("form", "email", "Email is already in use");
            bindingResult.addError(error);
        }

        if(!form.getPassword().equals(form.getPasswordConfirm())){
            FieldError error = new FieldError("form", "passwordConfirm", "Your confirmation didn't match password");
            bindingResult.addError(error);
        }

        if(bindingResult.hasErrors()){
            return "user-form";
        }

        appUserRepository.save(new AppUser(form.getFirstName(),form.getLastName(),form.getPassword(),form.getEmail(), LocalDate.now()));

        return "index";

    }

}

