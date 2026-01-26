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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String Reservations(Model model) {
        ShowReservationDTO[] response = restTemplate.getForObject(API_URL, ShowReservationDTO[].class);
        List<ShowReservationDTO> reservations = response != null ? Arrays.asList(response) : List.of();
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }

    @PostMapping("/delete/{id}")
    public String removeReservation(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.exchange(API_URL + "/" + id, HttpMethod.DELETE, null, Void.class);
        } catch (HttpClientErrorException e) {
            System.err.println("Nie udało się usunąć rezerwacji: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getResponseBodyAsString());
        }

        return "redirect:/reservations";

    }

    @PostMapping("/end/{id}")
    public String endReservation(@PathVariable String id,
                                 RedirectAttributes redirectAttributes) {
        try {
            restTemplate.postForLocation(API_URL + "/" + id + "/end", null);
        } catch (HttpClientErrorException e) {
            System.err.println("Nie udało się zakończyć rezerwację: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getResponseBodyAsString());
        }

        return "redirect:/reservations";
    }

    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        ShowUserDTO user = (ShowUserDTO) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("rooms", fetchRooms());

        CreateReservationDTO newReservation = new CreateReservationDTO();
        newReservation.setStartTime(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES));
        model.addAttribute("reservation", newReservation);

        return "/reservations/add";
    }

    @PostMapping("/add")
    public String addNewRoom(@ModelAttribute("reservation") @Valid CreateReservationDTO reservationDTO,
                             BindingResult result,
                             HttpSession session,
                             Model model) {

        ShowUserDTO user = (ShowUserDTO) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            ShowRoomDTO[] rooms = restTemplate.getForObject(ROOM_API_URL, ShowRoomDTO[].class);
            model.addAttribute("rooms", Arrays.asList(rooms));
            return "reservations/add";
        }
        try {
            reservationDTO.setClientId(user.getId());
            System.out.println(reservationDTO.getRoomId() + " " + reservationDTO.getRoomId() + " " + reservationDTO.getStartTime() + " " + reservationDTO.getPrice());
            restTemplate.postForEntity(API_URL, reservationDTO, Void.class);
            return "redirect:/reservations";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", e.getResponseBodyAsString());
            model.addAttribute("rooms", fetchRooms());
            return "reservations/add";
        }
    }

    private List<ShowRoomDTO> fetchRooms() {
        try {
            ShowRoomDTO[] roomsResponse = restTemplate.getForObject(ROOM_API_URL, ShowRoomDTO[].class);
            return roomsResponse != null ? Arrays.asList(roomsResponse) : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
}