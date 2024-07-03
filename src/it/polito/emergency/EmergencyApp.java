package it.polito.emergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.time.*;
import java.util.stream.*;

public class EmergencyApp {

    public enum PatientStatus {
        ADMITTED,
        DISCHARGED,
        HOSPITALIZED
    }

    public class Department {
        String name;
        int maxPatients, numPatients;
		public Department(String name, int maxPatients) {
			this.name = name;
			this.maxPatients = maxPatients;
            this.numPatients = maxPatients;
		}
        public String getName() {
            return name;
        }
        public int getMaxPatients() {
            return maxPatients;
        }
        public int getNumPatients() {
            return numPatients;
        }
        public void decrementNumPatients() {
            numPatients--;
        }
        public void incementNumPatients() {

            numPatients++;
        }
    }
    
    private final Map<String, Professional> professionals = new HashMap<>();
    private final List<String> specializations = new ArrayList<>();
    private final Map<String, Department> departments = new HashMap<>();
    private final Map<String, Patient> patients = new HashMap<>();
    private final Map<Patient, Professional> assignedPatients = new HashMap<>();
    private final Map<String, Report> reports = new HashMap<>();
    private final Map<Department, Patient> depPatients = new HashMap<>();


    /**
     * Add a professional working in the emergency room
     * 
     * @param id
     * @param name
     * @param surname
     * @param specialization
     * @param period
     * @param workingHours
     */
    public void addProfessional(String id, String name, String surname, String specialization, String period) {
        professionals.put(id, new Professional(id, name, surname, specialization, period));
        specializations.add(specialization);
    }

    /**
     * Retrieves a professional utilizing the ID.
     *
     * @param id The id of the professional.
     * @return A Professional.
     * @throws EmergencyException If no professional is found.
     */    
    public Professional getProfessionalById(String id) throws EmergencyException {
        if (!professionals.containsKey(id))
            throw new EmergencyException("Professional not found!");
        return professionals.get(id);
    }

    /**
     * Retrieves the list of professional IDs by their specialization.
     *
     * @param specialization The specialization to search for among the professionals.
     * @return A list of professional IDs who match the given specialization.
     * @throws EmergencyException If no professionals are found with the specified specialization.
     */    
    public List<String> getProfessionals(String specialization) throws EmergencyException {
        if (!specializations.contains(specialization))
            throw new EmergencyException("No professionals found with given specialization!");
        return professionals.values().stream()
            .filter(p -> p.getSpecialization().equals(specialization))
            .map(Professional::getId)
            .toList();
    }

    /**
     * Retrieves the list of professional IDs who are specialized and available during a given period.
     *
     * @param specialization The specialization to search for among the professionals.
     * @param period The period during which the professional should be available, formatted as "YYYY-MM-DD to YYYY-MM-DD".
     * @return A list of professional IDs who match the given specialization and are available during the period.
     * @throws EmergencyException If no professionals are found with the specified specialization and period.
     */    
    public List<String> getProfessionalsInService(String specialization, String period) throws EmergencyException {
        LocalDate startPeriod = LocalDate.parse(period.split(" to ")[0]);
        LocalDate endPeriod = LocalDate.parse(period.split(" to ")[1]);
        List<String> temp = professionals.values().stream()
                                .filter(p -> p.getSpecialization().equals(specialization))
                                .filter(p -> p.getStart().compareTo(startPeriod) <= 0 && p.getEnd().compareTo(endPeriod) >= 0)
                                .map(Professional::getId)
                                .toList();
        if (temp.isEmpty())
            throw new EmergencyException("No professionals found within given period and specialization!");
        return temp;
    }

    /**
     * Adds a new department to the emergency system if it does not already exist.
     *
     * @param name The name of the department.
     * @param maxPatients The maximum number of patients that the department can handle.
     * @throws EmergencyException If the department already exists.
     */
    public void addDepartment(String name, int maxPatients) {
        departments.put(name, new Department(name, maxPatients));
    }

    /**
     * Retrieves a list of all department names in the emergency system.
     *
     * @return A list containing the names of all registered departments.
     * @throws EmergencyException If no departments are found.
     */
    public List<String> getDepartments() throws EmergencyException {
        if (departments.isEmpty())
            throw new EmergencyException("No registered departments found!");
        return departments.values().stream().map(Department::getName).toList();
    }

