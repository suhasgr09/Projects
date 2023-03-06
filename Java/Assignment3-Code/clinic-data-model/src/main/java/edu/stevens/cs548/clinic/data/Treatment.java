package edu.stevens.cs548.clinic.data;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import javax.persistence.Table;

import static javax.persistence.InheritanceType.JOINED;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import javax.persistence.JoinColumn;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Treatment
 *
 */
@NamedQueries({
	@NamedQuery(
		name="SearchTreatmentByTreatmentId",
		query="select t from Treatment t where t.treatmentId = :treatmentId"),
	@NamedQuery(
			name="CountTreatmentByTreatmentId",
			query="select count(t) from Treatment t where t.treatmentId = :treatmentId"),
	@NamedQuery(
		name = "RemoveAllTreatments", 
		query = "delete from Treatment t")
})

// TODO
@Entity
@Table(name="Treatment",indexes = @Index(columnList="treatmentId"))
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name="TREATMENT_TYPE")
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// TODO PK
	@Id
	@GeneratedValue
	protected long id;
	
	@Column(nullable=false,unique=true)
	@Convert("uuidConverter")
	protected UUID treatmentId;
	
	protected String diagnosis;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public UUID getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(UUID treatmentId) {
		this.treatmentId = treatmentId;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	/*
	 * TODO
	 */
	@ManyToOne(cascade = { PERSIST, REMOVE })
	@JoinColumn(name = "patient_fk", referencedColumnName = "id")
	private Patient patient;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		// More logic in the domain model.
	}

	/*
	 * TODO
	 */
	@ManyToOne(cascade = { PERSIST, REMOVE })
	@JoinColumn(name = "provider_fk", referencedColumnName = "id")
	private Provider providers;

	public Provider getProvider() {
		return providers;
	}

	public void setProvider(Provider provider) {
		this.providers = provider;
		// More logic in the domain model.
	}	
	public Treatment() {
		super();
		
	}
   
}
