package io.pivotal.pal.data.rentaltrucks.reservation.domain;

import io.pivotal.pal.data.rentaltruck.framework.event.AsyncEventPublisher;
import io.pivotal.pal.data.rentaltrucks.event.ReservationInitializedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReservationManager {

    private final ReservationRepository reservationRepository;
    private final RentalRepository rentalRepository;
    private final AsyncEventPublisher<ReservationInitializedEvent> eventPublisher;

    public ReservationManager(ReservationRepository reservationRepository,
                              RentalRepository rentalRepository,
                              AsyncEventPublisher<ReservationInitializedEvent> eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.rentalRepository = rentalRepository;
        this.eventPublisher = eventPublisher;
    }

    public Reservation createReservation(ReservationRequest reservationRequest) {

        // could be validation here

        // new up a reservation with status=requested
        Reservation reservation = new Reservation(
                reservationRequest.getConfirmationNumber(),
                "REQUESTED",
                reservationRequest.getPickupDate(),
                reservationRequest.getDropoffDate(),
                reservationRequest.getCustomerName()
        );
        reservationRepository.save(reservation);

        // emit reservation initialized event
        ReservationInitializedEvent event = new ReservationInitializedEvent(
                reservationRequest.getConfirmationNumber(),
                reservationRequest.getPickupDate(),
                reservationRequest.getDropoffDate()
        );
        eventPublisher.publish(event);

        return reservation;
    }

    public void finalizeReservation(Reservation reservation) {
        // ideally assigns a confirmation number but too bad we did this already
        // what logic actually belongs in reservation?
        reservation.finalizeMe();
        reservationRepository.save(reservation);

        // what else:  update some value or history object based on date of finalize event

        // updates the counters of trucks available (for lack of better impl)

        // sends email ?
    }

    public void failReservation(Reservation reservation) {
        reservation.failed();
        reservationRepository.save(reservation);
    }

    public void cancelReservation(Reservation reservation) {
        reservation.cancel();
        reservationRepository.save(reservation);
    }

    // returns confirmationNumber to identify Rental
    public String convertToRental(Reservation reservation, Integer pickupMileage) {
        Rental rental = new Rental(
                reservation.getConfirmationNumber(),
                "PICKED_UP",
                reservation.getStartDate(),
                reservation.getEndDate(),
                null,
                reservation.getCustomerName(),
                pickupMileage,
                null
        );
        rentalRepository.save(rental);

        return reservation.getConfirmationNumber();
    }

    public void dropoffRental(Rental rental, LocalDate dropoffDate, Integer dropoffMileage) {
        rental.dropoff(dropoffDate, dropoffMileage);
        rentalRepository.save(rental);

        // send dropoff receipt
    }
}
