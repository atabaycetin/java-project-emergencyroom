package it.polito.emergency;

import it.polito.emergency.EmergencyApp.*;
import java.time.*;


public class Patient {

    String fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted;
    LocalDate birthDate;
    LocalDate dateAccepted;
    PatientStatus status;
    public Patient(String fiscalCode, String name, String surname, String dateOfBirth, String reason,
            String dateTimeAccepted) {
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.birthDate = LocalDate.parse(dateOfBirth);
        this.reason = reason;
        this.dateTimeAccepted = dateTimeAccepted;
        this.dateAccepted = LocalDate.parse(dateTimeAccepted);
        this.status = PatientStatus.ADMITTED;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getReason() {
        return reason;
    }

    public String getDateTimeAccepted() {
        return dateTimeAccepted;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus stat) {
        this.status = stat;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDateAccepted() {
        return dateAccepted;
    }
    
}
