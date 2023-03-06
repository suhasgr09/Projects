package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import edu.stevens.cs548.clinic.domain.ITreatmentDao.TreatmentExn;



/**
 * Entity implementation class for Entity: Patient
 *
 */
@NamedQueries({
	@NamedQuery(
		name="SearchProviderByProviderId",
		query="select p from Provider p where p.providerId = :providerId"),
	@NamedQuery(
		name="CountProviderByProviderId",
		query="select count(p) from Provider p where p.providerId = :providerId"),
	@NamedQuery(
			name = "SearchAllProviders", 
			query = "select p from Provider p"),
	@NamedQuery(
		name = "RemoveAllProviders", 
		query = "delete from Provider p")
})
// TODO
@Entity
@Table(indexes = @Index(columnList="providerId"))
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
public class Provider implements Serializable {
		
	private static final long serialVersionUID = -876909316791083094L;

	// TODO JPA annotations
	@Id
	@GeneratedValue
	private long id;
	
	// TODO
	@Column(name="providerId", unique=true, nullable=false)
	@Convert("uuidConverter")
	private UUID providerId;
	
	private String npi;

	private String name;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public UUID getProviderId() {
		return providerId;
	}

	public void setProviderId(UUID providerId) {
		this.providerId = providerId;
	}

	public String getNpi() {
		return npi;
	}

	public void setNpi(String npi) {
		this.npi = npi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// TODO JPA annotations (propagate deletion of provider to treatments)
	@OneToMany(cascade=CascadeType.ALL, mappedBy="provider")
	private Collection<Treatment> treatments;

	@Transient
	private ITreatmentDao treatmentDao;
	
	public void setTreatmentDao (ITreatmentDao tdao) {
		this.treatmentDao = tdao;
	}
	
	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
	
	/*
	 * Addition and deletion of treatments should be done here.
	 */
	
	public boolean administers(Treatment t) {
		return treatments.contains(t);
	}
	
	public void addTreatment (Patient p, Treatment t) {
		treatmentDao.addTreatment(t);
		/*
		 * TODO complete this operation (see patient entity)
		 */
		
		t.setPatient(p);//comment this 
		treatments.add(t);
		p.addTreatment(t);
		t.setProvider(p, this);//comment this
	}
	
	/**
	 * TODO complete this operation (see patient entity)
	 *
	 * Export a treatment without violating Aggregate pattern.
	 * Check that the exported treatment is a treatment for this provider.
	 * 
	 */
//	public <T> T exportTreatment(UUID tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
//		Treatment t = treatmentDao.getTreatment(tid);
//		
//		if (t.getProvider() != this) {
//			throw new TreatmentExn("Inappropriate treatment access: provider = " + id + ", treatment = " + tid);
//		}
//
//		return t.export(visitor);
//	}
	
	public <T> T exportTreatment(UUID tid, ITreatmentExporter<T> visitor) throws TreatmentExn {

		Treatment t = treatmentDao.getTreatment(tid);
		
		if (t.getProvider().getProviderId() != this.providerId) {
			throw new TreatmentExn("Inappropriate treatment access: provider = " + id + ", treatment = " + tid);
		}

		return t.export(visitor);
	}
	/**
	 * Map the treatment exporter over all of the treatments for this provider
	 */
	public <T> List<T> exportTreatments(ITreatmentExporter<T> visitor) throws TreatmentExn {
		List<T> exports = new ArrayList<T>();
		
		for (Treatment t : treatments) {
			exports.add(t.export(visitor));
		}
		
		return exports;
	}

	
}
