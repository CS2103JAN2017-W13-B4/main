package seedu.doist.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import seedu.doist.commons.core.Messages;
import seedu.doist.commons.util.CollectionUtil;
import seedu.doist.logic.commands.exceptions.CommandException;
import seedu.doist.model.tag.UniqueTagList;
import seedu.doist.model.task.Description;
import seedu.doist.model.task.Priority;
import seedu.doist.model.task.ReadOnlyTask;
import seedu.doist.model.task.Task;
import seedu.doist.model.task.UniqueTaskList;

/**
 * Edits the details of an existing task in the to-do list.
 */
public class EditCommand extends Command {

    public static ArrayList<String> commandWords = new ArrayList<>(Arrays.asList("edit"));
    public static final String DEFAULT_COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = info().getUsageTextForCommandWords()
            + ": Edits the details of the task identified " + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) [DESCRIPTION] [/as PRIORITY] [/under TAG]...\n"
            + "Example: " + DEFAULT_COMMAND_WORD + " 1 do stuff today";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists!";

    private final int filteredTaskListIndex;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param filteredTaskListIndex
     *            the index of the person in the filtered person list to edit
     * @param editTaskDescriptor
     *            details to edit the person with
     */
    public EditCommand(int filteredTaskListIndex, EditTaskDescriptor editTaskDescriptor) {
        assert filteredTaskListIndex > 0;
        assert editTaskDescriptor != null;

        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;

        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    public static CommandInfo info() {
        return new CommandInfo(commandWords, DEFAULT_COMMAND_WORD);
    }


    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToEdit = lastShownList.get(filteredTaskListIndex);
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        try {
            model.updateTask(filteredTaskListIndex, editedTask);
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
        model.updateFilteredListToShowAll();
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, taskToEdit));
    }

    /**
     * Creates and returns a {@code Task} with the details of
     * {@code taskToEdit} edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        Description updatedName = editTaskDescriptor.getDesc().orElseGet(taskToEdit::getDescription);
        Priority updatedPriority = editTaskDescriptor.getPriority().orElseGet(taskToEdit::getPriority);
        UniqueTagList updatedTags = editTaskDescriptor.getTags().orElseGet(taskToEdit::getTags);

        return new Task(updatedName, updatedPriority, updatedTags);
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value
     * will replace the corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private Optional<Description> desc = Optional.empty();
        private Optional<Priority> priority = Optional.empty();
        private Optional<UniqueTagList> tags = Optional.empty();

        public EditTaskDescriptor() {
        }

        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            this.desc = toCopy.getDesc();
            this.tags = toCopy.getTags();
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyPresent(this.desc, this.tags);

        }

        public void setDesc(Optional<Description> desc) {
            assert desc != null;
            this.desc = desc;
        }

        public Optional<Description> getDesc() {
            return desc;
        }

        public Optional<Priority> getPriority() {
            return priority;
        }

        public void setTags(Optional<UniqueTagList> tags) {
            assert tags != null;
            this.tags = tags;
        }

        public Optional<UniqueTagList> getTags() {
            return tags;
        }
    }
}
