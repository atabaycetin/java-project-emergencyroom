package it.polito.emergency;

import java.time.*;

public class Professional {

    String id, name, surname, specialization, period;
    LocalDate start, end;
    public Professional (String id, String name, String surname, String specialization, String period) {
        String[] temp = period.split(" to ");
        start = LocalDate.parse(temp[0]);
        end = LocalDate.parse(temp[1]);
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
        this.period = period;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPeriod() {
        return period;
    }

    public String getWorkingHours() {
        return "24/7";
    }
	public LocalDate getStart() {
		return start;
	}
	public LocalDate getEnd() {
		return end;
	}
}
