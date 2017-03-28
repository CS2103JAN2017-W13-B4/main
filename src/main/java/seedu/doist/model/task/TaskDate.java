package seedu.doist.model.task;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.joestelmach.natty.DateGroup;

import seedu.doist.commons.exceptions.IllegalValueException;

//@@author A0147620L
public class TaskDate {

    private Date startDate;
    private Date endDate;

    public TaskDate() {
        this.startDate = null;
        this.endDate = null;
    }

    public TaskDate (Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isPast() {
        if (this.getStartDate() != null && this.getEndDate() != null) {
            Date currentDate = new Date();
            return (this.getEndDate().compareTo(currentDate) < 0);
        } else {
            return false;
        }
    }

    public boolean isFloating() {
        return this.getStartDate() == null || this.getEndDate() == null;
    }

    public boolean isDeadline() {
        // Both not null
        if (!isFloating()) {
            return this.getStartDate().equals(this.getEndDate());
        } else {
            return false;
        }
    }

    public boolean isEvent() {
        return !isDeadline() && !isFloating();
    }

    /**
     * Function to check whether the date input is valid i.e, the Start date is before or equal to the End Date.
     * Also checks if the parsing of dates has been successful
     * @param startDate
     * @param endDate
     * @return
     * @throws IllegalValueException
     */
    public static boolean validateDate (Date startDate, Date endDate) throws IllegalValueException {
        if (startDate == null || endDate == null) {
            throw new IllegalValueException("Incorrect Dates");
        } else {
            return (startDate.compareTo(endDate) <= 0) ? true : false;
        }
    }

    /**
     * Function to support natural language input for date and time, using a 3rd party library 'Natty'
     * @param date
     * @return extracted Date if parsing is successful, or null if it fails
     */
    public static Date parseDate (String date) {
        com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser();
        List<DateGroup> groups = parser.parse(date);
        Date extractDate = null;
        boolean flag = false;
        for (DateGroup group:groups) {
            List<Date> dates = group.getDates();
            if (!dates.isEmpty()) {
                extractDate = dates.get(0);
                flag = true;
            }
        }
        return (flag ? extractDate : null);
    }

    @Override
    public String toString() {
        if (this.startDate != null && this.endDate != null) {
            return this.startDate.toString() + "--" + this.endDate.toString();
        } else {
            return "No dates";
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskDate // instanceof handles nulls
                && this.startDate == ((TaskDate) other).startDate // state check
                && this.endDate == ((TaskDate) other).endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
