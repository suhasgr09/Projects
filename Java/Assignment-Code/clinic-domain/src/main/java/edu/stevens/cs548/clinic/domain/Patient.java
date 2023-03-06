package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import edu.stevens.cs548.clinic.domain.ITreatmentDao.TreatmentExn;
import edu.stevens.cs548.clinic.util.DateUtils;

/**
 * Entity implementation class for Entity: Patient
 *
 */
@NamedQueries({
	@NamedQuery(
		name="SearchPatientByPatientId",
		query="select p from Patient p where p.patientId = :patientId"),
	@NamedQuery(
			name="SearchPatientWithTreatmentsByPatientId",
			query="select p from Patient p left join fetch p.treatments where p.patientId = :patientId"),
	@NamedQuery(
		name="CountPatientByPatientId",
		query="select count(p) from Patient p where p.patientId = :patientId"),
	@NamedQuery(
		name = "SearchAllPatients", 
		query = "select p from Patient p"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p")
})

// TODO
@Entity
@Table(indexes = @Index(columnList="patientId"))
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
public class Patient implements Serializable {
		
	private static final long serialVersionUID = -4512912599605407549L;

	// TODO PK
	@Id
	@GeneratedValue
	private long id;
	
	// TODO
	@Column(name="patientId", unique=true, nullable=false)
	@Convert("uuidConverter")
	private UUID patientId;
				
	private String name;

	// TODO
	@Temporal(TemporalType.DATE)
	private Date dob;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public UUID getPatientId() {
		return patientId;
	}

	public void setPatientId(UUID patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDob() {
		return DateUtils.fromDatabaseDate(dob);
	}

	public void setDob(LocalDate dob) {
		this.dob = DateUtils.toDatabaseDate(dob);
	}


	// TODO JPA annotations (propagate deletion of patient to treatments)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
	private Collection<Treatment> treatments;
	

	@Transient
	private ITreatmentDao treatmentDao;
	
	public void setTreatmentDao (ITreatmentDao tdao) {
		this.treatmentDao = tdao;
	}
	
	/**
	 * This should only be called from Provider.addTreatment()
	 */
	void addTreatment (Treatment t) {
		// Persist treatment and set forward and backward links
		treatments.add(t);
		treatmentDao.addTreatment(t);
		t.setPatient(this);
	}
	
//	public <T> T exportTreatment(UUID tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
//		// Export a treatment without violating Aggregate pattern
//		// Check that the exported treatment is a treatment for this patient.
//		Treatment t = treatmentDao.getTreatment(tid);
//		if (t.getPatient() != this) {
//			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + ", treatment = " + tid);
//		}
//		return t.export(visitor);
//	}
	
	public <T> T exportTreatment(UUID tid, ITreatmentExporter<T> visitor) throws TreatmentExn {

		Treatment t = treatmentDao.getTreatment(tid);
		
		if (t.getPatient().getPatientId() != this.patientId) {
			throw new TreatmentExn("Inappropriate treatment access: provider = " + id + ", treatment = " + tid);
		}

		return t.export(visitor);
	}
	/**
	 * Map the treatment exporter over all of the treatments for this patient
	 */
	public <T> List<T> exportTreatments(ITreatmentExporter<T> visitor) throws TreatmentExn {
		List<T> exports = new ArrayList<T>();
		
		for (Treatment t : treatments) {
			exports.add(t.export(visitor));
		}
		
		return exports;
	}
	
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
	
}
