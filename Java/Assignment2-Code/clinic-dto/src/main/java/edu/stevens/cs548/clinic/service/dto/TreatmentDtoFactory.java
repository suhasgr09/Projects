package edu.stevens.cs548.clinic.service.dto;

public class TreatmentDtoFactory {
	
	public DrugTreatmentDto createDrugTreatmentDto () {
		return new DrugTreatmentDto();
	}
	
	
	public SurgeryDto createSurgeryDto () {
		return new SurgeryDto();
	}
	
	public RadiologyDto createRadiologyDto () {
		return new RadiologyDto();
	}
	
	public PhysiotherapyDto createPhysiotherapyDto () {
		return new PhysiotherapyDto();
	}
	
	/*
	 * TODO: Repeat for other treatments.
	 */

}
