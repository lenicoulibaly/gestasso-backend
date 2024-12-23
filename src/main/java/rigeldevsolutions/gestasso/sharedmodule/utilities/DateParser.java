package rigeldevsolutions.gestasso.sharedmodule.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateParser {
    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
        {
            put("^\\d{8}$", "yyyyMMdd");
            put("^\\d{12}$", "yyyyMMddHHmm");
            put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
            put("^\\d{14}$", "yyyyMMddHHmmss");
            put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
            put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "dd/MM/yyyy");
            put("^\\d{1,2}/\\d{1,2}/\\d{2}\\s\\d{1,2}:\\d{2}$", "dd/MM/yy HH:mm");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
            put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
            put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
            put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
            put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
            put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
            put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
            put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
            put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
            put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
            put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
            put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
            put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
            put("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{2}[-+]\\d{2}:\\d{2}$", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        }
    };

    /**
     * To Determine the pattern by the string date value
     * 
     * @param dateString
     * @return The matching SimpleDateFormat pattern, or null if format is unknown.
     */
    public static String determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.matches(regexp) || dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        parse("2011-09-27T07:04:21.97-05:00"); //here is your value
        parse("20110917");
        parse("01/02/2018");
        parse("02-01-2018 06:07:59");
        parse("02 January 2018");
    }

    public static void parse(String value) {
        if (value != null) {
            String format = determineDateFormat(value);
            if (format != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                try {
                    Date date = sdf.parse(value);
                    System.out.println(String.format("Format : %s | Value : %s | Parsed Date : %s", value, date, format));
                } catch (ParseException e) {
                    // Failed the execution
                }
            }
        }
    }

    public static String getMonthAndYear(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être nulle");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);

        return date.format(formatter);
    }

    public static String getSemester(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être nulle");
        }
        int month = date.getMonthValue();
        int year = date.getYear();
        String semester = month <= 6 ? "1er semestre" : "2nd semestre";

        return semester + " " + year;
    }

    public static String getTrimester(LocalDate date)
    {
        if (date == null) throw new IllegalArgumentException("La date ne peut pas être nulle");

        int month = date.getMonthValue();
        int year = date.getYear();
        String trimester = month <= 3 ? "1er trimestre" :
                           month <= 6 ? "2nd trimestre" :
                           month <= 9 ? "3ème trimestre" : "4ème trimestre";

        return trimester + " " + year;
    }

    public static String getWeekOfYear(LocalDate date)
    {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être nulle");
        }
        int weekOfYear = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int year = date.getYear();
        String suffix = getOrdinalSuffix(weekOfYear);
        return weekOfYear + suffix + " semaine " + year;
    }

    private static String getOrdinalSuffix(int number)
    {
        return number == 1 ? "ère" : (number == 2 ? "nde" : "ème");
    }
}