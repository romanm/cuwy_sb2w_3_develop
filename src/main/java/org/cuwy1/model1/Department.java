package org.cuwy1.model1;

import java.util.List;

public class Department {
	private String name;
	private List<PatientDiagnosis> patientesDiagnoses;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PatientDiagnosis> getPatientesDiagnoses() {
		return patientesDiagnoses;
	}
	public void setPatientesDiagnoses(List<PatientDiagnosis> patientesDiagnoses) {
		this.patientesDiagnoses = patientesDiagnoses;
	}
}
