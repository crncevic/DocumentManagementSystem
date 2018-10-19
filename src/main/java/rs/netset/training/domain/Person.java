package rs.netset.training.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person {

	@NotNull(message = "Unique number for person is mandatory")
	@Size(min = 13, max = 13, message = "Unique number must have 13 digits")
	private String uniqueNumber;
	@NotNull(message = "First name for pearson is mandatory")
	@Size(min = 2, message = "First name must have minimum 2 characters")
	private String firstName;
	@NotNull(message = "Last name for pearson is mandatory")
	@Size(min = 2, message = "Last name must have minimum 2 characters")
	private String lastName;

	public Person() {
	}

	public Person(String uniqueNumber, String firstName, String lastName) {
		this.uniqueNumber = uniqueNumber;
		this.firstName = firstName;
		this.lastName = lastName;
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

}
