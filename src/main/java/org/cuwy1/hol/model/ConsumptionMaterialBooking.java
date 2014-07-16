package org.cuwy1.hol.model;

public class ConsumptionMaterialBooking extends ConsumptionMaterial implements BookingSetting{
	private int number;
	private String numberUnits;
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

}
