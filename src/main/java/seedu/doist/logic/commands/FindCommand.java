package seedu.doist.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Finds and lists all tasks in to-do list whose description contains any of the
 * argument keywords. Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static ArrayList<String> commandWords = new ArrayList<>(Arrays.asList("find"));
    public static final String DEFAULT_COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = info().getUsageTextForCommandWords()
            + ": Finds all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n" + "Example: " + DEFAULT_COMMAND_WORD + " go NUS";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(keywords);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

    public static CommandInfo info() {
        return new CommandInfo(commandWords, DEFAULT_COMMAND_WORD);
    }
}
