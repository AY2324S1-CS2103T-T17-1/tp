package seedu.address.logic.commands;

import seedu.address.model.Model;

public class ScreamCommand extends Command {
    public static final String COMMAND_WORD = "scream";
    public static final String MESSAGE_SUCCESS = "AHHHHHHHHHHHHHHHHHHH";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
