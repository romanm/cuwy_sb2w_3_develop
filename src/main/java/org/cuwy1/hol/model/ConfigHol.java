package org.cuwy1.hol.model;

import java.util.List;
import java.util.Map;

public class ConfigHol {
	private List<CountryHol> countries;
	private List<DepartmentHol> departments;
	private List<Map<String, Object>> directs;
	private List<Map<String, Object>> treatmentAnalysis;
	
	public List<CountryHol> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryHol> countries) {
		this.countries = countries;
	}

	public List<DepartmentHol> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentHol> departments) {
		this.departments = departments;
	}

	public List<Map<String, Object>> getDirects() {
		return directs;
	}

	public void setDirects(List<Map<String, Object>> directs) {
		this.directs = directs;
	}

	public List<Map<String, Object>> getTreatmentAnalysis() {
		return treatmentAnalysis;
	}

	public void setTreatmentAnalysis(List<Map<String, Object>> treatmentAnalysis) {
		this.treatmentAnalysis = treatmentAnalysis;
	}

}
