package rs.netset.training;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Comparator;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import constants.Constants;
import rs.netset.training.domain.Appointment;
import rs.netset.training.domain.Person;
import rs.netset.training.util.SettingsLoader;

@WebService
@Stateless
public class WebServiceClass {

	@EJB
	ServiceBean serviceBean;
	private ValidatorFactory validationFactory;
	private Validator validator;
	private Set<ConstraintViolation<Person>> violationsPerson;
	private Set<ConstraintViolation<Appointment>> violationsAppointments;
	private int startTime;
	private int endTime;

	@PostConstruct
	private void init() {
		validationFactory = Validation.buildDefaultValidatorFactory();
		validator = validationFactory.getValidator();

	}

	@WebMethod
	public String registerAppointment(@NotNull String firstName, @NotNull String lastName, @NotNull String uniqueNumber,
			@NotNull String dateTime) {

		String message = "Central office is closed due to the reconstruction. It will reopen on Friday.";
		String messageError = "";

		LocalDateTime localDateTime = makeAndVerifyLocalDateTime(dateTime);

		if (localDateTime == null) {
			return "Bad request. Please provide valid date time format(yyyy/MM/dd/hh/mm). Also make sure that minutes can only be 0,15,30,45";
		}

		if (!checkUniqueNumber(uniqueNumber)) {
			return "Bad request.Unique number must consist only of digits.";
		}

		Person person = new Person(uniqueNumber, firstName, lastName);
		Appointment appointment = new Appointment(person, localDateTime);

		violationsPerson = validator.validate(person);
		violationsAppointments = validator.validate(appointment);

		if (violationsPerson.size() > 0) {
			messageError = "Bad request. There is errors in your request for informations about yourself:";
			int i = 1;
			for (ConstraintViolation<Person> constraintViolation : violationsPerson) {
				messageError += "\n " + (i++) + ". " + constraintViolation.getMessage();
			}
		}

		if (violationsAppointments.size() > 0) {
			messageError += "\n Bad request. There is errors in your request for informations about your appointment:";
			int i = 1;
			for (ConstraintViolation<Appointment> constraintViolation : violationsAppointments) {
				messageError += "\n " + (i++) + ". " + constraintViolation.getMessage();
			}
		}

		if (messageError.trim().length() > 0) {
			return messageError;
		} else {
			serviceBean.createAppointment(appointment);
			return message;
		}

	}

	@WebMethod
	public String getAllAvailableTimeSlots(String date) {

		String timeslots = "";
		LocalDateTime localDateTime = makeAndVerifyDate(date);

		if (!loadStartAndEndTime()) {
			return "Server error. System could not read properties file.";
		}

		if (localDateTime == null) {
			return "Bad request. Please provide date in this format yyyy/MM/dd";
		}

		if (!checkTimeOfAppointment(localDateTime)) {
			return "Bad request. Please provide date that is in the future!";
		}

		timeslots = "\n AVAILABLE TIMESLOTS FOR " + localDateTime.getDayOfWeek().toString() + " "
				+ localDateTime.getDayOfMonth() + "." + localDateTime.getMonthValue() + "." + localDateTime.getYear()
				+ " \n";

		String[] minutesHours = { "00", "15", "30", "45" };

		for (int j = startTime; j < endTime; j++) {

			for (String minutes : minutesHours) {
				timeslots += j + ":" + minutes + "h" + "\t";
			}
		}

		return timeslots;
	}

	private boolean checkifItIsSameDay(LocalDateTime localDateTime) {
		return (localDateTime.getYear() == LocalDateTime.now().getYear()
				&& localDateTime.getMonthValue() == LocalDateTime.now().getMonthValue()
				&& localDateTime.getDayOfMonth() == LocalDateTime.now().getDayOfMonth());
	}

	private boolean checkTimeOfAppointment(LocalDateTime localDateTime) {
		if (localDateTime.getYear() > LocalDateTime.now().getYear()) {
			return true;
		} else if (localDateTime.getYear() == LocalDateTime.now().getYear()) {

			if (localDateTime.getMonthValue() > LocalDateTime.now().getMonthValue()) {
				return true;
			} else if (localDateTime.getMonthValue() == LocalDateTime.now().getMonthValue()) {

				if (localDateTime.getDayOfMonth() > LocalDateTime.now().getDayOfMonth()) {
					return true;
				}
			}
		}
		return false;
	}

	private LocalDateTime makeAndVerifyDate(String date) {
		try {

			String[] array = date.split("/");

			if (array.length != 3) {
				return null;
			}

			return LocalDateTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]),
					0, 0);

		} catch (Exception ex) {
			return null;
		}
	}

	private boolean checkUniqueNumber(String uniqueNumber) {
		try {
			Long.parseLong(uniqueNumber);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private LocalDateTime makeAndVerifyLocalDateTime(String dateTime) {

		try {
			String[] array = dateTime.split("/");

			if (array.length != 5) {
				return null;
			}

			LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
					Integer.parseInt(array[2]), Integer.parseInt(array[3]), Integer.parseInt(array[4]));

			if (localDateTime.getMinute() != 0 && localDateTime.getMinute() != 15 && localDateTime.getMinute() != 30
					&& localDateTime.getMinute() != 45) {
				return null;
			}

			return localDateTime;
		} catch (Exception ex) {
			return null;
		}
	}

	private boolean loadStartAndEndTime() {
		try {
			startTime = Integer.parseInt(SettingsLoader.getInstance().getValue(Constants.START_WORK));
			endTime = Integer.parseInt(SettingsLoader.getInstance().getValue(Constants.END_WORK));
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
