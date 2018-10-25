package rs.netset.training.logic;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import constants.Constants;
import rs.netset.training.domain.Appointment;
import rs.netset.training.exception.ErrorCode;
import rs.netset.training.exception.NetSetException;
import rs.netset.training.util.SettingsLoader;

@Stateless
public class AppointmentLogic {

	// @EJB
	// ServiceBean serviceBean;

	@PersistenceContext
	EntityManager em;
	private ValidatorFactory validationFactory;
	private Validator validator;
	private Set<ConstraintViolation<Appointment>> violationsAppointments;
	private int startTime;
	private int endTime;

	@PostConstruct
	private void init() {
		validationFactory = Validation.buildDefaultValidatorFactory();
		validator = validationFactory.getValidator();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Appointment registerAppointment(Appointment appointment) {

		if (appointment.getDateTime().getMinute() != 0 && appointment.getDateTime().getMinute() != 15
				&& appointment.getDateTime().getMinute() != 30 && appointment.getDateTime().getMinute() != 45) {
			throw new NetSetException("Appointment time is not valid", null, ErrorCode.USER_ERROR);
		}

		if (!checkUniqueNumber(appointment.getUniqueNumber())) {
			throw new NetSetException("Unique number must consist only of digits.", null, ErrorCode.USER_ERROR);
		}

		violationsAppointments = validator.validate(appointment);

		if (violationsAppointments.size() > 0) {
			throw new ConstraintViolationException(violationsAppointments);
		}

		if (getAppointmentByJMBG(appointment.getUniqueNumber()) != null) {
			throw new NetSetException("Appointment for you already exists", null, ErrorCode.USER_ERROR);
		}

		em.persist(appointment);

		return appointment;

	}

	private Appointment getAppointmentByJMBG(String uniqueNumber) {
		TypedQuery<Appointment> query = em.createNamedQuery(Constants.APPOINTMENT_FIND_BY_JMBG, Appointment.class);
		query.setParameter(Constants.UNIQUE_NUMBER, uniqueNumber);
		List<Appointment> list = query.getResultList();

		if (list == null || list.isEmpty()) {
			return null;
		}

		return list.get(0);
	}

	@WebMethod
	public String getAllAvailableTimeSlots(String date) {

		/*
		 * String timeslots = ""; LocalDateTime localDateTime = makeAndVerifyDate(date);
		 * 
		 * if (!loadStartAndEndTime()) { return
		 * "Server error. System could not read properties file."; }
		 * 
		 * if (localDateTime == null) { return
		 * "Bad request. Please provide date in this format yyyy/MM/dd. Make sure that yyyy, MM,dd are positive numbers!"
		 * ; }
		 * 
		 * if (!checkTimeOfAppointment(localDateTime)) { return
		 * "Bad request. Please provide date that is in the future!"; }
		 * 
		 * timeslots = "\n AVAILABLE TIMESLOTS FOR " +
		 * localDateTime.getDayOfWeek().toString() + " " + localDateTime.getDayOfMonth()
		 * + "." + localDateTime.getMonthValue() + "." + localDateTime.getYear() +
		 * " \n";
		 * 
		 * String[] minutesHours = { "00", "15", "30", "45" };
		 * 
		 * for (int j = startTime; j < endTime; j++) {
		 * 
		 * for (String minutes : minutesHours) { timeslots += j + ":" + minutes + "h" +
		 * "\t"; } }
		 * 
		 * return timeslots;
		 */

		return "";
	}

	private boolean checkUniqueNumber(String uniqueNumber) {
		try {
			Long.parseLong(uniqueNumber);
			return true;
		} catch (Exception e) {
			return false;
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
