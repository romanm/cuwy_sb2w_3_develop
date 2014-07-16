package org.cuwy1.icd10;

import java.util.List;

public class Icd10Class {
	private String text;
	private String code;
	private String subCode;
	private String kind;
	private List<Icd10Class> icd10Classes;

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

	public List<Icd10Class> getIcd10Classes() {
		return icd10Classes;
	}

	public void setIcd10Classes(List<Icd10Class> icd10Classes) {
		this.icd10Classes = icd10Classes;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
}
