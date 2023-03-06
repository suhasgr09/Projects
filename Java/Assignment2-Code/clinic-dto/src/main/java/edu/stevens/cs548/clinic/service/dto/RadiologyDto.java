package edu.stevens.cs548.clinic.service.dto;

import java.time.LocalDate;




import com.google.gson.annotations.SerializedName;

public class RadiologyDto extends TreatmentDto {
	
	
	

	

	
	@SerializedName("follow_up_treatment")
	private String[] follow_up_treatment;
	
	
	@SerializedName("Treatment_Dates")
	private String[] follow_up_dates;
	
	

	
	public RadiologyDto() {
		super(TreatmentType.RADIOLOGY);
	}

	

	
	
	
	public String[] getfollow_up_treatment() {
		return follow_up_treatment;
	}

	public void setfollow_up_treatment(String follow_up_treatment_L) {
		this.follow_up_treatment = follow_up_treatment_L.split(",");
	}
	
	
	
	public String[] getfollow_up_dates() {
		return follow_up_dates;
	}

	public void setfollow_up_dates(String follow_up_dates_l) {
		this.follow_up_dates = follow_up_dates_l.split(",");
	
	}
	
	
	
}
