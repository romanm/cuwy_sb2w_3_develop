package org.cuwy1.anesthesia;

import org.cuwy1.model1.Patient;

public class NarcoseProtocol {
	private Patient patient;
	private int probaStange;
	private int probaSoobraze;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getProbaStange() {
		return probaStange;
	}

	public void setProbaStange(int probaStange) {
		this.probaStange = probaStange;
	}

	public int getProbaSoobraze() {
		return probaSoobraze;
	}

	public void setProbaSoobraze(int probaSoobraze) {
		this.probaSoobraze = probaSoobraze;
	}
}
