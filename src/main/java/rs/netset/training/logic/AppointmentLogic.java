package rs.netset.training.logic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

import org.hibernate.validator.internal.xml.LocalNamespace;

import constants.Constants;
import rs.netset.training.db.GenericRepository;
import rs.netset.training.domain.Appointment;
import rs.netset.training.exception.ErrorCode;
import rs.netset.training.exception.NetSetException;
import rs.netset.training.util.SettingsLoader;

@Stateless
public class AppointmentLogic {

	@PersistenceContext
	private EntityManager em;
	
	private GenericRepository<Appointment> gra;
	private ValidatorFactory validationFactory;
	private Validator validator;
	private Set<ConstraintViolation<Appointment>> violationsAppointments;
	private int startTime;
	private int endTime;

	@PostConstruct
	private void init() {
		validationFactory = Validation.buildDefaultValidatorFactory();
		validator = validationFactory.getValidator();
		loadStartAndEndTime();
		gra = new GenericRepository<>(Appointment.class,em);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public synchronized Appointment registerAppointment(Appointment appointment) {

		if (checkForHardwareMaintanance()) {
			throw new NetSetException(
					"Service for appointments have hardvare maintenece every tuesday and thursday from 14 to 15 hours.",
					null, ErrorCode.SERVER_ERROR);
		}

		if (!isValidLocalDateTime(appointment.getDateTime())) {
			throw new NetSetException("Appointment time is not valid", null, ErrorCode.USER_ERROR);
		}

		if (!checkUniqueNumber(appointment.getUniqueNumber())) {
			throw new NetSetException("Unique number must consist only of digits.", null, ErrorCode.USER_ERROR);
		}

		violationsAppointments = validator.validate(appointment);

		if (violationsAppointments.size() > 0) {
			throw new NetSetException("Error in informations about appointment",
					new ConstraintViolationException(violationsAppointments), ErrorCode.USER_ERROR);
		}

		if (getAppointmentByJMBG(appointment.getUniqueNumber()) != null) {
			throw new NetSetException("Appointment for you already exists", null, ErrorCode.USER_ERROR);
		}

		gra.save(appointment);

		return appointment;

	}

	private boolean isValidLocalDateTime(LocalDateTime dateTime) {

		if (dateTime.getMinute() != 0 && dateTime.getMinute() != 15 && dateTime.getMinute() != 30
				&& dateTime.getMinute() != 45) {
			return false;
		}
		return true;

	}

	private boolean checkForHardwareMaintanance() {
		if ((LocalDateTime.now().getDayOfWeek() == DayOfWeek.TUESDAY && LocalDateTime.now().getHour() == 14)
				|| (LocalDateTime.now().getDayOfWeek() == DayOfWeek.THURSDAY && LocalDateTime.now().getHour() == 14)) {
			return true;
		}
		return false;
	}

	private Appointment getAppointmentByJMBG(String uniqueNumber) {
		return gra.getSingleByParamFromNamedQuery(new Object[] { uniqueNumber },
				new String[] { Constants.UNIQUE_NUMBER }, Constants.APPOINTMENT_FIND_BY_JMBG);
	}

	@WebMethod
	public List<LocalDateTime> getAllAvailableTimeSlots(LocalDate dateTime) {

		if (dateTime.isBefore(LocalDate.now())) {
			throw new NetSetException("Date that you provide is in the past!", null, ErrorCode.USER_ERROR);
		}

		List<LocalDateTime> allAvailableTimeslots = new ArrayList<>();
		List<Appointment> allAppointments = getAllApointments();
		List<Appointment> specificAppointemnts = new ArrayList<>();

		// TODO: Uraditi Bolje
		for (Appointment appointment : allAppointments) {
			if (appointment.getDateTime().getYear() == dateTime.getYear()
					&& appointment.getDateTime().getMonthValue() == dateTime.getMonthValue()
					&& appointment.getDateTime().getDayOfMonth() == dateTime.getDayOfMonth()) {
				specificAppointemnts.add(appointment);
			}
		}

		LocalDateTime ldt = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
				startTime, 0);

		LocalDateTime limit = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
				endTime, 0);

		while (ldt.isBefore(limit)) {
			boolean signal = false;
			for (Appointment appointment : specificAppointemnts) {
				if (ldt.equals(appointment.getDateTime())) {
					signal = true;
					break;
				}
			}

			if (!signal) {
				allAvailableTimeslots.add(ldt);
			}

			ldt = ldt.plusMinutes(15);
		}

		return allAvailableTimeslots;

	}

	public List<Appointment> getAllApointments() {
		return gra.getAll(Constants.APPOINTMENT_FIND_ALL);
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
