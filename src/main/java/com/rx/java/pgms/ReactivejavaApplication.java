package com.rx.java.pgms;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactivejavaApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ReactivejavaApplication.class, args);
		
		Thread.sleep(10000);
	}

}

@Component
class DataInitializer implements ApplicationRunner {
	
	private final ReservationRepository reservationRepository;
	
	DataInitializer (ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		Flux <String> names = Flux.just("peach", "julie", "ravi", "chandra", "raj", "bitraunta", "divya");
		Flux <Reservation> reservationFlux = names.map(name -> new Reservation (null, name) ); 
		Flux <Reservation>  map = reservationFlux.flatMap (this.reservationRepository:: save);
		map.subscribe (System.out::println);
		
	}
	
	
}

 interface ReservationRepository extends ReactiveMongoRepository <Reservation, String> {
	
	Flux <Reservation> findByReservation(final String reservation);
}

@Document
class Reservation {
	
	@Id
	private String id;
	private String reservation;
	
	public Reservation () {
		
	}
	
	public Reservation(String id, String reservation) {
		this.id = id;
		this.reservation = reservation;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReservation() {
		return reservation;
	}
	public void setReservation(String reservation) {
		this.reservation = reservation;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", reservation=" + reservation + "]";
	}
	
	
}