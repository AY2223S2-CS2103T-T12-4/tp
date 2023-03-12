package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.function.Predicate;

import seedu.address.commons.core.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.LanguageContainsKeywordsPredicate;
import seedu.address.model.tag.TagContainsKeywordsPredicate;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    private final Predicate<Person> predicate;
    private final TagContainsKeywordsPredicate predicateTag;

    private final LanguageContainsKeywordsPredicate predicateLang;
    private final boolean isKeywordPresent;

    /**
     * Initializes a ListCommand with the given predicate and whether a tag is present.
     */
    public ListCommand(TagContainsKeywordsPredicate predicateTag, LanguageContainsKeywordsPredicate predicateLang,
                       boolean isKeywordPresent) {
        this.predicateTag = predicateTag;
        this.predicateLang = predicateLang;
        this.predicate = predicateTag.and(predicateLang);
        this.isKeywordPresent = isKeywordPresent;
    }
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (!isKeywordPresent) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            model.updateFilteredPersonList(predicate);
            return new CommandResult(
                    String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && predicateTag.equals(((ListCommand) other).predicateTag)
                && predicateLang.equals(((ListCommand) other).predicateLang)
                && (isKeywordPresent == ((ListCommand) other).isKeywordPresent)); // state check
    }
}
