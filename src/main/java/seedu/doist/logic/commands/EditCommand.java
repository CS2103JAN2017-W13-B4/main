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
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static ArrayList<String> commandWords = new ArrayList<>(Arrays.asList("edit"));
    public static final String DEFAULT_COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = getUsageTextForCommandWords()
            + ": Edits the details of the person identified " + "by the index number used in the last person listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) [NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS ] [t/TAG]...\n"
            + "Example: " + DEFAULT_COMMAND_WORD + " 1 p/91234567 e/johndoe@yahoo.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final int filteredPersonListIndex;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param filteredPersonListIndex
     *            the index of the person in the filtered person list to edit
     * @param editPersonDescriptor
     *            details to edit the person with
     */
    public EditCommand(int filteredPersonListIndex, EditPersonDescriptor editPersonDescriptor) {
        assert filteredPersonListIndex > 0;
        assert editPersonDescriptor != null;

        // converts filteredPersonListIndex from one-based to zero-based.
        this.filteredPersonListIndex = filteredPersonListIndex - 1;

        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (filteredPersonListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyTask personToEdit = lastShownList.get(filteredPersonListIndex);
        Task editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        try {
            model.updateTask(filteredPersonListIndex, editedPerson);
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        model.updateFilteredListToShowAll();
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, personToEdit));
    }

    /**
     * Creates and returns a {@code Person} with the details of
     * {@code personToEdit} edited with {@code editPersonDescriptor}.
     */
    private static Task createEditedPerson(ReadOnlyTask taskToEdit, EditPersonDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        Description updatedName = editTaskDescriptor.getName().orElseGet(taskToEdit::getDescription);
        Priority updatedPriority = editTaskDescriptor.getPriority().orElseGet(taskToEdit::getPriority);
        UniqueTagList updatedTags = editTaskDescriptor.getTags().orElseGet(taskToEdit::getTags);

        return new Task(updatedName, updatedPriority, updatedTags);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value
     * will replace the corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Optional<Description> desc = Optional.empty();
        private Optional<Priority> priority = Optional.empty();
        private Optional<UniqueTagList> tags = Optional.empty();

        public EditPersonDescriptor() {
        }

        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            this.desc = toCopy.getName();
            this.tags = toCopy.getTags();
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyPresent(this.desc, this.tags);

        }

        public void setName(Optional<Description> name) {
            assert name != null;
            this.desc = name;
        }

        public Optional<Description> getName() {
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

    /**
     * @return a string containing all the command words to be shown in the
     *         usage message, in the format of (word1|word2|...)
     */
    protected static String getUsageTextForCommandWords() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (!commandWords.contains(DEFAULT_COMMAND_WORD)) {
            sb.append(DEFAULT_COMMAND_WORD + "|");
        }
        for (String commandWord : commandWords) {
            sb.append(commandWord + "|");
        }
        sb.setCharAt(sb.length() - 1, ')');
        return sb.toString();
    }

    public static boolean canCommandBeTriggeredByWord(String word) {
        return commandWords.contains(word) || DEFAULT_COMMAND_WORD.equals(word);
    }
}
