package rs.netset.training.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "appointment")
@NamedQueries({
		@NamedQuery(name = "Appointment.getByUniqueNumber", query = "SELECT a FROM Appointment a WHERE a.uniqueNumber=:uniqueNumber")
})
public class Appointment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "date_time")
	@NotNull(message = "Date and time for appointment is mandatory")
	@Future(message = "Date of appointment must be in future")
	private LocalDateTime dateTime;

	@Column(name = "unique_number")
	@NotNull(message = "Unique number for person is mandatory")
	@Size(min = 13, max = 13, message = "Unique number must have 13 digits")
	private String uniqueNumber;

	@Column(name = "first_name")
	@NotNull(message = "First name for pearson is mandatory")
	@Size(min = 2, message = "First name must have minimum 2 characters")
	private String firstName;

	@Column(name = "last_name")
	@NotNull(message = "Last name for pearson is mandatory")
	@Size(min = 2, message = "Last name must have minimum 2 characters")
	private String lastName;

	public Appointment() {
	}

	public Appointment(String firstName, String lastName, String uniqueNumber, LocalDateTime localDateTime) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.uniqueNumber = uniqueNumber;
		this.dateTime = localDateTime;
	}

	public String getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

}
