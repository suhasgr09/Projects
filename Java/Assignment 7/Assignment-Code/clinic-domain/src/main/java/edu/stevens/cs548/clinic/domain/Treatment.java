package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Convert;

/**
 * Entity implementation class for Entity: Treatment
 *
 */
@NamedQueries({
		@NamedQuery(name = "SearchTreatmentByTreatmentId", query = "select t from Treatment t where t.treatmentId = :treatmentId"),
		@NamedQuery(name = "SearchTreatmentWithFollowupsByTreatmentId", query = "select t from Treatment t left join fetch t.followupTreatments where t.treatmentId = :treatmentId"),
		@NamedQuery(name = "CountTreatmentByTreatmentId", query = "select count(t) from Treatment t where t.treatmentId = :treatmentId"),
		@NamedQuery(name = "RemoveAllTreatments", query = "delete from Treatment t") })

// TODO
@Entity
@Table(indexes = @Index(columnList = "treatmentId"))
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Type")
public abstract class Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	// TODO PK
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	// TODO
	@Column(nullable = false, unique = true)
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
	@ManyToOne
	protected Patient patient;

	public Patient getPatient() {
		return patient;
	}

	void setPatient(Patient patient) {
		this.patient = patient;
	}

	/*
	 * TODO
	 */
	@ManyToOne
	protected Provider provider;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	/*
	 * TODO
	 */
	@OneToMany(targetEntity = Treatment.class)
	@JoinTable(name = "followupTreatments")
	protected Collection<Treatment> followupTreatments;

	public void addFollowupTreatment(Treatment t) {
		followupTreatments.add(t);
	}

	/*
	 * We use the visitor pattern to access a treatment.
	 */
	public abstract <T> T export(ITreatmentExporter<T> visitor);

	protected final <T> List<T> exportFollowupTreatments(ITreatmentExporter<T> visitor) {
		List<T> exports = new ArrayList<T>();
		for (Treatment t : followupTreatments) {
			exports.add(t.export(visitor));
		}
		return exports;
	}

	public Treatment() {
		super();
		/*
		 * TODO initialize lists
		 */
		followupTreatments = new ArrayList<Treatment>();
	}
}
