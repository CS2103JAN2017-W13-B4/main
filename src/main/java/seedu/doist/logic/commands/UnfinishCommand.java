package seedu.doist.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.doist.logic.commands.exceptions.CommandException;
import seedu.doist.model.task.ReadOnlyTask;
import seedu.doist.model.task.UniqueTaskList.TaskAlreadyUnfinishedException;
import seedu.doist.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Marks the task as 'unfinished' identified using it's last displayed index from the to-do list.
 */
public class UnfinishCommand extends Command {

    public static ArrayList<String> commandWords = new ArrayList<>(Arrays.asList("unfinish", "unfinished", "unfin"));
    public static final String DEFAULT_COMMAND_WORD = "unfinish";

    public static final String MESSAGE_USAGE = info().getUsageTextForCommandWords()
            + ": Marks the tasks as not 'finished' identified by the index numbers used in the last task listing.\n"
            + "Parameters: INDEX [INDEX...] (must be a positive integer)\n"
            + "Example: " + DEFAULT_COMMAND_WORD + " 1 8";

    public static final String MESSAGE_UNFINISH_TASK_SUCCESS = "Unfinished Tasks: %1$s";
    public static final String MESSAGE_TASK_ALREADY_NOT_FINISHED = "Tasks already not finished: %1$s";

    public final int[] targetIndices;

    public UnfinishCommand(int[] targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute() throws CommandException {
        String outputMessage = "";

        ArrayList<ReadOnlyTask> tasksToUnfinish = getMultipleTasksFromIndices(targetIndices);
        ArrayList<ReadOnlyTask> tasksUnfinished = new ArrayList<ReadOnlyTask>();
        ArrayList<ReadOnlyTask> tasksAlreadyNotFinished = new ArrayList<ReadOnlyTask>();

        for (ReadOnlyTask task : tasksToUnfinish) {
            try {
                model.unfinishTask(task);
                tasksUnfinished.add(task);
            } catch (TaskNotFoundException pnfe) {
                assert false : "The target task cannot be missing";
            } catch (TaskAlreadyUnfinishedException e) {
                tasksAlreadyNotFinished.add(task);
            }
        }
        if (!tasksAlreadyNotFinished.isEmpty()) {
            outputMessage += String.format(MESSAGE_TASK_ALREADY_NOT_FINISHED, tasksAlreadyNotFinished + "\n");
        }
        if (!tasksUnfinished.isEmpty()) {
            outputMessage += String.format(MESSAGE_UNFINISH_TASK_SUCCESS, tasksUnfinished);
        }
        return new CommandResult(outputMessage);
    }

    public static CommandInfo info() {
        return new CommandInfo(commandWords, DEFAULT_COMMAND_WORD);
    }
}