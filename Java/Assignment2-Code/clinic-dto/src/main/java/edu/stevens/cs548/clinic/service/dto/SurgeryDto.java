package edu.stevens.cs548.clinic.service.dto;

import java.time.LocalDate;

import java.util.UUID;
import com.google.gson.annotations.SerializedName;

public class SurgeryDto extends TreatmentDto {
	
	
	

	
	@SerializedName("date_of_surgrey")
	private LocalDate dateofsurgrey;
	
	@SerializedName("discharge_instruction")
	private String[] discharge_instruction;
	
	@SerializedName("follow_up_treatment")
	private String[] follow_up_treatment;

	
	public SurgeryDto() {
		super(TreatmentType.SURGERY);
	}

	public String[] getdischarge_instruction() {
		return discharge_instruction;
	}

	public void setdischarge_instruction(String discharge_instruction_l) {
		this.discharge_instruction = discharge_instruction_l.split(",");
	}

	
	public LocalDate getdateofsurgrey() {
		return dateofsurgrey;
	}

	public void setdateofsurgrey(LocalDate dateofsurgrey) {
		this.dateofsurgrey = dateofsurgrey;
	}

	public String[] getfollow_up_treatment() {
		return follow_up_treatment;
	}

	public void setfollow_up_treatment(String follow_up_treatment_list) {
		this.follow_up_treatment = follow_up_treatment_list.split(",");
	}


	
}
