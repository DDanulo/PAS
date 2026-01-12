package com.example.service;

import com.example.controller.exception.AccountNotActiveException;
import com.example.controller.exception.NotFoundException;
import com.example.controller.exception.ReservationHasEndedException;
import com.example.domain.Client;
import com.example.domain.Reservation;
import com.example.mappers.ReservationMapper;
import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;
import com.example.repository.UserRepository;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationServiceMongo implements ReservationService {
    private final ReservationRepository repository;
    private final MongoClient mongoClient;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    @Override
    public void makeReservation(CreateReservationDTO reservation) {
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                LocalDateTime start = reservation.getStartTime();
                List<ShowReservationDTO> allReservations = getAllReservations();


                for (ShowReservationDTO r : allReservations) {
                    if (reservation.getRoomId().toString().equals(r.getRoomId()) && r.getEndTime() == null) {
                        throw new Exception("W tym czasie istnieje już inna rezerwacja.");
                    }
                }

                if (!userRepository.findById(new ObjectId(reservation.getClientId()))
                        .orElseThrow(NotFoundException::new).getIsActive()) {
                    throw new AccountNotActiveException();
                }
                Reservation r = reservationMapper.createReservationDTOToReservation(reservation);
                r.setClient((Client) userRepository.findById(new ObjectId(reservation.getClientId()))
                        .orElseThrow(NotFoundException::new));
                r.setRoom(roomRepository.findById(new ObjectId(reservation.getRoomId()))
                        .orElseThrow(NotFoundException::new));
                r.setPrice(0.0);
                repository.add(session, r);

                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add reservation", e);
            }
        }

    }

    @Override
    public List<ShowReservationDTO> getAllReservations() {
        return repository.findAll().stream().map(reservationMapper::reservationToShowReservationDTO).toList();
    }

    @Override
    public Optional<ShowReservationDTO> findReservation(String id) {
        ObjectId objectId = new ObjectId(id);
        return repository.findById(objectId).map(reservationMapper::reservationToShowReservationDTO);
    }

    @Override
    public void removeReservation(String id) {
        ObjectId objectId = new ObjectId(id);

        if (findReservation(id).isEmpty()) {
            throw new NotFoundException("Reservation not found");
        }
        if (findReservation(id).get().getEndTime() != null) {
            throw new ReservationHasEndedException();
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.remove(session, objectId);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot remove reservation", e);
            }
        }
    }

    @Override
    public void updateReservation(String id, CreateReservationDTO res) {
        ObjectId objectId = new ObjectId(id);
        if (id == null) {
            throw new IllegalArgumentException("Wrong reservation id");
        }
        if (findReservation(id) == null) {
            throw new IllegalArgumentException("Reservation not found");
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.update(session, objectId, reservationMapper.createReservationDTOToReservation(res));
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot update reservation", e);
            }
        }
    }

    @Override
    public List<ShowReservationDTO> findCurrentForClient(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        List<Reservation> reservations = repository.findByClient(objectId);
        System.out.println(reservations);
        return repository.findByClient(objectId)
                .stream()
                .filter(reservation1 -> reservation1.getEndTime() == null)
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    @Override
    public List<ShowReservationDTO> findPastForClient(String clientId) {
        ObjectId objectId = new ObjectId(clientId);
        return repository.findByClient(objectId)
                .stream()
                .filter(reservation1 -> reservation1.getEndTime() != null)
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    @Override
    public List<ShowReservationDTO> findCurrentForRoom(String roomId) {
        ObjectId objectId = new ObjectId(roomId);
        return repository.findByRoom(objectId)
                .stream()
                .filter(reservation1 -> reservation1.getEndTime().isAfter(LocalDateTime.now()))
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    @Override
    public List<ShowReservationDTO> findPastForRoom(String roomId) {
        ObjectId objectId = new ObjectId(roomId);
        return repository.findByRoom(objectId)
                .stream()
                .filter(reservation1 -> reservation1.getEndTime().isBefore(LocalDateTime.now()))
                .map(reservationMapper::reservationToShowReservationDTO)
                .toList();
    }

    @Override
    public void endReservation(String id) {
        ObjectId objectId = new ObjectId(id);
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                Reservation reservation = repository.findById(objectId).orElseThrow(NotFoundException::new);
                reservation.setEndTime(LocalDateTime.now());

                Duration duration = Duration.between(reservation.getStartTime(), reservation.getEndTime());
                long reservationTime = duration.toHours();

                if (reservationTime == 0) {
                    reservationTime = 1;
                }

                double resPrice = reservation.getRoom().getBasePrice() * reservationTime;
                reservation.setPrice(resPrice);

                repository.update(session, objectId, reservation);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot update reservation", e);
            }
        }
    }
}
