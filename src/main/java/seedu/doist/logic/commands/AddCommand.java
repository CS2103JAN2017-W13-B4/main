package seedu.doist.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import seedu.doist.commons.exceptions.IllegalValueException;
import seedu.doist.logic.commands.exceptions.CommandException;
import seedu.doist.logic.parser.CliSyntax;
import seedu.doist.logic.parser.ParserUtil;
import seedu.doist.model.tag.UniqueTagList;
import seedu.doist.model.task.Description;
import seedu.doist.model.task.Priority;
import seedu.doist.model.task.Task;
import seedu.doist.model.task.UniqueTaskList;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static ArrayList<String> commandWords = new ArrayList<>(Arrays.asList("add", "do"));
    public static final String DEFAULT_COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = info().getUsageTextForCommandWords() + ": Adds a task to Doist\n"
            + "Parameters: TASK_DESCRIPTION  [\\from START_TIME] [\\to END_TIME] [\\as PRIORITY] [\\under TAG...]\n"
            + "Example: " + DEFAULT_COMMAND_WORD + "Group meeting \\from 1600 \\to 1800 \\as IMPORTANT \\under school ";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This task already exists in the to-do list";

    private final Task toAdd;
    private UniqueTagList tagList = new UniqueTagList();

    /**
     * Creates an AddCommand using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     */
    public AddCommand(String preamble, Map<String, List<String>> parameters) throws IllegalValueException {
        if (preamble == null || preamble.trim().isEmpty()) {
            throw new IllegalValueException("You can't add a task without a description!");
        }

        List<String> tagsParameterStringList = parameters.get("\\under");
        if (tagsParameterStringList != null && !tagsParameterStringList.isEmpty()) {
            tagList = ParserUtil.parseTagsFromString(tagsParameterStringList.get(0));
        }
        this.toAdd = new Task(new Description(preamble), tagList);

        List<String> priority = parameters.get(CliSyntax.PREFIX_AS.toString());
        System.out.println(parameters);
        if (priority != null && priority.size() > 0) {
            String strPriority = priority.get(0).trim();
            this.toAdd.setPriority(new Priority(strPriority));
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
    }

    public static CommandInfo info() {
        return new CommandInfo(commandWords, DEFAULT_COMMAND_WORD);
    }
}
