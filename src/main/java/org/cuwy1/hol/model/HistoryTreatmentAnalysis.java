package org.cuwy1.hol.model;

import java.sql.Timestamp;

public class HistoryTreatmentAnalysis {
private String historyTreatmentAnalysisText;
private Timestamp historyTreatmentAnalysisDatetime;
public String getHistoryTreatmentAnalysisText() {
	return historyTreatmentAnalysisText;
}

public void setHistoryTreatmentAnalysisText(String historyTreatmentAnalysisText) {
	this.historyTreatmentAnalysisText = historyTreatmentAnalysisText;
}

public Timestamp getHistoryTreatmentAnalysisDatetime() {
	return historyTreatmentAnalysisDatetime;
}

public void setHistoryTreatmentAnalysisDatetime(
		Timestamp historyTreatmentAnalysisDatetime) {
	this.historyTreatmentAnalysisDatetime = historyTreatmentAnalysisDatetime;
}
}