    /**
     * Reads professional data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a professional's ID, name, surname, specialization, period of availability, and working hours.
     * The expected format of each line is: matricola, nome, cognome, specializzazione, period, orari_lavoro
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of professionals successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */
    public int readFromFileProfessionals(Reader reader) throws IOException {
        if (reader == null) 
            throw new IOException("Null reader input!");
        List<String> content;
        int count = 0;
        try (BufferedReader in = new BufferedReader(reader)) {
			content = in.lines().collect(Collectors.toList());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
        for (String lines: content) {
            // to skip the header
            if (count == 0) {
                count++;
            } else {
                String[] temp = lines.split(",");
                addProfessional(temp[0].strip(), temp[1].strip(), temp[2].strip(), temp[3].strip(), temp[4].strip());
                count++;
                specializations.add(temp[3].strip());
            }

        }
        return count-1;
    }

    /**
     * Reads department data from a CSV file and stores it in the application.
     * Each line of the CSV should contain a department's name and the maximum number of patients it can accommodate.
     * The expected format of each line is: nome_reparto, num_max
     * 
     * @param reader The reader used to read the CSV file. Must not be null.
     * @return The number of departments successfully read and stored from the file.
     * @throws IOException If there is an error reading from the file or if the reader is null.
     */    
    public int readFromFileDepartments(Reader reader) throws IOException {
        if (reader == null) 
            throw new IOException("Null reader input!");
        List<String> content;
        int count = 0;
        try (BufferedReader in = new BufferedReader(reader)) {
			content = in.lines().collect(Collectors.toList());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
        for (String lines: content) {
            // to skip the header
            if (count == 0) {
                count++;
            } else {
                String[] temp = lines.split(",");
                addDepartment(temp[0].strip(), Integer.valueOf(temp[1].strip()));
                count++;
            }

            
        }
        return count-1;
    }

    /**
     * Registers a new patient in the emergency system if they do not exist.
     * 
     * @param fiscalCode The fiscal code of the patient, used as a unique identifier.
     * @param name The first name of the patient.
     * @param surname The surname of the patient.
     * @param dateOfBirth The birth date of the patient.
     * @param reason The reason for the patient's visit.
     * @param dateTimeAccepted The date and time the patient was accepted into the emergency system.
     */
    public Patient addPatient(String fiscalCode, String name, String surname, String dateOfBirth, String reason, String dateTimeAccepted) {
        patients.put(fiscalCode, new Patient(fiscalCode, name, surname, dateOfBirth, reason, dateTimeAccepted));
        return patients.get(fiscalCode);
    }

    /**
     * Retrieves a patient or patients based on a fiscal code or surname.
     *
     * @param identifier Either the fiscal code or the surname of the patient(s).
     * @return A single patient if a fiscal code is provided, or a list of patients if a surname is provided.
     *         Returns an empty collection if no match is found.
     */    
    public List<Patient> getPatient(String identifier) throws EmergencyException {
        List<Patient> temp = patients.entrySet().stream()
                                .filter(entry -> entry.getKey().equals(identifier) || 
                                        entry.getValue().getSurname().equals(identifier))
                                .map(Map.Entry::getValue)
                                .toList();
        if (temp.isEmpty())
            throw new EmergencyException("No patients found with given identifier!");
        return temp;
    }

    /**
     * Retrieves the fiscal codes of patients accepted on a specific date, 
     * sorted by acceptance time in descending order.
     *
     * @param date The date of acceptance to filter the patients by, expected in the format "yyyy-MM-dd".
     * @return A list of patient fiscal codes who were accepted on the given date, sorted from the most recent.
     *         Returns an empty list if no patients were accepted on that date.
     */
    public List<String> getPatientsByDate(String date) {
        List<String> temp = patients.values().stream()
                                .filter(p -> p.getDateAccepted().equals(LocalDate.parse(date)))
                                .sorted((p1, p2) -> {
                                    if (p1.getSurname().equals(p1.getSurname())) {
                                        return p1.getName().compareTo(p2.getName());
                                    } else {
                                        return p1.getSurname().compareTo(p2.getSurname());
                                    }
                                })
                                .map(Patient::getFiscalCode)
                                .collect(Collectors.toList());
        if (temp.isEmpty())
            return new ArrayList<String>();
        return temp;
    }

    /**
     * Assigns a patient to a professional based on the required specialization and checks availability during the request period.
     *
     * @param fiscalCode The fiscal code of the patient.
     * @param specialization The required specialization of the professional.
     * @return The ID of the assigned professional.
     * @throws EmergencyException If the patient does not exist, if no professionals with the required specialization are found, or if none are available during the period of the request.
     */
    public String assignPatientToProfessional(String fiscalCode, String specialization) throws EmergencyException {
        if (!patients.containsKey(fiscalCode))
            throw new EmergencyException("No patient found with given fiscal code!");
        Patient patient = patients.get(fiscalCode);
        if (!specialization.contains(specialization))
            throw new EmergencyException("No professionals found with given specialization!");
        List<String> temp = professionals.values().stream()
                                .filter(p -> p.getSpecialization().equals(specialization))
                                .filter(p -> p.getStart().compareTo(patient.getDateAccepted()) <= 0 ||
                                            p.getEnd().compareTo(patient.getDateAccepted()) >= 0)
                                .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                                .map(Professional::getId)
                                .collect(Collectors.toList());
        if (temp.isEmpty())
            throw new EmergencyException();
        assignedPatients.put(patient, professionals.get(temp.get(0)));
        return temp.get(0);
    }

    int reportId = 1;
    public Report saveReport(String professionalId, String fiscalCode, String date, String description) throws EmergencyException {
        if (!professionals.containsKey(professionalId))
            throw new EmergencyException();
        reports.put(String.valueOf(reportId), new Report(String.valueOf(reportId), professionalId, fiscalCode, date, description));
        return reports.get(String.valueOf(reportId++));
    }

    /**
     * Either discharges a patient or hospitalizes them depending on the availability of space in the requested department.
     * 
     * @param fiscalCode The fiscal code of the patient to be discharged or hospitalized.
     * @param departmentName The name of the department to which the patient might be admitted.
     * @throws EmergencyException If the patient does not exist or if the department does not exist.
     */
    public void dischargeOrHospitalize(String fiscalCode, String departmentName) throws EmergencyException {
        if (!patients.containsKey(fiscalCode))
            throw new EmergencyException();
        if (!departments.containsKey(departmentName))
            throw new EmergencyException();
        if (departments.get(departmentName).getNumPatients() > 0) {
            patients.get(fiscalCode).setStatus(PatientStatus.HOSPITALIZED);
            departments.get(departmentName).decrementNumPatients();
            depPatients.put(departments.get(departmentName), patients.get(fiscalCode));
        } else {
            patients.get(fiscalCode).setStatus(PatientStatus.DISCHARGED);
        }
    }

    /**
     * Checks if a patient is currently hospitalized in any department.
     *
     * @param fiscalCode The fiscal code of the patient to verify.
     * @return 0 if the patient is currently hospitalized, -1 if not hospitalized or discharged.
     * @throws EmergencyException If no patient is found with the given fiscal code.
     */
    public int verifyPatient(String fiscalCode) throws EmergencyException{
        if (!patients.containsKey(fiscalCode))
            throw new EmergencyException();
        if (patients.get(fiscalCode).getStatus() == PatientStatus.HOSPITALIZED)
            return 0;
        else
            return -1;
    }

    /**
     * Returns the number of patients currently being managed in the emergency room.
     *
     * @return The total number of patients in the system.
     */    
    public int getNumberOfPatients() {
        return (int) patients.values().stream()
                        .filter(p -> p.getStatus() == PatientStatus.ADMITTED)
                        .count();
    }

    /**
     * Returns the number of patients admitted on a specified date.
     *
     * @param dateString The date of interest provided as a String (format "yyyy-MM-dd").
     * @return The count of patients admitted on that date.
     */
    public int getNumberOfPatientsByDate(String date) {
        LocalDate d = LocalDate.parse(date);
        return (int) patients.values().stream()
                        .filter(p -> p.getDateAccepted().equals(d))
                        .count();
    }

    public int getNumberOfPatientsHospitalizedByDepartment(String departmentName) throws EmergencyException {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of patients who have been discharged from the emergency system.
     *
     * @return The count of discharged patients.
     */
    public int getNumberOfPatientsDischarged() {
        //TODO: to be implemented
        return -1;
    }

    /**
     * Returns the number of discharged patients who were treated by professionals of a specific specialization.
     *
     * @param specialization The specialization of the professionals to filter by.
     * @return The count of discharged patients treated by professionals of the given specialization.
     */
    public int getNumberOfPatientsAssignedToProfessionalDischarged(String specialization) {
        //TODO: to be implemented
        return -1;
    }

}
