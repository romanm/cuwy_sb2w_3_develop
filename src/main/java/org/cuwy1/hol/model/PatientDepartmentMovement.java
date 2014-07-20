package org.cuwy1.hol.model;

import java.sql.Timestamp;

public class PatientDepartmentMovement {
private String departmentName;
private String personalSurname;
private String personalName;
private String personalPatronymic;
private Timestamp departmentHistoryIn;
private Timestamp departmentHistoryOut;
public String getDepartmentName() {
	return departmentName;
}

public void setDepartmentName(String departmentName) {
	this.departmentName = departmentName;
}

public Timestamp getDepartmentHistoryIn() {
	return departmentHistoryIn;
}

public void setDepartmentHistoryIn(Timestamp departmentHistoryIn) {
	this.departmentHistoryIn = departmentHistoryIn;
}

public Timestamp getDepartmentHistoryOut() {
	return departmentHistoryOut;
}

public void setDepartmentHistoryOut(Timestamp departmentHistoryOut) {
	this.departmentHistoryOut = departmentHistoryOut;
}

public String getPersonalPatronymic() {
	return personalPatronymic;
}

public void setPersonalPatronymic(String personalPatronymic) {
	this.personalPatronymic = personalPatronymic;
}

public String getPersonalName() {
	return personalName;
}

public void setPersonalName(String personalName) {
	this.personalName = personalName;
}

public String getPersonalSurname() {
	return personalSurname;
}

public void setPersonalSurname(String personalSurname) {
	this.personalSurname = personalSurname;
}
}
