package com.example.web_mvc.Controller;

import com.example.web_mvc.model.users.ShowUserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class AuthWebController {


    private final RestTemplate restTemplate;

    private static final String API_URL = "http://localhost:8080/PAS_PD-1/api/v1/users";

    @GetMapping("/login")
    public String showLoginScreen(){
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login, HttpSession session, Model model){
        try {
            ShowUserDTO user = restTemplate.getForObject(API_URL + "/by-login/" + login, ShowUserDTO.class);
            if (user != null) {
                session.setAttribute("currentUser", user);
                return "redirect:/reservations";
            }

        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", "Nie znaleziono użytkownika o takim loginie.");
        } catch (Exception e) {
            model.addAttribute("error", "Błąd serwera: " + e.getMessage());
        }
        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
