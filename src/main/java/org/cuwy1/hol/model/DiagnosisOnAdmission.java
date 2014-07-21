package org.cuwy1.hol.model;

import java.sql.Timestamp;

public class DiagnosisOnAdmission {
	private String icdCode;
	private String icdName;
	private Timestamp historyDiagnosDate;
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public String getIcdName() {
		return icdName;
	}
	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
	public Timestamp getHistoryDiagnosDate() {
		return historyDiagnosDate;
	}
	public void setHistoryDiagnosDate(Timestamp historyDiagnosDate) {
		this.historyDiagnosDate = historyDiagnosDate;
	}
}
