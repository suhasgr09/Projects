package edu.stevens.cs548.clinic.data;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Converter;
import javax.persistence.ElementCollection;
import javax.persistence.OneToMany;

// TODO
@Entity
@Table(name="SurgeryTreatment")
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
public class SurgeryTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4173146640306267418L;
	
	@Temporal(TemporalType.DATE)
	private Date surgeryDate;
	
	private String dischargeInstructions;
	
	// TODO
	
	@OneToMany
	private Collection<Treatment> followupTreatments;

	public Date getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public String getDischargeInstructions() {
		return dischargeInstructions;
	}

	public void setDischargeInstructions(String dischargeInstructions) {
		this.dischargeInstructions = dischargeInstructions;
	}

	public Collection<Treatment> getFollowupTreatments() {
		return followupTreatments;
	}

	public void setFollowupTreatments(Collection<Treatment> followupTreatments) {
		this.followupTreatments = followupTreatments;
	}
	
	public SurgeryTreatment() {
		// TODO
		super();
	}

}
