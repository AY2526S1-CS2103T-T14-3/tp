package foodtrail.logic.parser;

import static foodtrail.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static foodtrail.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static foodtrail.testutil.Assert.assertThrows;
import static foodtrail.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import foodtrail.logic.commands.AddCommand;
import foodtrail.logic.commands.ClearCommand;
import foodtrail.logic.commands.DeleteCommand;
import foodtrail.logic.commands.EditCommand;
import foodtrail.logic.commands.EditCommand.EditRestaurantDescriptor;
import foodtrail.logic.commands.ExitCommand;
import foodtrail.logic.commands.FindCommand;
import foodtrail.logic.commands.HelpCommand;
import foodtrail.logic.commands.ListCommand;
import foodtrail.logic.commands.TagCommand;
import foodtrail.logic.commands.UnrateCommand;
import foodtrail.logic.commands.UntagCommand;
import foodtrail.logic.parser.exceptions.ParseException;
import foodtrail.model.restaurant.Restaurant;
import foodtrail.model.restaurant.RestaurantContainsKeywordsPredicate;
import foodtrail.model.restaurant.Tag;
import foodtrail.testutil.EditRestaurantDescriptorBuilder;
import foodtrail.testutil.RestaurantBuilder;
import foodtrail.testutil.RestaurantUtil;

public class RestaurantDirectoryParserTest {

    private final RestaurantDirectoryParser parser = new RestaurantDirectoryParser();

    @Test
    public void parseCommand_add() throws Exception {
        Restaurant restaurant = new RestaurantBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(RestaurantUtil.getAddCommand(restaurant));
        assertEquals(new AddCommand(restaurant), command);
    }

    @Test
    public void parseCommand_tag() throws Exception {
        final Tag tag = new Tag("friend");
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        TagCommand command = (TagCommand) parser.parseCommand(TagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_RESTAURANT.getOneBased() + " " + CliSyntax.PREFIX_TAG + tag.tagName);
        assertEquals(new TagCommand(INDEX_FIRST_RESTAURANT, tags), command);
    }

    @Test
    public void parseCommand_untag() throws Exception {
        final Tag tag = new Tag("friend");
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        UntagCommand command = (UntagCommand) parser.parseCommand(UntagCommand.COMMAND_WORD + " "
                + INDEX_FIRST_RESTAURANT.getOneBased() + " " + CliSyntax.PREFIX_TAG + tag.tagName);
        assertEquals(new UntagCommand(INDEX_FIRST_RESTAURANT, tags), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_RESTAURANT), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Restaurant restaurant = new RestaurantBuilder().build();
        EditRestaurantDescriptor descriptor = new EditRestaurantDescriptorBuilder(restaurant).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_RESTAURANT.getOneBased() + " "
                + RestaurantUtil.getEditRestaurantDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_RESTAURANT, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(",")));
        assertEquals(new FindCommand(new RestaurantContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrate() throws Exception {
        UnrateCommand command = (UnrateCommand) parser.parseCommand(
                UnrateCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased());
        assertEquals(new UnrateCommand(INDEX_FIRST_RESTAURANT), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), () ->
                parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
