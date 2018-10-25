
package rs.netset.training;

import java.time.LocalDateTime;

import javax.ejb.Startup;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.netset.training.domain.Appointment;

@Stateless
@Startup
public class ServiceBean {

	private Logger log = LoggerFactory.getLogger(ServiceBean.class);

	public void createAppointment(Appointment appointment) {

		/*
		 * log.info("Person with name " + appointment.getPerson().getFirstName() + " " +
		 * appointment.getPerson().getLastName() + "(unique number - " +
		 * appointment.getPerson().getUniqueNumber() + ") created appointment at " +
		 * LocalDateTime.now() + " for date: " + appointment.getDateTime().toString());
		 */
	}
}
