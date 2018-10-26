package rs.netset.training;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.constraints.NotNull;

import rs.netset.training.domain.Appointment;
import rs.netset.training.exception.ErrorCode;
import rs.netset.training.exception.NetSetException;
import rs.netset.training.logic.AppointmentLogic;

@WebService
@Stateless
public class WebServiceClass {

	@EJB
	AppointmentLogic appointmentLogic;

	public Appointment registerAppointment(@NotNull Appointment appointment) {

		try {
			return appointmentLogic.registerAppointment(appointment);
		} catch (NetSetException nse) {
			throw nse;
		} catch (Exception e) {
			throw new NetSetException("Server error" + e.getMessage(), e, ErrorCode.SERVER_ERROR);
		}
	}

	public List<LocalDateTime> getAllAvailableTimeSlots(@NotNull LocalDate dateTime) {
		try {
			return appointmentLogic.getAllAvailableTimeSlots(dateTime);

		} catch (NetSetException nse) {
			throw nse;
		} catch (Exception e) {
			throw new NetSetException("Server error" + e.getMessage(), e, ErrorCode.SERVER_ERROR);
		}
	}
}
