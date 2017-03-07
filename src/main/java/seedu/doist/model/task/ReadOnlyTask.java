package seedu.doist.model.task;

import java.util.Comparator;

import seedu.doist.model.tag.UniqueTagList;
import seedu.doist.model.task.Priority.PriorityLevel;

/**
 * A read-only immutable interface for a Task in the to-do list.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Description getDescription();
    Priority getPriority();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the person's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getDescription().equals(this.getDescription())); // state checks here onwards
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getDescription());
        getTags().forEach(builder::append);
        return builder.toString();
    }

    public class ReadOnlyTaskPriorityComparator implements Comparator<ReadOnlyTask> {

        @Override
        public int compare(ReadOnlyTask task1, ReadOnlyTask task2) {
            // Highest priority to lowest priority
            PriorityLevel task1Priority = task1.getPriority().getPriorityLevel();
            PriorityLevel task2Priority = task2.getPriority().getPriorityLevel();
            return task2Priority.compareTo(task1Priority);
        }
    }
}
