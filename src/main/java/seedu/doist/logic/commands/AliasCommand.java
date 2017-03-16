package seedu.doist.logic.commands;

import seedu.doist.logic.commands.exceptions.CommandException;

/**
 * Adds a person to the address book.
 */
public class AliasCommand extends Command {
    public static final String DEFAULT_COMMAND_WORD = "alias";

    public static final String MESSAGE_USAGE = info().getUsageTextForCommandWords() + ": Adds an alias for a command\n"
            + "Parameters: ALIAS  [\\for DEFAULT_COMMAND_WORD]\n"
            + "Example: " + DEFAULT_COMMAND_WORD + " list_tasks \\for list";

    public static final String MESSAGE_SUCCESS = "New alias \"%1$s\" added to command \"%2$s\"";
    public static final String MESSAGE_COMMAND_WORD_NOT_EXIST = "This command word \"%1$s\" does not exist";
    public static final String MESSAGE_ALIAS_IS_DEFAULT_COMMAND_WORD = "The input alias \"%1$s\" " +
                                                                       "is already a default command word";

    private final String defaultCommandWord;
    private final String alias;

    public AliasCommand(String alias, String defaultCommandWord) {
        this.defaultCommandWord = defaultCommandWord;
        this.alias = alias;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!Command.getDefaultCommandWordSet().contains(defaultCommandWord)) {
            throw new CommandException(String.format(MESSAGE_COMMAND_WORD_NOT_EXIST, defaultCommandWord));
        } else if (Command.getDefaultCommandWordSet().contains(alias)) {
            throw new CommandException(String.format(MESSAGE_ALIAS_IS_DEFAULT_COMMAND_WORD, alias));
        } else {
            Command.setAlias(alias, defaultCommandWord);
            return new CommandResult(String.format(MESSAGE_SUCCESS, alias, defaultCommandWord));
        }
    }

    public static CommandInfo info() {
        return new CommandInfo(Command.getAliasList(DEFAULT_COMMAND_WORD), DEFAULT_COMMAND_WORD);
    }
}
