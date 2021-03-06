package io.pivotal.pal.data.rentaltrucks.reservation.config;

import io.pivotal.pal.data.rentaltruck.framework.event.AsyncEventPublisher;
import io.pivotal.pal.data.rentaltruck.framework.event.DefaultAsyncEventPublisher;
import io.pivotal.pal.data.rentaltrucks.event.ReservationInitializedEvent;
import io.pivotal.pal.data.rentaltrucks.event.ReservationRequestedEvent;
import io.pivotal.pal.data.rentaltrucks.event.TruckAvailableEvent;
import io.pivotal.pal.data.rentaltrucks.event.TruckNotAvailableEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventPublisherConfig {

    @Bean
    public AsyncEventPublisher<ReservationRequestedEvent> reservationRequestedEventPublisher() {
        return new DefaultAsyncEventPublisher<>("reservation-requested");
    }

    @Bean
    public AsyncEventPublisher<ReservationInitializedEvent> reservationInitializedEventPublisher() {
        return new DefaultAsyncEventPublisher<>("reservation-initialized");
    }

    @Bean
    public AsyncEventPublisher<TruckAvailableEvent> truckAvailableEventPublisher() {
        return new DefaultAsyncEventPublisher<>("truck-available");
    }

    @Bean
    public AsyncEventPublisher<TruckNotAvailableEvent> truckNotAvailableEventPublisher() {
        return new DefaultAsyncEventPublisher<>("truck-not-available");
    }
}
