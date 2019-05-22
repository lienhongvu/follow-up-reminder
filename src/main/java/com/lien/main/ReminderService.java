package com.lien.main;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReminderService implements Runnable {

    private Logger LOGGER = Logger.getLogger(ReminderService.class.getName());

    @Override
    public void run() {
        try {
            LOGGER.log(Level.INFO, "ReminderService: START");
            process();
            LOGGER.log(Level.INFO, "ReminderService: END");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "ReminderService: Error when processing data");
        }
    }

    private void process() throws ParseException, IOException {
        AppInfo appInfo = Utilities.getAppInfoFromFile();
        if (LocalDateTime.now().getHour() < 7) {
            LOGGER.log(Level.INFO, "ReminderService: Dont remind too early");
        } else if (LocalDate.now().toString().equals(appInfo.getLastNotify()) && appInfo.getStatus().equals("done")) {
            LOGGER.log(Level.INFO, "ReminderService: Finished reminding for today");
        } else if (!appInfo.getStatus().equals("done")) {
            Utilities.displayNotification();
        } else {
            String sheetId = "xxx";
            String apiKey = "xxx";
            String uri = "https://sheets.googleapis.com/v4/spreadsheets/%s/values/results!A2:D30?key=" + apiKey;
            String finalUri = String.format(uri, sheetId, apiKey);
            ObjectMapper objectMapper = new ObjectMapper();

            LOGGER.log(Level.INFO, "ReminderService: Request to get sheet data now");
            SheetResult result = objectMapper.readValue(new URL(finalUri), SheetResult.class);
            if (!result.getValues().isEmpty()) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date dateFromSheet = simpleDateFormat.parse(result.getValues().get(0).get(0));
                LocalDateTime dateFromSheetForComparing = LocalDateTime.ofInstant(dateFromSheet.toInstant(), ZoneId.systemDefault());

                List<String> patients = new ArrayList<>();
                String lastUpdated = "";
                boolean isNotify = false;
                for (List<String> values : result.getValues()) {
                    String emailFromSheet = values.get(1);
                    LOGGER.log(Level.INFO, "ReminderService: Info from sheet, date = {}, mail = {}", new Object[]{dateFromSheetForComparing, emailFromSheet});
                    if (dateFromSheetForComparing.toLocalDate().equals(LocalDate.now())
                            && emailFromSheet.equalsIgnoreCase(appInfo.getName())) {
                        patients.add(values.get(2));
                        lastUpdated = values.get(3);
                        isNotify = true;
                    }
                }
                if (isNotify) {
                    Utilities.writeContentToFile(appInfo.getName(), LocalDate.now().toString(), "not confirmed", patients, lastUpdated);
                    Utilities.displayNotification();
                }
            }
        }
    }
}
