package Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeService implements DateTimeApi {
    @Override
    public String getDateTime() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/uuuu", Locale.ENGLISH);
        String formattedDate = date.format(formatter);
        return formattedDate;
    }
}









