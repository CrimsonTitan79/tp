package medbot.ui;

public class PatientUi extends PersonUi {
    private static final String PATIENT = "patient";

    public static String getPatientCommandList() {
        return getCommandList();
    }

    public static String getListPatientHelpMessage() {
        return getListHelpMessage(PATIENT);
    }

    public static String getViewPatientHelpMessage() {
        return getViewHelpMessage(PATIENT);
    }

    public static String getAddPatientHelpMessage() {
        return getAddHelpMessage(PATIENT);
    }

    public static String getEditPatientHelpMessage() {
        return getEditHelpMessage(PATIENT);
    }

    public static String getFindPatientHelpMessage() {
        return getFindHelpMessage(PATIENT);
    }

    public static String getDeletePatientHelpMessage() {
        return getDeleteHelpMessage(PATIENT);
    }

    public static String getHidePatientHelpMessage() {
        return getHideHelpMessage(PATIENT);
    }

    public static String getShowPatientHelpMessage() {
        return getShowHelpMessage(PATIENT);
    }

    public static String getAddPatientMessage(int id) {
        return getAddPersonMessage(PATIENT, id);
    }

    public static String getDeletePatientMessage(int id) {
        return getDeletePersonMessage(PATIENT, id);
    }

    public static String getEditPatientMessage(int id, String info) {
        return getEditPersonMessage(PATIENT, id, info);
    }

    public static String getHidePatientMessage(int id) {
        return getHidePersonMessage(PATIENT, id);
    }

    public static String getShowPatientMessage(int id) {
        return getShowPersonMessage(PATIENT, id);
    }

    public static String getCurrentViewPatientMessage() {
        return getCurrentViewPersonMessage(PATIENT);
    }

    /**
     * Returns the profile of a patient.
     *
     * @param patientInfo the Info of the patient to be printed.
     * @return the Patient information
     */
    public static String getPatientInfo(String patientInfo) {
        return getPersonInfo(PATIENT, patientInfo);
    }

    /**
     * Returns all patients' information in a list.
     *
     * @param patientListString String containing information of all patients.
     * @return all Patients' information.
     */

    public static String getAllPatientsString(String patientListString) {
        return getAllPersonsString(PATIENT, patientListString);
    }

    /**
     * Returns the header of the patient information table.
     *
     * @return the header of the patient information table.
     */
    public static String getPatientTableHeader() {
        return getTableHeader(PATIENT);
    }

}
