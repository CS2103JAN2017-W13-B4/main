package seedu.doist.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.joestelmach.natty.DateGroup;

import seedu.doist.commons.exceptions.IllegalValueException;
import seedu.doist.commons.util.StringUtil;
import seedu.doist.model.tag.Tag;
import seedu.doist.model.tag.UniqueTagList;
import seedu.doist.model.task.Description;
import seedu.doist.model.task.Priority;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes
 */
public class ParserUtil {

    private static final Pattern INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    /**
     * Returns the specified index in the {@code command} if it is a positive unsigned integer
     * Returns an {@code Optional.empty()} otherwise.
     */
    public static Optional<Integer> parseIndex(String command) {
        final Matcher matcher = INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));
    }

    /**
     * Returns an list of integers separated using space in the input string
     */
    public static List<Optional<Integer>> parseIndices(String command) {
        ArrayList<Optional<Integer>> indices = new ArrayList<Optional<Integer>>();
        String[] commandStringComponents = command.trim().split(" ");
        for (String component : commandStringComponents) {
            indices.add(parseIndex(component));
        }
        return indices;
    }

    /**
     * Function to support natural language input for date and time, using a 3rd party library 'Natty'
     * @param date
     * @return extracte Date if parsing is succesful, or null if it fails
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
     * Returns an array of integers separated using space in the input string
     */
    public static int[] parseStringToIntArray(String string) {
        List<Optional<Integer>> optionalIndices = ParserUtil.parseIndices(string);
        for (Optional<Integer> optionalIndex : optionalIndices) {
            if (!optionalIndex.isPresent()) {
                return null;
            }
        }
        int[] indices = new int[optionalIndices.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = optionalIndices.get(i).get().intValue();
        }
        return indices;
    }

    /**
     * Returns a new Set populated by all elements in the given list of strings
     * Returns an empty set if the given {@code Optional} is empty,
     * or if the list contained in the {@code Optional} is empty
     */
    public static Set<String> toSet(Optional<List<String>> list) {
        List<String> elements = list.orElse(Collections.emptyList());
        return new HashSet<>(elements);
    }

    /**
    * Splits a preamble string into ordered fields.
    * @return A list of size {@code numFields} where the ith element is the ith field value if specified in
    *         the input, {@code Optional.empty()} otherwise.
    */
    public static List<Optional<String>> splitPreamble(String preamble, int numFields) {
        return Arrays.stream(Arrays.copyOf(preamble.split("\\s+", numFields), numFields))
                .map(Optional::ofNullable)
                .collect(Collectors.toList());
    }

    /**
     * Parses a {@code Optional<String> desc} into an {@code Optional<Description>} if {@code desc} is present.
     */
    public static Optional<Description> parseDesc(Optional<String> desc) throws IllegalValueException {
        assert desc != null;
        return desc.isPresent() ? Optional.of(new Description(desc.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> priority} into an {@code Optional<Priority>} if {@code priority} is present.
     */
    public static Optional<Priority> parsePriority(Optional<String> priorityRawValue) throws IllegalValueException {
        assert priorityRawValue != null;
        return priorityRawValue.isPresent() ? Optional.of(new Priority(priorityRawValue.get())) : Optional.empty();
    }

    /**
     * Parses {@code Collection<String> tags} into an {@code UniqueTagList}.
     */
    public static UniqueTagList parseTags(Collection<String> tags) throws IllegalValueException {
        assert tags != null;
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        return new UniqueTagList(tagSet);
    }

    /**
     * Parses {@code <String> tagsParameterString} into an {@code UniqueTagList}.
     */
    public static UniqueTagList parseTagsFromString(String tagsParameterString) throws IllegalValueException {
        assert tagsParameterString != null;
        String[] extractedTags = tagsParameterString.trim().split(" ");
        return ParserUtil.parseTags(Arrays.asList(extractedTags));
    }

    /**
     * Parses {@code <String> parameterString} into an {@code ArrayList<String>}.
     */
    public static ArrayList<String> getParameterKeysFromString(String parameterString) {
        ArrayList<String> parameterKeys = new ArrayList<String>();
        LinkedList<String> parametersList = new LinkedList<String>(Arrays.asList(parameterString.split("\\\\")));
        parametersList.poll();  // remove the first item, which is an empty string
        for (String parameterPair : parametersList) {
            String parameterKey = "\\" + parameterPair.split(" ")[0];
            parameterKeys.add(parameterKey);
        }
        return parameterKeys;
    }
}





