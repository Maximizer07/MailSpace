package com.maximus.mailspace;

import com.maximus.mailspace.ConfirmationToken.ConfirmationToken;
import com.maximus.mailspace.ConfirmationToken.ConfirmationTokenRepository;
import com.maximus.mailspace.ConfirmationToken.ConfirmationTokenService;

import com.maximus.mailspace.Mail.Mail;
import com.maximus.mailspace.Mail.MailService;
import com.maximus.mailspace.User.User;
import com.maximus.mailspace.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class MyController implements ErrorController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;


    @PostMapping("/sign-up")
    String signUp(User user, Model model) {
        if (!user.getEmail().isEmpty()) {
            if (userService.uniqueEmail(user.getEmail())) {
                model.addAttribute("Open", true);
                model.addAttribute("Status", "busyemail");
                return "login";
            }
        }
        if(userService.uniqueUsername(user.getUsername())){
            model.addAttribute("Open", true);
            model.addAttribute("Status","busyusername");
            return "login";
        }
        model.addAttribute("status","noconfirm");
        userService.signUpUser(user);
        return "sign-up";
    }


    @GetMapping("sign-up/confirm")
    String confirmMail(@RequestParam("token") String token,Model model) {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        userService.confirmUser(optionalConfirmationToken.get());
        model.addAttribute("status","confirm");
        return "sign-up";
    }


    @GetMapping("/auth")
    public String viewLoginPage(Model model, User user)
    {
        model.addAttribute("Open", false);
        return "login";
    }

    @RequestMapping(value = "/user_info/changename", method = RequestMethod.POST)
    public @ResponseBody String addname(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String firstName = request.getParameter("firstname");
        String email = request.getParameter("email");
        userService.updateFirstName(userService.loadUserByUsername(email).getId(),firstName);
        return firstName;
    }


    @RequestMapping(value = "/user_info/changesurname", method = RequestMethod.POST)
    public @ResponseBody String addsurname(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        userService.updateSurName(userService.loadUserByUsername(email).getId(),surname);
        return surname;
    }


    @RequestMapping(path="/user_info")
    public String userinfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user",userService.loadUserByUsername(authentication.getName()));
        return "user_info";
    }


    @RequestMapping(path="/send")
    public String send(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user",userService.loadUserByUsername(authentication.getName()));
        return "send_mail";
    }

    @RequestMapping(path="/inbox")
    public String inbox(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("path","/inbox");
        model.addAttribute("chapter", "inbox");
        model.addAttribute("mails", mailService.findRecipientMail(user));
        return "main";
    }
    @RequestMapping(path="/find")
    public String find(Model model, @RequestParam("search") String search) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("path","/inbox");
        model.addAttribute("chapter", "inbox");
        model.addAttribute("search", search);
        model.addAttribute("mails", mailService.findMail(user, search));
        return "main";
    }


    @RequestMapping(path="/stared")
    public String stared(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user",user);
        model.addAttribute("path","/stared");
        model.addAttribute("chapter", "stared");
        model.addAttribute("mails",mailService.findStared(user, true));
        return "main";
    }
    @RequestMapping(path="/sent")
    public String sent(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("path","/sent");
        model.addAttribute("mails", mailService.findSenderMail(user));
        return "main";
    }
    @RequestMapping(path="/important")
    public String important(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        model.addAttribute("user",user);
        model.addAttribute("chapter", "important");
        model.addAttribute("mails",mailService.findImportant(user, true));
        model.addAttribute("path","/important");
        return "main";
    }

//    @RequestMapping(path="/add_draft")
//    public String add_draft(Model model,@RequestParam("body") String body,
//                            @RequestParam("topic") String topic, @RequestParam("recipient") String username) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.loadUserByUsername(authentication.getName());
//        model.addAttribute("user", user);
//        if (userService.uniqueUsername(username)){
//            return "redirect:/draft";
//        }
//        else {
//            Draft draft = new Draft();
//            draft.setBody(body);
//            draft.setSaveDate(LocalDate.now());
//            draft.setTopic(topic);
//            draft.setSender_user(user);
//            draft.setRecipient_user(userService.loadUserByUsername(username));
//            draftService.saveDraft(draft);
//        }
//        return "redirect:/draft";
//    }

    @RequestMapping(path="/send_mail")
    public String send_mail(Model model,@RequestParam("body") String body,
                            @RequestParam("topic") String topic, @RequestParam("recipient") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        System.out.println(topic);
        model.addAttribute("user", user);
        if (!userService.uniqueUsername(username)){
            return "redirect:/inbox";
        }
        else{
            Mail mail = new Mail();
            mail.setBody(body);
            mail.setSendingDate(LocalDate.now());
            mail.setTopic(topic);
            mail.setSender(user);
            mail.setRecipient(userService.loadUserByUsername(username));
            mailService.saveMail(mail);
        }
        return "redirect:/inbox";
    }
    @RequestMapping(path="/")
    public String home(Model model){
        return "redirect:/auth";
    }

    @RequestMapping(path="/view/{number}")
    public String view(Model model, @PathVariable(value = "number") int number) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        Optional<Mail> mail = mailService.findById(number);
        Mail m = mail.get();
        if (user != m.getRecipient() && user != m.getSender()){
            return "redirect:/inbox";
        }
        if (user != m.getSender() || user==m.getSender() && user== m.getRecipient()) {
            mailService.setReaded(m);
        }
        model.addAttribute("mail", m);
        model.addAttribute("user",user);
        return "view_mail";
    }

    @RequestMapping(path="/delete/{number}")
    public String deletemail(Model model, @PathVariable(value = "number") int number) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        Optional<Mail> mail = mailService.findById(number);
        Mail m = mail.get();
        if (user != m.getRecipient() && user != m.getSender()){
            return "redirect:/inbox";
        }
        model.addAttribute("user", user);
        mailService.deleteMail(number);
        return "redirect:/inbox";
    }

    @RequestMapping(path="/important/{number}")
    public String importantmail(Model model, @PathVariable(value = "number") int number) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        Optional<Mail> mail = mailService.findById(number);
        Mail m = mail.get();
        if (user != m.getRecipient()){
            return "redirect:/inbox";
        }
        model.addAttribute("user", user);
        mailService.setImportant(m);
        return "redirect:/inbox";
    }

    @RequestMapping(path="/stared/{number}")
    public String staredmail(Model model, @PathVariable(value = "number") int number) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(authentication.getName());
        Optional<Mail> mail = mailService.findById(number);
        Mail m = mail.get();
        if (user != m.getRecipient()){
            return "redirect:/inbox";
        }
        model.addAttribute("user", user);
        mailService.setStared(m);
        return "redirect:/inbox";
    }


    @Override
    public String getErrorPath() {
        return "user_info";
    }


}

