package com.example.web_mvc.Controller;

import com.example.web_mvc.model.users.CreateClientDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class ClientWebController {

    private final RestTemplate restTemplate;

    private static final String BACKEND_URL = "http://peecee:8080/PAS_PD-1/api/v1/users/client";

    public ClientWebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        CreateClientDTO dto = new CreateClientDTO();

        dto.setIsActive(true);

        model.addAttribute("client", dto);
        return "/user/registration";
    }

    @PostMapping("/register")
    public String registerClient(
            @ModelAttribute("client") @Valid CreateClientDTO dto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "user/registration";
        }

        try {
            dto.setIsActive(true);

            restTemplate.postForEntity(BACKEND_URL, dto, Void.class);

            return "redirect:/login";

        } catch (HttpClientErrorException e) {
            model.addAttribute("serverError", "Błąd serwera: " + e.getResponseBodyAsString());
            return "/user/registration";

        } catch (Exception e) {
            model.addAttribute("serverError", "Nie można połączyć z serwerem.");
            return "/user/registration";
        }
    }

}