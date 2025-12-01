package com.example.web_mvc.Controller;

import com.example.web_mvc.model.CreateReservationDTO;
import com.example.web_mvc.model.ShowReservationDTO;
import com.example.web_mvc.model.ShowRoomDTO;
import com.example.web_mvc.model.users.ShowUserDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationWebController {

    private final RestTemplate restTemplate;

    private static final String API_URL = "http://localhost:8080/PAS_PD-1/api/v1/reservations";
    private static final String ROOM_API_URL = "http://localhost:8080/PAS_PD-1/api/v1/rooms";

    @GetMapping
    public String myReservations(HttpSession session, Model model) {
        ShowUserDTO user = (ShowUserDTO) session.getAttribute("currentUser");
        System.out.println(user.getId());
        if (user == null) {
            return "redirect:/login";
        }

        try {
            String url = API_URL + "/clients/" + user.getId() + "/reservations?status=current";

            ShowReservationDTO[] response = restTemplate.getForObject(url, ShowReservationDTO[].class);
            List<ShowReservationDTO> reservations = response != null ? Arrays.asList(response) : List.of();

            model.addAttribute("reservations", reservations);
            model.addAttribute("user", user);

            return "reservations/list";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            return "reservations/list";
        }
    }

    @PostMapping("/delete/{id}")
    public String removeReservation(@PathVariable String id) {
        try {
        restTemplate.exchange(API_URL + "/" + id, HttpMethod.DELETE, null, Void.class);

        } catch (Exception e) {
            System.err.println("Nie udało się usunąć rezerwacji: " + e.getMessage());
        }

        return "redirect:/reservations";

    }

    @PostMapping("/end/{id}")
    public String endReservation(@PathVariable String id) {
        try {
            restTemplate.postForLocation(API_URL + "/" + id + "/end", null);
        } catch (Exception e) {
            System.err.println("Nie udało się zakończyć rezerwację: " + e.getMessage());
        }

        return "redirect:/reservations";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        ShowUserDTO user = (ShowUserDTO) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";

        try {
            ShowRoomDTO[] roomsResponse = restTemplate.getForObject(ROOM_API_URL, ShowRoomDTO[].class);
            List<ShowRoomDTO> rooms = roomsResponse != null ? Arrays.asList(roomsResponse) : List.of();
            model.addAttribute("rooms", rooms);
        } catch (Exception e) {
            model.addAttribute("rooms", List.of());
        }

        CreateReservationDTO newReservation = new CreateReservationDTO();
        newReservation.setStartTime(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES));
        model.addAttribute("reservation", newReservation);

        return "/reservations/add";
    }

    @PostMapping("/add")
    public String addNewRoom(@ModelAttribute("reservation") @Valid CreateReservationDTO reservationDTO,
                             BindingResult result,
                             HttpSession session,
                             Model model){

        ShowUserDTO user = (ShowUserDTO) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";
        System.out.println("dupa");
        if (result.hasErrors()) {
            System.out.println("dupa");
            System.out.println(result.getAllErrors());
            try {
                ShowRoomDTO[] rooms = restTemplate.getForObject(ROOM_API_URL, ShowRoomDTO[].class);
                model.addAttribute("rooms", Arrays.asList(rooms));
            } catch (Exception e) {
                System.out.println("błąd");
            }
            return "reservations/add";
        }
        try {
            reservationDTO.setClientId(user.getId());
            System.out.println(reservationDTO.getRoomId() + " " + reservationDTO.getRoomId() + " " + reservationDTO.getStartTime() + " " + reservationDTO.getPrice());
            restTemplate.postForEntity(API_URL, reservationDTO, Void.class);
            return "redirect:/reservations";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd zapisu: " + e.getMessage());
            return "reservations/add";
        }
//        return "redirect:/reservations";
    }
}