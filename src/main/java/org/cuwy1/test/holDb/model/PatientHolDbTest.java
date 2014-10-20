package org.cuwy1.test.holDb.model;

import java.sql.Timestamp;
import java.util.Calendar;

import org.cuwy1.holDb.model.PatientHolDb;

public class PatientHolDbTest {
	
public static void main2(String[] args) {
	PatientHolDb patientHolDb = new PatientHolDb();
	Calendar patientDob = Calendar.getInstance();
	patientDob.set(2014, 8, 1);
	Timestamp timestamp = new Timestamp(patientDob.getTimeInMillis());
	patientHolDb.setPatientDob(timestamp);
	System.out.println(Calendar.getInstance().getTime());
	System.out.println(patientDob.getTime());
	System.out.println(patientHolDb.patientAge());
	System.out.println(patientHolDb.patientMonth());
	System.out.println(patientHolDb.patientDay());
}
}
