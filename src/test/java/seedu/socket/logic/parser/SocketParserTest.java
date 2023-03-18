package seedu.socket.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.socket.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.socket.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.socket.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.socket.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.socket.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.socket.logic.commands.CommandTestUtil.VALID_GITHUBPROFILE_AMY;
import static seedu.socket.logic.commands.CommandTestUtil.VALID_LANGUAGE_PYTHON;
import static seedu.socket.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.socket.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_LANGUAGE;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_PROFILE;
import static seedu.socket.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.socket.testutil.Assert.assertThrows;
import static seedu.socket.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.socket.logic.commands.AddCommand;
import seedu.socket.logic.commands.ClearCommand;
import seedu.socket.logic.commands.DeleteCommand;
import seedu.socket.logic.commands.EditCommand;
import seedu.socket.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.socket.logic.commands.ExitCommand;
import seedu.socket.logic.commands.FindCommand;
import seedu.socket.logic.commands.HelpCommand;
import seedu.socket.logic.commands.ListCommand;
import seedu.socket.logic.commands.RedoCommand;
import seedu.socket.logic.commands.RemoveCommand;
import seedu.socket.logic.commands.RemoveCommand.RemovePersonDescriptor;
import seedu.socket.logic.commands.SortCommand;
import seedu.socket.logic.commands.UndoCommand;
import seedu.socket.logic.commands.ViewCommand;
import seedu.socket.logic.parser.exceptions.ParseException;
import seedu.socket.model.person.Person;
import seedu.socket.model.person.predicate.PersonContainsKeywordsPredicate;
import seedu.socket.testutil.EditPersonDescriptorBuilder;
import seedu.socket.testutil.PersonBuilder;
import seedu.socket.testutil.PersonUtil;
import seedu.socket.testutil.RemovePersonDescriptorBuilder;

public class SocketParserTest {

    private final SocketParser parser = new SocketParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + TAG_DESC_FRIEND) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().withProfile(VALID_GITHUBPROFILE_AMY)
                .withAddress(VALID_ADDRESS_AMY).withEmail(VALID_EMAIL_AMY).withPhone(VALID_PHONE_AMY)
                .withLanguages(VALID_LANGUAGE_PYTHON).withTags(VALID_TAG_FRIEND).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> emptyKeywords = Collections.emptyList();
        List<String> nameKeywords = Arrays.asList("foo", "bar", "baz");
        FindCommand findNameCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_NAME + nameKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                nameKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords)), findNameCommand);


        List<String> profileKeywords = Arrays.asList("-hans-b0", "t-e-s-t-", "-DaViD-");
        FindCommand findProfileCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD
                        + " "
                        + PREFIX_PROFILE
                        + profileKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                emptyKeywords,
                profileKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords)), findProfileCommand);

        List<String> phoneKeywords = Arrays.asList("999", "12345678", "00001111");
        FindCommand findPhoneCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD
                        + " "
                        + PREFIX_PHONE
                        + phoneKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                emptyKeywords,
                emptyKeywords,
                phoneKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords)), findPhoneCommand);

        List<String> emailKeywords = Arrays.asList("test1@nus.edu", "boHans99@gmail.com", "customer@SOCket.sg");
        FindCommand findEmailCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD
                        + " "
                        + PREFIX_EMAIL
                        + emailKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emailKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords)), findEmailCommand);

        List<String> addressKeywords = Arrays.asList("bedok", "99", "block");
        FindCommand findAddressCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD
                        + " "
                        + PREFIX_ADDRESS
                        + addressKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                addressKeywords,
                emptyKeywords,
                emptyKeywords)), findAddressCommand);

        List<String> languageKeywords = Arrays.asList("Python", "c++", "java");
        FindCommand findLanguageCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD
                        + " "
                        + PREFIX_LANGUAGE
                        + languageKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                languageKeywords,
                emptyKeywords)), findLanguageCommand);

        List<String> tagKeywords = Arrays.asList("friends", "cs2103t", "student");
        FindCommand findTagCommand = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD
                        + " "
                        + PREFIX_TAG
                        + tagKeywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                emptyKeywords,
                tagKeywords)), findTagCommand);
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
    public void parseCommand_sort() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD) instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " address") instanceof SortCommand);
    }
    @Test
    public void parseCommand_remove() throws Exception {
        Person person = new PersonBuilder().withProfile(VALID_GITHUBPROFILE_AMY)
                .withAddress(VALID_ADDRESS_AMY).withEmail(VALID_EMAIL_AMY).withPhone(VALID_PHONE_AMY)
                .withLanguages(VALID_LANGUAGE_PYTHON).withTags(VALID_TAG_FRIEND).build();
        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder(person).build();
        RemoveCommand command = (RemoveCommand) parser.parseCommand(RemoveCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getRemovePersonDescriptorDetails(descriptor));
        assertEquals(new RemoveCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD + " 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_redo() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD + " 3") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewCommand command = (ViewCommand) parser.parseCommand(
                ViewCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new ViewCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
