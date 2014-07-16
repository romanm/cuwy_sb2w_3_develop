package org.cuwy1.hol.model;

import java.util.List;

public class DrugBooking extends Drug implements BookingSetting{
	private int doseConcentrationNumber;
	private String doseConcentrationUnit;
	private int doseNumber;
	private String doseUnit;
	private int number;
	private String numberUnits;
	private List<DrugBooking> replacement;
	
	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String getNumberUnits() {
		return numberUnits;
	}

	@Override
	public void setNumberUnits(String numberUnits) {
		this.numberUnits = numberUnits;
	}

	public int getDoseConcentrationNumber() {
		return doseConcentrationNumber;
	}

	public void setDoseConcentrationNumber(int doseConcentrationNumber) {
		this.doseConcentrationNumber = doseConcentrationNumber;
	}

	public String getDoseConcentrationUnit() {
		return doseConcentrationUnit;
	}

	public void setDoseConcentrationUnit(String doseConcentrationUnit) {
		this.doseConcentrationUnit = doseConcentrationUnit;
	}

	public int getDoseNumber() {
		return doseNumber;
	}

	public void setDoseNumber(int doseNumber) {
		this.doseNumber = doseNumber;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public List<DrugBooking> getReplacement() {
		return replacement;
	}

	public void setReplacement(List<DrugBooking> replacement) {
		this.replacement = replacement;
	}

}
