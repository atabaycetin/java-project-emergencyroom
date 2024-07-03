package it.polito.emergency;

public class Report {
    String reportId, professionalId, fiscalCode, date, description;
    
    public Report(String reportId, String professionalId, String fiscalCode, String date, String description) {
		this.reportId = reportId;
        this.professionalId = professionalId;
		this.fiscalCode = fiscalCode;
		this.date = date;
		this.description = description;
	}

	public String getId() {
        return reportId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
