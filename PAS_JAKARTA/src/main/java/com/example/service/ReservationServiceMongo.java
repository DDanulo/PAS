package com.example.service;

import com.example.controller.exception.*;
import com.example.domain.Client;
import com.example.domain.Reservation;
import com.example.mappers.ReservationMapper;
import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.example.repository.UserRepository;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ReservationServiceMongo implements ReservationService {

    private final ReservationRepository repository;
    private final Provider<MongoClient> mongoClientProvider;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Inject
    public ReservationServiceMongo(ReservationRepository repository,
                                   Provider<MongoClient> mongoClientProvider,
                                   ReservationMapper reservationMapper,
                                   UserRepository userRepository,
                                   RoomRepository roomRepository) {
        this.repository = repository;
        this.mongoClientProvider = mongoClientProvider;
        this.reservationMapper = reservationMapper;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void makeReservation(CreateReservationDTO reservation) {
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.startTransaction();

            LocalDateTime start = reservation.getStartTime();
            List<ShowReservationDTO> allReservations = getAllReservations();

            for (ShowReservationDTO r : allReservations) {
                if (reservation.getRoomId().equals(r.getRoomId()) && r.getEndTime() == null) {
                    throw new RoomIsReservedException("W tym czasie istnieje już inna rezerwacja.");
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

            repository.add(session, r);

            session.commitTransaction();

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
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.withTransaction(() -> {
                repository.remove(session, objectId);
                return null;
            });
        }
    }

    @Override
    public void updateReservation(String id, CreateReservationDTO res) {
        if (id == null) {
            throw new NotFoundException("Wrong reservation id");
        }
        if (findReservation(id).isEmpty()) {
            throw new NotFoundException("Reservation not found");
        }
        ObjectId objectId = new ObjectId(id);
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.withTransaction(() -> {
                repository.update(session, objectId, reservationMapper.createReservationDTOToReservation(res));
                return null;
            });
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
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.startTransaction();
            Reservation reservation = repository.findById(objectId).orElseThrow(NotFoundException::new);
            if (reservation.getEndTime() != null) {
                session.abortTransaction();
                throw new ReservationIsAlreadyEndedException();
            }
            reservation.setEndTime(LocalDateTime.now());
            repository.update(session, objectId, reservation);
            session.commitTransaction();
        }
    }
}