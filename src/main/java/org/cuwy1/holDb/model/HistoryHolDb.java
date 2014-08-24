package org.cuwy1.holDb.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.cuwy1.hol.model.DiagnosisOnAdmission;
import org.cuwy1.hol.model.HistoryTreatmentAnalysis;
import org.cuwy1.hol.model.PatientDepartmentMovement;

public class HistoryHolDb {
	private boolean perevid = false;
	private PatientHolDb patientHolDb;
	private int historyId,historyNo, patientId;
	private Timestamp historyIn;
	private List<PatientDepartmentMovement> patientDepartmentMovements;
	private List<HistoryTreatmentAnalysis> historyTreatmentAnalysises;
	private DiagnosisOnAdmission diagnosisOnAdmission;

	public int getHistoryId() {
		return historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public int getHistoryNo() {
		return historyNo;
	}

	public void setHistoryNo(int historyNo) {
		this.historyNo = historyNo;
	}

	public List<PatientDepartmentMovement> getPatientDepartmentMovements() {
		return patientDepartmentMovements;
	}

	public void setPatientDepartmentMovements(List<PatientDepartmentMovement> patientDepartmentMovements) {
		this.patientDepartmentMovements = patientDepartmentMovements;
	}

	public List<HistoryTreatmentAnalysis> getHistoryTreatmentAnalysises() {
		return historyTreatmentAnalysises;
	}

	public void setHistoryTreatmentAnalysises(List<HistoryTreatmentAnalysis> historyTreatmentAnalysises) {
		this.historyTreatmentAnalysises = historyTreatmentAnalysises;
	}

	public DiagnosisOnAdmission getDiagnosisOnAdmission() {
		return diagnosisOnAdmission;
	}

	public void setDiagnosisOnAdmission(DiagnosisOnAdmission diagnosisOnAdmission) {
		this.diagnosisOnAdmission = diagnosisOnAdmission;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public PatientHolDb getPatientHolDb() {
		return patientHolDb;
	}

	public void setPatientHolDb(PatientHolDb patientHolDb) {
		this.patientHolDb = patientHolDb;
	}

	public Timestamp getHistoryIn() {
		return historyIn;
	}

	public void setHistoryIn(Timestamp historyIn) {
		this.historyIn = historyIn;
	}

	public boolean isPerevid() {
		return perevid;
	}

	public void setPerevid(boolean perevid) {
		this.perevid = perevid;
	}

}
