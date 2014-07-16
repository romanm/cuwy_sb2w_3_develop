package org.cuwy1.hol.model;

import java.util.List;

public class ProcedureBooking {
	private String name, nameLong;
	private List<DrugBooking> drugsBooking;
	private List<ConsumptionMaterialBooking> consumptionMaterialsBooking;

	public List<DrugBooking> getDrugsBooking() {
		return drugsBooking;
	}

	public void setDrugsBooking(List<DrugBooking> drugsBooking) {
		this.drugsBooking = drugsBooking;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLong() {
		return nameLong;
	}

	public void setNameLong(String nameLong) {
		this.nameLong = nameLong;
	}

	public List<ConsumptionMaterialBooking> getConsumptionMaterialsBooking() {
		return consumptionMaterialsBooking;
	}

	public void setConsumptionMaterialsBooking(
			List<ConsumptionMaterialBooking> consumptionMaterialsBooking) {
		this.consumptionMaterialsBooking = consumptionMaterialsBooking;
	}
}
