package edu.stevens.cs548.clinic.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.util.DateUtils;

//TODO JPA annotations
@Entity
@DiscriminatorValue("Radiology")
public class RadiologyTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656673416179492428L;

	// TODO  including order by date
	@ElementCollection
	protected List<Date> treatmentDates;

	public void addTreatmentDate(LocalDate date) {
		treatmentDates.add(DateUtils.toDatabaseDate(date));
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		// TODO
		return visitor.exportRadiology(treatmentId,
				   patient.getPatientId(),
				   patient.getName(),
				   provider.getProviderId(),
				   provider.getName(),
		   		   diagnosis,
		   		   DateUtils.fromDatabaseDates(treatmentDates),
		   		   () -> exportFollowupTreatments(visitor));
	}
	
	public RadiologyTreatment() {
		super();
		treatmentDates = new ArrayList<>();
	}
	
}
