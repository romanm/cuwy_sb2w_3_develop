package org.cuwy1.hol.model;

import java.sql.Date;

public class PatientDiagnosisHol {
	private String name, icd_name,icd_code;
	private int patient_id;
	private int diagnos_id;
	private Date history_in;

	public PatientDiagnosisHol(String name, Date history_in,
			String icd_code, String icd_name, int patient_id, short diagnos_id) {
		this.name = name;
		this.history_in = history_in;
		this.icd_code = icd_code;
		this.icd_name = icd_name;
		this.patient_id = patient_id;
		this.diagnos_id = diagnos_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcd_name() {
		return icd_name;
	}

	public void setIcd_name(String icd_name) {
		this.icd_name = icd_name;
	}

	public String getIcd_code() {
		return icd_code;
	}

	public void setIcd_code(String icd_code) {
		this.icd_code = icd_code;
	}

	public int getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}


	public Date getHistory_in() {
		return history_in;
	}

	public void setHistory_in(Date history_in) {
		this.history_in = history_in;
	}

	public int getDiagnos_id() {
		return diagnos_id;
	}

	public void setDiagnos_id(int diagnos_id) {
		this.diagnos_id = diagnos_id;
	}

}
