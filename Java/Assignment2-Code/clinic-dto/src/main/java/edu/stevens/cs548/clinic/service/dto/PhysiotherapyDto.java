package edu.stevens.cs548.clinic.service.dto;

import java.time.LocalDate;




import com.google.gson.annotations.SerializedName;

public class PhysiotherapyDto extends TreatmentDto {
	
	
	

	

	
	@SerializedName("Treatment_Dates")
	private String[] Treatment_Dates;
	
	

	
	public PhysiotherapyDto() {
		super(TreatmentType.PHYSIOTHERAPY);
	}

	

	
	
	

	
	
	
	public String[] getTreatment_Dates() {
		return Treatment_Dates;
	}

	public void setTreatment_Dates(String Treatment_Dates_l) {
		this.Treatment_Dates = Treatment_Dates_l.split(",");
	
	}
	
	
	
}
