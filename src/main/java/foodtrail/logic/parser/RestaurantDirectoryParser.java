package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import foodtrail.commons.core.LogsCenter;
import foodtrail.logic.commands.AddCommand;
import foodtrail.logic.commands.ClearCommand;
import foodtrail.logic.commands.Command;
import foodtrail.logic.commands.DeleteCommand;
import foodtrail.logic.commands.EditCommand;
import foodtrail.logic.commands.ExitCommand;
import foodtrail.logic.commands.FindCommand;
import foodtrail.logic.commands.HelpCommand;
import foodtrail.logic.commands.ListCommand;
import foodtrail.logic.commands.MarkCommand;
import foodtrail.logic.commands.RateCommand;
import foodtrail.logic.commands.SortCommand;
import foodtrail.logic.commands.TagCommand;
import foodtrail.logic.commands.UnmarkCommand;
import foodtrail.logic.commands.UnrateCommand;
import foodtrail.logic.commands.UntagCommand;
import foodtrail.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class RestaurantDirectoryParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(RestaurantDirectoryParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level
        // (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case TagCommand.COMMAND_WORD:
            return new TagCommandParser().parse(arguments);

        case UntagCommand.COMMAND_WORD:
            return new UntagCommandParser().parse(arguments);

        case MarkCommand.COMMAND_WORD:
            return new MarkCommandParser().parse(arguments);

        case UnmarkCommand.COMMAND_WORD:
            return new UnmarkCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case RateCommand.COMMAND_WORD:
            return new RateCommandParser().parse(arguments);

        case UnrateCommand.COMMAND_WORD:
            return new UnrateCommandParser().parse(arguments);

        case SortCommand.COMMAND_WORD:
            return new SortCommand();

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
