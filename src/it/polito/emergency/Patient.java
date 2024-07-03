package it.polito.emergency;

import it.polito.emergency.EmergencyApp.*;
import java.time.*;

public class Patient {
    String fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted;
    LocalDate birthDate;
    LocalDateTime dateAccepted;

    public Patient(String fiscalCode, String name, String surname, String dateOfBirth, String reason,
            String dateTimeAccepted) {
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.birthDate = LocalDate.parse(dateOfBirth);
        this.reason = reason;
        this.dateTimeAccepted = dateTimeAccepted;
        this.dateAccepted = LocalDateTime.parse(dateTimeAccepted);
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
        return null;
    }

    public String getDateTimeAccepted() {
        return null;
    }

    public PatientStatus getStatus() {
        return null;
    }
    
}
