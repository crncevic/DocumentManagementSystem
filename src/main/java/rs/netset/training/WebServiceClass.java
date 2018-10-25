package rs.netset.training;

import java.time.LocalDateTime;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.constraints.NotNull;

import rs.netset.training.domain.Appointment;
import rs.netset.training.logic.AppointmentLogic;

@WebService
@Stateless
public class WebServiceClass {

	@EJB
	AppointmentLogic appointmentLogic;

	@WebMethod
	public Appointment registerAppointment(@NotNull Appointment appointment) {

		return appointmentLogic.registerAppointment(appointment);
	}
}
