package edu.stevens.cs548.clinic.data;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Converter;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

// TODO
@Entity
@Table(name="RadiologyTreatment")
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
public class RadiologyTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656673416179492428L;

	// TODO 
	@ElementCollection
	protected Collection<Date> treatmentDates;
	
	// TODO
	
	@OneToMany
	protected Collection<Treatment> followupTreatments;

	public Collection<Date> getTreatmentDates() {
		return treatmentDates;
	}

	public void setTreatmentDates(Collection<Date> treatmentDates) {
		this.treatmentDates = treatmentDates;
	}

	public Collection<Treatment> getFollowupTreatments() {
		return followupTreatments;
	}

	public void setFollowupTreatments(Collection<Treatment> followupTreatments) {
		this.followupTreatments = followupTreatments;
	}
	
	public RadiologyTreatment() {
		super();
		// TODO
	}
	
}
