package rs.netset.training.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

public class Appointment implements Serializable {

	@NotNull(message = "Person for appointment is mandatory")
	private Person person;
	@NotNull(message = "Date and time for appointment is mandatory")
	@Future(message = "Date of appointment must be in future")
	private LocalDateTime dateTime;

	public Appointment() {
	}

	public Appointment(Person person, LocalDateTime localDateTime) {
		this.person = person;
		this.dateTime = localDateTime;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

}
