package org.cuwy1.holDb.model;

public class PatientHolDb {
	private int patientId;
	private boolean patientGender;
	private String patientSurname;
	private String patientPersonalName;
	private String patientPatronymic;
	private String patientStreet;
	private int patientHouse;
	private int patientFlat;

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getPatientSurname() {
		return patientSurname;
	}

	public void setPatientSurname(String patientSurname) {
		this.patientSurname = patientSurname;
	}

	public String getPatientPersonalName() {
		return patientPersonalName;
	}

	public void setPatientPersonalName(String patientPersonalName) {
		this.patientPersonalName = patientPersonalName;
	}

	public String getPatientPatronymic() {
		return patientPatronymic;
	}

	public void setPatientPatronymic(String patientPatronymic) {
		this.patientPatronymic = patientPatronymic;
	}

	public boolean isPatientGender() {
		return patientGender;
	}

	public void setPatientGender(boolean patientGender) {
		this.patientGender = patientGender;
	}

	public String getPatientStreet() {
		return patientStreet;
	}

	public void setPatientStreet(String patientStreet) {
		this.patientStreet = patientStreet;
	}

	public int getPatientHouse() {
		return patientHouse;
	}

	public void setPatientHouse(int patientHouse) {
		this.patientHouse = patientHouse;
	}

	public int getPatientFlat() {
		return patientFlat;
	}

	public void setPatientFlat(int patientFlat) {
		this.patientFlat = patientFlat;
	}
}
