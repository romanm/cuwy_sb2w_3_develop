package org.cuwy1.icd10;

import java.util.List;

public class Icd10UaClass {
	private String text;
	private String code;
	private String kind;
	private List<Icd10UaClass> icd10Classes;
	private long icd_id, icd_start, icd_end;

	@Override
	public String toString() {
		return String.format(
				"Icd10UaClass[icd_id=%d, icd_start=%d, icd_end=%d, firstName='%s', lastName='%s']",
				icd_id, icd_start, icd_end, code, text);
	}
	
	public Icd10UaClass(long icd_id,long  icd_start,long  icd_end, String code, String text) {
		this.setIcd_id(icd_id);
		this.code = code;
		this.text = text;
	}

	public Icd10UaClass() {
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Icd10UaClass> getIcd10Classes() {
		return icd10Classes;
	}

	public void setIcd10Classes(List<Icd10UaClass> icd10Classes) {
		this.icd10Classes = icd10Classes;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getIcd_id() {
		return icd_id;
	}

	public void setIcd_id(long icd_id) {
		this.icd_id = icd_id;
	}

	public long getIcd_end() {
		return icd_end;
	}

	public void setIcd_end(long icd_end) {
		this.icd_end = icd_end;
	}

	public long getIcd_start() {
		return icd_start;
	}

	public void setIcd_start(long icd_start) {
		this.icd_start = icd_start;
	}
}
