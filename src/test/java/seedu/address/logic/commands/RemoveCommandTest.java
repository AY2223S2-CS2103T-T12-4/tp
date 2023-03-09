package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GITHUBPROFILE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LANGUAGE_CPLUSPLUS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LANGUAGE_PYTHON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveCommand.RemovePersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.RemovePersonDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemoveCommand.
 */
public class RemoveCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder();
        Person removedFieldPerson = personInList.withName("George Best").build();

        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder()
                .withPhone("9482442")
                .withEmail("anna@example.com")
                .withLanguages("Python")
                .withAddress("4th street").build();
        RemoveCommand removeCommand = new RemoveCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(RemoveCommand.MESSAGE_REMOVE_FIELD_SUCCESS, removedFieldPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, removedFieldPerson);

        assertCommandSuccess(removeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder();
        Person removedFieldPerson = personInList.withName("George Best")
                .withEmail("anna@example.com")
                .withAddress("4th street").build();

        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder()
                .withPhone("9482442").withLanguages("Python").build();
        RemoveCommand removeCommand = new RemoveCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(RemoveCommand.MESSAGE_REMOVE_FIELD_SUCCESS, removedFieldPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, removedFieldPerson);

        assertCommandSuccess(removeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_languageFieldSpecifiedExistingLanguageFieldUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder();
        Person removedFieldPerson = personInList.withName("George Best")
                .withPhone("9482442")
                .withEmail("anna@example.com")
                .withAddress("4th street").build();

        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder()
                .withLanguages(VALID_LANGUAGE_PYTHON).build();
        RemoveCommand removeCommand = new RemoveCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(RemoveCommand.MESSAGE_REMOVE_FIELD_SUCCESS, removedFieldPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, removedFieldPerson);

        assertCommandSuccess(removeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clearTagAndLanguageFieldSpecifiedUnfilteredList_success() {
        Index indexBenson = Index.fromOneBased(2);
        Person benson = model.getFilteredPersonList().get(indexBenson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder();
        Person removedFieldPerson = personInList.withName("Benson Meier")
                .withProfile("benson-meier")
                .withAddress("311, Clementi Ave 2, #02-25")
                .withEmail("johnd@example.com")
                .withPhone("98765432").build();

        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder()
                .withLanguages().withTags().build();
        RemoveCommand removeCommand = new RemoveCommand(indexBenson, descriptor);

        String expectedMessage = String.format(RemoveCommand.MESSAGE_REMOVE_FIELD_SUCCESS, removedFieldPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(benson, removedFieldPerson);

        assertCommandSuccess(removeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_specificFieldNotMatchUnfilteredList_failure() {
        Index indexBenson = Index.fromOneBased(2);

        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder()
                .withProfile(VALID_GITHUBPROFILE_AMY)
                .withLanguages(VALID_LANGUAGE_CPLUSPLUS)
                .withTags(VALID_TAG_HUSBAND).build();
        RemoveCommand removeCommand = new RemoveCommand(indexBenson, descriptor);

        assertCommandFailure(removeCommand, model, RemoveCommand.MESSAGE_REMOVE_FIELD_NOT_MATCH);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemovePersonDescriptor descriptor = new RemovePersonDescriptorBuilder().build();
        RemoveCommand editCommand = new RemoveCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemoveCommand editCommand = new RemoveCommand(outOfBoundIndex,
                new RemovePersonDescriptorBuilder().build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


}
