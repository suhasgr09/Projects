package edu.stevens.cs548.clinic.service.init;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import edu.stevens.cs548.clinic.service.IPatientService;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.PhysiotherapyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDtoFactory;

@Singleton
@LocalBean
@Startup
//@ApplicationScoped
//@Transactional
public class InitBean {

	private static final Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	private static final ZoneId ZONE_ID = ZoneOffset.UTC;

	private PatientDtoFactory patientFactory = new PatientDtoFactory();

	private ProviderDtoFactory providerFactory = new ProviderDtoFactory();

	private TreatmentDtoFactory treatmentFactory = new TreatmentDtoFactory();

	// TODO
	@Inject
	private IPatientService patientService;

	// TODO
	@Inject
	private IProviderService providerService;

	/*
	 * Initialize using EJB logic
	 */
	
	/*
	 * This should work to initialize with CDI bean, but there is a bug in
	 * Payara.....
	 */
	// public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext init) {
	@PostConstruct
	public void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the
		 * server logs.
		 */
		logger.info("Your name here: Suhas Gorur Ravi Kumar");
		System.err.println("Suhas Gorur Ravi Kumar");

		try {

			/*
			 * Clear the database and populate with fresh data.
			 * 
			 * Note that the service generates the external ids, when adding the entities.
			 */

			providerService.removeAll();
			patientService.removeAll();

			PatientDto John = patientFactory.createPatientDto();
			John.setName("John");
			John.setDob(LocalDate.parse("1993-01-01"));
			John.setId(patientService.addPatient(John));

			ProviderDto Jane = providerFactory.createProviderDto();
			Jane.setName("Jane");
			Jane.setNpi("1234");
			Jane.setId(providerService.addProvider(Jane));

			DrugTreatmentDto drug01 = treatmentFactory.createDrugTreatmentDto();
			drug01.setPatientId(John.getId());
			drug01.setPatientName(John.getName());
			drug01.setProviderId(Jane.getId());
			drug01.setProviderName(Jane.getName());
			drug01.setDiagnosis("Fever");
			drug01.setDrug("Crocin");
			drug01.setDosage(21);
			drug01.setFrequency(01);
			drug01.setStartDate(LocalDate.parse("2022-11-05"));
			drug01.setEndDate(LocalDate.parse("2022-11-09"));
			drug01.setId(providerService.addTreatment(drug01));


			//jane.addTreatment(drug01);
			// TODO add more testing, including treatments and providers
			
			SurgeryTreatmentDto surgery01 = treatmentFactory.createSurgeryTreatmentDto();
			surgery01.setPatientId(John.getId());
			surgery01.setPatientName(John.getName());
			surgery01.setProviderId(Jane.getId());
			surgery01.setProviderName(Jane.getName());
			surgery01.setDiagnosis("Cancer");
			surgery01.setSurgeryDate(LocalDate.parse("2022-11-05"));
			surgery01.setDischargeInstructions("Drink Lot's of water, Keep calm , go walking");
			surgery01.setId(providerService.addTreatment(surgery01));
			
			RadiologyTreatmentDto radiology01 = treatmentFactory.createRadiologyTreatmentDto();
			radiology01.setPatientId(John.getId());
			radiology01.setPatientName(John.getName());
			radiology01.setProviderId(Jane.getId());
			radiology01.setProviderName(Jane.getName());
			radiology01.setDiagnosis("Cancer cells");
			radiology01.setTreatmentDates(Arrays.asList(LocalDate.parse("2022-11-05"),LocalDate.parse("2022-12-05")));
			radiology01.setId(providerService.addTreatment(radiology01));
			
			PhysiotherapyTreatmentDto physiotherapy01 = treatmentFactory.createPhysiotherapyTreatmentDto();
			physiotherapy01.setPatientId(John.getId());
			physiotherapy01.setPatientName(John.getName());
			physiotherapy01.setProviderId(Jane.getId());
			physiotherapy01.setProviderName(Jane.getName());
			physiotherapy01.setDiagnosis("BackPain");
			physiotherapy01.setTreatmentDates(Arrays.asList(LocalDate.parse("2022-11-05"),LocalDate.parse("2022-12-05")));
			physiotherapy01.setFollowupTreatments(Arrays.asList(drug01, surgery01, radiology01));
			physiotherapy01.setId(providerService.addTreatment(physiotherapy01));	
			
			
			PatientDto suhas = patientFactory.createPatientDto();
			suhas.setName("suhas");
			suhas.setDob(LocalDate.parse("1994-02-02"));
			suhas.setId(patientService.addPatient(suhas));

			ProviderDto kumar = providerFactory.createProviderDto();
			kumar.setName("kumar");
			kumar.setNpi("5678");
			kumar.setId(providerService.addProvider(kumar));

			DrugTreatmentDto drug02 = treatmentFactory.createDrugTreatmentDto();
			drug02.setPatientId(suhas.getId());
			drug02.setPatientName(suhas.getName());
			drug02.setProviderId(kumar.getId());
			drug02.setProviderName(kumar.getName());
			drug02.setDiagnosis("cough");
			drug02.setDrug("nyteQuil");
			drug02.setDosage(01);
			drug02.setFrequency(04);
			drug02.setStartDate(LocalDate.parse("2022-11-05"));
			drug02.setEndDate(LocalDate.parse("2022-11-09"));
			drug02.setId(providerService.addTreatment(drug02));

			SurgeryTreatmentDto surgery02 = treatmentFactory.createSurgeryTreatmentDto();
			surgery02.setPatientId(suhas.getId());
			surgery02.setPatientName(suhas.getName());
			surgery02.setProviderId(kumar.getId());
			surgery02.setProviderName(kumar.getName());
			surgery02.setDiagnosis("Appendix");
			surgery02.setSurgeryDate(LocalDate.parse("2022-11-05"));
			surgery02.setDischargeInstructions("Drink Water, Ice cream , More solid food");
			surgery02.setId(providerService.addTreatment(surgery02));
			
			RadiologyTreatmentDto radiology02 = treatmentFactory.createRadiologyTreatmentDto();
			radiology02.setPatientId(suhas.getId());
			radiology02.setPatientName(suhas.getName());
			radiology02.setProviderId(kumar.getId());
			radiology02.setProviderName(kumar.getName());
			radiology02.setDiagnosis("Fracture");
			radiology02.setTreatmentDates(Arrays.asList(LocalDate.parse("2022-11-05"),LocalDate.parse("2022-12-05")));
			radiology02.setId(providerService.addTreatment(radiology02));
			
			PhysiotherapyTreatmentDto physiotherapy02 = treatmentFactory.createPhysiotherapyTreatmentDto();
			physiotherapy02.setPatientId(suhas.getId());
			physiotherapy02.setPatientName(suhas.getName());
			physiotherapy02.setProviderId(kumar.getId());
			physiotherapy02.setProviderName(kumar.getName());
			physiotherapy02.setDiagnosis("Leg Pain");
			physiotherapy02.setTreatmentDates(Arrays.asList(LocalDate.parse("2022-11-05"),LocalDate.parse("2022-12-05")));
			physiotherapy02.setFollowupTreatments(Arrays.asList(drug02, surgery02, radiology02));
			physiotherapy02.setId(providerService.addTreatment(physiotherapy02));	
			
			
			PatientDto raven = patientFactory.createPatientDto();
			raven.setName("raven");
			raven.setDob(LocalDate.parse("1995-03-03"));
			raven.setId(patientService.addPatient(raven));

			ProviderDto rob = providerFactory.createProviderDto();
			rob.setName("rob");
			rob.setNpi("9874");
			rob.setId(providerService.addProvider(rob));

			DrugTreatmentDto drug03 = treatmentFactory.createDrugTreatmentDto();
			drug03.setPatientId(raven.getId());
			drug03.setPatientName(raven.getName());
			drug03.setProviderId(rob.getId());
			drug03.setProviderName(rob.getName());
			drug03.setDiagnosis("Headache");
			drug03.setDrug("Asprine");
			drug03.setDosage(20);
			drug03.setFrequency(03);
			drug03.setStartDate(LocalDate.parse("2022-11-05"));
			drug03.setEndDate(LocalDate.parse("2022-11-09"));
			drug03.setId(providerService.addTreatment(drug03));

			SurgeryTreatmentDto surgery03 = treatmentFactory.createSurgeryTreatmentDto();
			surgery03.setPatientId(raven.getId());
			surgery03.setPatientName(raven.getName());
			surgery03.setProviderId(rob.getId());
			surgery03.setProviderName(rob.getName());
			surgery03.setDiagnosis("Cancer");
			surgery03.setSurgeryDate(LocalDate.parse("2022-11-05"));
			surgery03.setDischargeInstructions("Drink Water, Ice cream , More solid food");
			surgery03.setId(providerService.addTreatment(surgery03));
			
			RadiologyTreatmentDto radiology03 = treatmentFactory.createRadiologyTreatmentDto();
			radiology03.setPatientId(raven.getId());
			radiology03.setPatientName(raven.getName());
			radiology03.setProviderId(rob.getId());
			radiology03.setProviderName(rob.getName());
			radiology03.setDiagnosis("Cancer Cells ");
			radiology03.setTreatmentDates(Arrays.asList(LocalDate.parse("2022-11-05"),LocalDate.parse("2022-12-05")));
			radiology03.setId(providerService.addTreatment(radiology03));
			
			PhysiotherapyTreatmentDto physiotherapy03 = treatmentFactory.createPhysiotherapyTreatmentDto();
			physiotherapy03.setPatientId(raven.getId());
			physiotherapy03.setPatientName(raven.getName());
			physiotherapy03.setProviderId(rob.getId());
			physiotherapy03.setProviderName(rob.getName());
			physiotherapy03.setDiagnosis("neck pain");
			physiotherapy03.setTreatmentDates(Arrays.asList(LocalDate.parse("2022-11-05"),LocalDate.parse("2022-12-05")));
			physiotherapy03.setFollowupTreatments(Arrays.asList(drug03, surgery03, radiology03));
			physiotherapy03.setId(providerService.addTreatment(physiotherapy03));	
			
			// Now show in the logs what has been added
//Uncomment --- 
			Collection<PatientDto> patients = patientService.getPatients();
			for (PatientDto p : patients) {
				logger.info(String.format("Patient %s, ID %s, DOB %s", p.getName(), p.getId().toString(),
						p.getDob().toString()));
				logTreatments(p.getTreatments());
			}

			Collection<ProviderDto> providers = providerService.getProviders();
			for (ProviderDto p : providers) {
				logger.info(String.format("Provider %s, ID %s, NPI %s", p.getName(), p.getId().toString(), p.getNpi()));
				logTreatments(p.getTreatments());
			}

		} catch (Exception e) {
			;
			throw new IllegalStateException("Failed to add record.", e);

		}
		
	}

	public void shutdown(@Observes @Destroyed(ApplicationScoped.class) ServletContext init) {
		logger.info("App shutting down....");
	}

	private void logTreatments(Collection<TreatmentDto> treatments) {
		for (TreatmentDto treatment : treatments) {
			if (treatment instanceof DrugTreatmentDto) {
				logTreatment((DrugTreatmentDto) treatment);
			} else if (treatment instanceof SurgeryTreatmentDto) {
				logTreatment((SurgeryTreatmentDto) treatment);
			} else if (treatment instanceof RadiologyTreatmentDto) {
				logTreatment((RadiologyTreatmentDto) treatment);
			} else if (treatment instanceof PhysiotherapyTreatmentDto) {
				logTreatment((PhysiotherapyTreatmentDto) treatment);
			}
			if (!treatment.getFollowupTreatments().isEmpty()) {
				logger.info("============= Follow-up Treatments");
				logTreatments(treatment.getFollowupTreatments());
				logger.info("============= End Follow-up Treatments");
			}
		}
	}

	private void logTreatment(DrugTreatmentDto t) {
		logger.info(String.format("...Drug treatment %s, drug %s", t.getId().toString(), t.getDrug()));
	}

	private void logTreatment(RadiologyTreatmentDto t) {
		logger.info(String.format("...Radiology treatment %s", t.getId().toString()));
	}

	private void logTreatment(SurgeryTreatmentDto t) {
		logger.info(String.format("...Surgery treatment %s", t.getId().toString()));
	}

	private void logTreatment(PhysiotherapyTreatmentDto t) {
		logger.info(String.format("...Physiotherapy treatment %s", t.getId().toString()));
	}

}
