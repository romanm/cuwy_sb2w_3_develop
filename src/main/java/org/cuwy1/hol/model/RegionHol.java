package org.cuwy1.hol.model;

public class RegionHol {
	private int regionId;
	private int districtId;
	private String regionName;
	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int destrictId) {
		this.districtId = destrictId;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
}
