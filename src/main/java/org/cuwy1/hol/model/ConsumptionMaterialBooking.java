package org.cuwy1.hol.model;

import java.util.List;

public class ConsumptionMaterialBooking extends ConsumptionMaterial implements BookingSetting{
	private int number;
	private String numberUnits;
	private List<ConsumptionMaterialBooking> replacement;
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

	public List<ConsumptionMaterialBooking> getReplacement() {
		return replacement;
	}

	public void setReplacement(List<ConsumptionMaterialBooking> replacement) {
		this.replacement = replacement;
	}

}
