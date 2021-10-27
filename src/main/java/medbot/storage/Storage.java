package medbot.storage;

import medbot.Appointment;
import medbot.Scheduler;
import medbot.exceptions.MedBotException;
import medbot.list.ListItem;
import medbot.list.ListItemType;
import medbot.person.Patient;
import medbot.person.Staff;
import medbot.utilities.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.max;
import static medbot.ui.Ui.VERTICAL_LINE_SPACED_ESCAPED;

public abstract class Storage {
    protected static final String ERROR_LOAD_STORAGE = "Error: Unable to load data file.";
    protected static final String ERROR_SAVE_STORAGE = "Error: Unable to save data.";

    protected File dataFile;
    protected String dataPath;

    /**
     * Generic Constructor with creates a storage file if it doesn't already exist.
     *
     * @param dataPathString String that is the path of the storage file
     * @throws MedBotException if storage file cannot be created and does not exist
     */
    public Storage(String dataPathString) throws MedBotException {
        try {
            dataPath = dataPathString;
            dataFile = new File(dataPath);
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();

        } catch (IOException e) {
            throw new MedBotException(ERROR_LOAD_STORAGE);
        }
    }


    /**
     * Reads in storage file, parses each line and adds the data into the program
     * returns all line numbers of storage file that are invalid.
     *
     * @param listItemType enum of ListItem type
     * @return Error message if there are formatting errors in storage file
     * @throws FileNotFoundException if storage file cannot be found
     */
    public String loadStorage(ListItemType listItemType, Scheduler scheduler) throws FileNotFoundException {
        int lineNumber = 1;
        Scanner s = new Scanner(dataFile);
        String loadStorageErrorMessage = "";

        while (s.hasNext()) {
            try {
                String storageLine = s.nextLine();
                addListItemFromStorageLine(listItemType, scheduler, storageLine);

            } catch (Exception e) {
                loadStorageErrorMessage += loadStorageLineErrorMessage(lineNumber);
            }
            lineNumber++;
        }

        return loadStorageErrorMessage;
    }


    /**
     * Add a ListItem object to the corresponding list,
     * from the text in a storage line in a storage file.
     *
     * @param listItemType enum of ListItem type
     * @param storageLine  a line in storage file
     * @throws MedBotException if fail to add a ListItem to the list
     */
    protected void addListItemFromStorageLine(ListItemType listItemType, Scheduler scheduler, String storageLine)
            throws MedBotException {
        ListItem listItem = createListItem(storageLine, listItemType);

        switch (listItemType) {
        case PATIENT:
            scheduler.addPatient((Patient) listItem);
            int lastPatientId = max(listItem.getId(), scheduler.getLastPatientId());
            scheduler.setLastPatientId(lastPatientId);
            break;
        case STAFF:
            scheduler.addStaff((Staff) listItem);
            int lastStaffId = max(listItem.getId(), scheduler.getLastStaffId());
            scheduler.setLastStaffId(lastStaffId);
            break;
        case APPOINTMENT:
            scheduler.addAppointment((Appointment) listItem);
            int lastAppointmentId = max(listItem.getId(), scheduler.getLastAppointmentId());
            scheduler.setLastAppointmentId(lastAppointmentId);
            break;
        default:
            throw new MedBotException("Not a list item");

        }
    }

    /**
     * Writes the storageString to storage file.
     *
     * @param storageString String containing the data of the list.
     * @throws MedBotException if unable to write to storage text file.
     */
    public void saveData(String storageString) throws MedBotException {
        try {
            FileWriter fw = new FileWriter(dataPath);
            fw.write(storageString);
            fw.close();
        } catch (IOException e) {
            throw new MedBotException(ERROR_SAVE_STORAGE);
        }
    }


    /**
     * Parse a line from the storage file by splitting its constituent parts.
     *
     * @param storageLine a line from the storage file
     * @return listItem details, consisting of person ID and other parameters
     */
    protected Pair<Integer, ArrayList<String>> parseStorageLine(String storageLine, String[] parameterPrefixes) {
        if (storageLine.isBlank()) {
            return null;
        }

        String[] listItemParameters = splitStorageLine(storageLine);
        ArrayList<String> prefixPlusListItemParameters = new ArrayList<>();
        Integer listItemId = Integer.parseInt(listItemParameters[0]);

        for (int i = 0; i < parameterPrefixes.length; i++) {
            // i + 1, since listItemParameters[0] is the listItemId
            if (isStorageParameterNull(listItemParameters[i + 1])) {
                continue;
            }
            // i + 1, since listItemParameters[0] is the listItemId
            String prefixPlusListItemParameter = parameterPrefixes[i] + listItemParameters[i + 1];
            prefixPlusListItemParameters.add(prefixPlusListItemParameter);
        }
        assert listItemParameters.length == parameterPrefixes.length + 1;

        return new Pair<>(listItemId, prefixPlusListItemParameters);
    }

    /**
     * String split a line with " | " as the delimiters.
     *
     * @param storageLine a line in storage file
     * @return String[] with the parameters separated in different indexes in the array
     */
    protected static String[] splitStorageLine(String storageLine) {
        return storageLine.split(VERTICAL_LINE_SPACED_ESCAPED);
    }

    /**
     * True if "X", which means the parameter is null, false otherwise.
     *
     * @param parameter a parameter in a line of storage/data.txt
     * @return true if "X", which means the parameter is null, false otherwise
     */
    protected static boolean isStorageParameterNull(String parameter) {
        return parameter.equals("X");
    }

    /**
     * Error message that shows the line number of a line in storage file with improper formatting.
     *
     * @param lineNumber the line number with improper formatting in storage/data.txt
     * @return Error message
     */
    protected String loadStorageLineErrorMessage(int lineNumber) {
        return "Error: Line " + lineNumber + " of " + dataPath
                + " is invalid!\n";
    }

    /**
     * Template Method. Instantiates a ListItem interfaced object.
     *
     * @param storageLine  a line in storage file
     * @param listItemType enum of ListItem type
     * @return a ListItem interfaced object
     * @throws MedBotException if a ListItem object fails to be created
     */
    protected abstract ListItem createListItem(String storageLine, ListItemType listItemType) throws MedBotException;
}

