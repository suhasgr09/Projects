package edu.stevens.cs548.clinic.service.impl;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.IPatientDao;
import edu.stevens.cs548.clinic.domain.IPatientDao.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDao;
import edu.stevens.cs548.clinic.domain.IProviderDao.ProviderExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDao.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentFactory;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PhysiotherapyTreatment;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.SurgeryTreatment;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PhysiotherapyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentDto;

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * CDI Bean implementation class ProviderService
 */
// TODO
@RequestScoped
public class ProviderService implements IProviderService {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderService.class.getCanonicalName());

	private IProviderFactory providerFactory;

	private ITreatmentFactory treatmentFactory;

	private ProviderDtoFactory providerDtoFactory;
	

	public ProviderService() {
		// Initialize factories
		providerFactory = new ProviderFactory();
		providerDtoFactory = new ProviderDtoFactory();
		treatmentFactory = new TreatmentFactory();
	}
	
	// TODO
	@Inject
	private IProviderDao providerDao;

	// TODO
	@Inject
	private IPatientDao patientDao;


	/**
	 * @see IProviderService#addProvider(ProviderDto dto)
	 */
	@Override
	public UUID addProvider(ProviderDto dto) throws ProviderServiceExn {
		// Use factory to create Provider entity, and persist with DAO
		try {
			Provider provider = providerFactory.createProvider();
			provider.setProviderId(UUID.randomUUID());
			provider.setName(dto.getName());
			provider.setNpi(dto.getNpi());
			providerDao.addProvider(provider);
			return provider.getProviderId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn("Failed to add provider", e);
		}
	}
	
	public List<ProviderDto> getProviders() throws ProviderServiceExn {
		List<ProviderDto> dtos = new ArrayList<ProviderDto>();
		Collection<Provider> providers = providerDao.getProviders();
		for (Provider provider : providers) {
			try {
				dtos.add(providerToDto(provider, false));
			} catch (TreatmentExn e) {
				throw new ProviderServiceExn("Failed to export treatment", e);
			}
		}
		return dtos;
	}

	/**
	 * @see IProviderService#getProvider(UUID)
	 */
	@Override
	public ProviderDto getProvider(UUID id) throws ProviderServiceExn {
		// TODO use DAO to get Provider by external key
		//return null;
		
		Provider p = null;
		ProviderDto dto = null;
		try {
			 p = providerDao.getProvider(id);
			 dto = providerToDto(p, true);
		
			
			
		}
		catch (ProviderExn e)
		{
		  throw new ProviderServiceExn("Failed to get Patient ID",e);
		 
		}
		catch (TreatmentExn e) {
			throw new ProviderServiceExn("Failed to export treatment", e);
		}
		return dto;
	}
	
	private ProviderDto providerToDto(Provider provider, boolean includeTreatments) throws TreatmentExn {
		ProviderDto dto = providerDtoFactory.createProviderDto();
		dto.setId(provider.getProviderId());
		dto.setName(provider.getName());
		dto.setNpi(provider.getNpi());
		if (includeTreatments) {
			dto.setTreatments(provider.exportTreatments(TreatmentExporter.exportWithoutFollowups()));
		}
		return dto;
	}

	@Override
	public UUID addTreatment(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn {
		return addTreatmentImpl(dto).getTreatmentId();
	}
	
	private void addFollowupTreatments(Collection<TreatmentDto> treatmentDtos, Treatment treatment) throws PatientServiceExn, ProviderServiceExn {
		for (TreatmentDto treatmentDto : treatmentDtos) {
			
			treatment.addFollowupTreatment(addTreatmentImpl(treatmentDto));
		}
	}

	
	
	private Treatment addTreatmentImpl(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn {
		try {
			Provider provider = providerDao.getProvider(dto.getProviderId());
			Patient patient = patientDao.getPatient(dto.getPatientId());
			if (dto instanceof DrugTreatmentDto) {
				DrugTreatmentDto drugTreatmentDto = (DrugTreatmentDto) dto;
				DrugTreatment drugTreatment = treatmentFactory.createDrugTreatment();
				drugTreatment.setTreatmentId(UUID.randomUUID());
				drugTreatment.setDiagnosis(drugTreatmentDto.getDiagnosis());
				drugTreatment.setFrequency(drugTreatmentDto.getFrequency());
				drugTreatment.setDrug(drugTreatmentDto.getDrug());
				drugTreatment.setDosage(drugTreatmentDto.getDosage());
				drugTreatment.setStartDate(drugTreatmentDto.getStartDate());
				drugTreatment.setEndDate(drugTreatmentDto.getEndDate());
				// TODO fill in the rest of the fields
				
				addFollowupTreatments(dto.getFollowupTreatments(), drugTreatment);
				drugTreatment.setProvider(patient, provider);
				return drugTreatment;
			}
			else if  (dto instanceof SurgeryTreatmentDto){
				SurgeryTreatmentDto surgeryTreatmentDto = (SurgeryTreatmentDto) dto;
				SurgeryTreatment surgery_treatment = treatmentFactory.createSurgeryTreatment();
				
				surgery_treatment.setTreatmentId(UUID.randomUUID());
				surgery_treatment.setDiagnosis(surgeryTreatmentDto.getDiagnosis());
				surgery_treatment.setDischargeInstructions(surgeryTreatmentDto.getDischargeInstructions());
				surgery_treatment.setSurgeryDate(surgeryTreatmentDto.getSurgeryDate());
				addFollowupTreatments(dto.getFollowupTreatments(), surgery_treatment);
				surgery_treatment.setProvider(patient, provider);
				
				return surgery_treatment;
				
			}
			else if  (dto instanceof RadiologyTreatmentDto)
			{
				RadiologyTreatmentDto radiologyTreatmentDto = (RadiologyTreatmentDto) dto;
				RadiologyTreatment radiologyTreatment = treatmentFactory.createRadiologyTreatment();
				
				radiologyTreatment.setTreatmentId(UUID.randomUUID());
				radiologyTreatment.setDiagnosis(radiologyTreatmentDto.getDiagnosis());
				for (LocalDate fd : radiologyTreatmentDto.getTreatmentDates())
				{
					radiologyTreatment.addTreatmentDate(fd);
				}
				//radiologyTreatment.addTreatmentDate(radiologyTreatmentDto.getTreatmentDates().get(0));
				addFollowupTreatments(dto.getFollowupTreatments(), radiologyTreatment);
				//radiologyTreatment.addTreatmentDate(radiologyTreatmentDto.getTreatmentDates());
				
				return radiologyTreatment;
			}
			else if  (dto instanceof PhysiotherapyTreatmentDto)
			{
				PhysiotherapyTreatmentDto physiotherapyTreatmentDto = (PhysiotherapyTreatmentDto) dto;
				PhysiotherapyTreatment physiotherapyTreatment = treatmentFactory.createPhysiotherapyTreatment();
				
				physiotherapyTreatment.setTreatmentId(UUID.randomUUID());
				physiotherapyTreatment.setDiagnosis(physiotherapyTreatmentDto.getDiagnosis());
				for (LocalDate fd : physiotherapyTreatmentDto.getTreatmentDates())
				{
					physiotherapyTreatment.addTreatmentDate(fd);
				}
				//physiotherapyTreatment.addTreatmentDate(physiotherapyTreatmentDto.getTreatmentDates().get(0));
				addFollowupTreatments(dto.getFollowupTreatments(), physiotherapyTreatment);
				//radiologyTreatment.addTreatmentDate(radiologyTreatmentDto.getTreatmentDates());
				
				return physiotherapyTreatment;
			}
			else {
				/*
				 * TODO Handle the other cases
				 */

				throw new IllegalArgumentException("No treatment-specific info provided.");
			}

		} catch (PatientExn e) {
			throw new PatientNotFoundExn("Could not find patient for "+dto.getPatientId(), e);
		
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn("Could not find provider for "+dto.getProviderId(), e);
		}
	}
	
	@Override
	public TreatmentDto getTreatment(UUID providerId, UUID tid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn {
		// Export treatment DTO from Provider aggregate
		try {
			Provider provider = providerDao.getProvider(providerId);
			TreatmentDto treatment = provider.exportTreatment(tid, TreatmentExporter.exportWithoutFollowups());
			//return provider.exportTreatment(tid, TreatmentExporter.exportWithFollowups());
            return treatment;
		} catch (TreatmentExn e) {
			throw new TreatmentNotFoundExn("Could not find treatment for "+tid, e);
		
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn("Could not find provider for "+providerId, e);
		}
	}
	
	@Override
	public void removeAll() throws ProviderServiceExn {
		providerDao.deleteProviders();
	}
//	@Override
//	public UUID addTreatment(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn {
//		try {
//			Provider provider = providerDao.getProvider(dto.getProviderId());
//			Patient patient = patientDao.getPatient(dto.getPatientId());
//			if (dto instanceof DrugTreatmentDto) {
//				DrugTreatmentDto drugTreatmentDto = (DrugTreatmentDto) dto;
//				DrugTreatment drugTreatment = treatmentFactory.createDrugTreatment();
//				drugTreatment.setTreatmentId(UUID.randomUUID());
//				drugTreatment.setDiagnosis(drugTreatmentDto.getDiagnosis());
//				drugTreatment.setDrug(drugTreatmentDto.getDrug());
//				drugTreatment.setDosage(drugTreatmentDto.getDosage());
//				drugTreatment.setStartDate(drugTreatmentDto.getStartDate());
//				drugTreatment.setEndDate(drugTreatmentDto.getEndDate());
//				drugTreatment.setFrequency(drugTreatmentDto.getFrequency());
//				//drugTreatment.setFollowupTreatment(drugTreatmentDto.getFollowupTreatments());
//				
//				
//				// TODO fill in the rest of the fields
//				
//				drugTreatment.setProvider(patient, provider);
//				return drugTreatment.getTreatmentId();
//			}
//			else if  (dto instanceof SurgeryTreatmentDto){
//				SurgeryTreatmentDto surgeryTreatmentDto = (SurgeryTreatmentDto) dto;
//				SurgeryTreatment surgery_treatment = treatmentFactory.createSurgeryTreatment();
//				
//				surgery_treatment.setTreatmentId(UUID.randomUUID());
//				surgery_treatment.setDiagnosis(surgeryTreatmentDto.getDiagnosis());
//				surgery_treatment.setDischargeInstructions(surgeryTreatmentDto.getDischargeInstructions());
//				surgery_treatment.setSurgeryDate(surgeryTreatmentDto.getSurgeryDate());
//				surgery_treatment.setProvider(patient, provider);
//				return surgery_treatment.getTreatmentId();
//				
//			}
//			else if  (dto instanceof RadiologyTreatmentDto)
//			{
//				RadiologyTreatmentDto radiologyTreatmentDto = (RadiologyTreatmentDto) dto;
//				RadiologyTreatment radiologyTreatment = treatmentFactory.createRadiologyTreatment();
//				
//				radiologyTreatment.setTreatmentId(UUID.randomUUID());
//				radiologyTreatment.setDiagnosis(radiologyTreatmentDto.getDiagnosis());
//				//radiologyTreatment.addTreatmentDate(radiologyTreatmentDto.getTreatmentDates());
//				
//				return radiologyTreatment.getTreatmentId();
//			}
//			else if  (dto instanceof PhysiotherapyTreatmentDto)
//			{
//				PhysiotherapyTreatmentDto physiotherapyTreatmentDto = (PhysiotherapyTreatmentDto) dto;
//				PhysiotherapyTreatment radiologyTreatment = treatmentFactory.createPhysiotherapyTreatment();
//				
//				radiologyTreatment.setTreatmentId(UUID.randomUUID());
//				radiologyTreatment.setDiagnosis(physiotherapyTreatmentDto.getDiagnosis());
//				//radiologyTreatment.addTreatmentDate(radiologyTreatmentDto.getTreatmentDates());
//				
//				return radiologyTreatment.getTreatmentId();
//			}
//			else {
//				/*
//				 * TODO Handle the other cases
//				 */
//
//				throw new IllegalArgumentException("No treatment-specific info provided.");
//			}
//
//		} catch (PatientExn e) {
//			throw new PatientNotFoundExn("Could not find patient for "+dto.getPatientId(), e);
//		
//		} catch (ProviderExn e) {
//			throw new ProviderNotFoundExn("Could not find provider for "+dto.getProviderId(), e);
//		}
//	}
}
