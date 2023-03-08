package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.*;
import seedu.address.model.tag.Language;
import seedu.address.model.tag.Tag;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

public class RemoveCommand extends Command {

    public static final String COMMAND_WORD = "remove";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the field of the person identified "
            + "by the index number used in the displayed person list. "
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_PROFILE + "GITHUBPROFILE] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_LANGUAGE + "LANGUAGE]...\n"
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_LANGUAGE + "Java "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_REMOVE_FIELD_SUCCESS = "Remove field: %1$s";

    public static final String MESSAGE_NOT_REMOVE = "At least one field to remove must be provided.";

    public static final String MESSAGE_REMOVE_FIELD_NOT_MATCH = "The field provided does not exist in the SOCket.";

    private final Index index;

    private final RemovePersonDescriptor removePersonDescriptor;

    public RemoveCommand(Index index, RemovePersonDescriptor removePersonDescriptor) {
        requireNonNull(index);

        this.index = index;
        this.removePersonDescriptor = new RemovePersonDescriptor(removePersonDescriptor);
    }

    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personFieldToRemove = lastShownList.get(index.getZeroBased());
        Person removedFieldPerson = createRemoveFieldPerson(personFieldToRemove, removePersonDescriptor);

        if (!removePersonDescriptor.isAnyFieldRemoved()) {
            throw new CommandException(MESSAGE_REMOVE_FIELD_NOT_MATCH);
        }

        model.setPerson(personFieldToRemove, removedFieldPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_REMOVE_FIELD_SUCCESS, removedFieldPerson));
    }

    private static Person createRemoveFieldPerson(Person personToRemoveField, RemoveCommand.RemovePersonDescriptor removePersonDescriptor) {
        assert personToRemoveField != null;

        removePersonDescriptor.setPerson(personToRemoveField);

        Name defaultName = personToRemoveField.getName();
        GitHubProfile updatedProfile = removePersonDescriptor.getRemoveProfile().orElse(personToRemoveField.getProfile());
        Phone updatedPhone = removePersonDescriptor.getRemovePhone().orElse(personToRemoveField.getPhone());
        Email updatedEmail = removePersonDescriptor.getRemoveEmail().orElse(personToRemoveField.getEmail());
        Address updatedAddress = removePersonDescriptor.getRemoveAddress().orElse(personToRemoveField.getAddress());
        Set<Language> updatedLanguages = removePersonDescriptor.getRemoveLanguages().orElse(personToRemoveField.getLanguages());
        Set<Tag> updatedTags = removePersonDescriptor.getRemoveTags().orElse(personToRemoveField.getTags());

        return new Person(defaultName, updatedProfile, updatedPhone, updatedEmail, updatedAddress, updatedLanguages, updatedTags);
    }

    public static class RemovePersonDescriptor {
        private GitHubProfile profile;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Language> languages;
        private Set<Tag> tags;

        private Person person;

        public RemovePersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code languages} and {@code tags} is used internally.
         */
        public RemovePersonDescriptor(RemoveCommand.RemovePersonDescriptor toCopy) {
            setProfile(toCopy.profile);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setLanguages(toCopy.languages);
            setTags(toCopy.tags);
        }

        public boolean isAnyFieldRemoved() {
            return CollectionUtil.isAnyNonNull(profile, phone, email, address, languages, tags);
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public void setProfile(GitHubProfile profile) {
            this.profile = profile;
        }

        public Optional<GitHubProfile> getRemoveProfile() {
            if (profile == null) {
                return Optional.empty();
            }

            if (!profile.equals(person.getProfile()) && !profile.isEmptyProfile()) {
                setProfile(null);
                return Optional.empty();
            }

            return Optional.of(new GitHubProfile(""));
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getRemovePhone() {
            if (phone == null) {
                return Optional.empty();
            }

            if (!phone.equals(person.getPhone()) && !phone.isPhoneEmpty()) {
                setPhone(null);
                return Optional.empty();
            }

            return Optional.of(new Phone(""));
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getRemoveEmail() {
            if (email == null) {
                return Optional.empty();
            }

            if (!email.equals(person.getEmail()) && !email.isEmailEmpty()) {
                setEmail(null);
                return Optional.empty();
            }

            return Optional.of(new Email(""));
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getRemoveAddress() {
            if (address == null) {
                return Optional.empty();
            }

            if (!address.equals(person.getAddress()) && !address.isAddressEmpty()) {
                setAddress(null);
                return Optional.empty();
            }

            return Optional.of(new Address(""));
        }

        /**
         * Sets {@code languages} to this object's {@code languages}.
         * A defensive copy of {@code languages} is used internally.
         */
        public void setLanguages(Set<Language> languages) {
            this.languages = (languages != null) ? new HashSet<>(languages) : null;
        }

        /**
         * Returns an unmodifiable language set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code languages} is null.
         */
        public Optional<Set<Language>> getRemoveLanguages() {
            if (languages == null) {
                return Optional.empty();
            }

            Set<Language> newLanguages = new HashSet<>(person.getLanguages());

            if (!newLanguages.removeAll(languages) && !languages.isEmpty()) {
                setLanguages(null);
                return Optional.empty();
            }

            return Optional.of(Collections.unmodifiableSet(newLanguages));
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getRemoveTags() {
            if (tags == null) {
                return Optional.empty();
            }

            Set<Tag> newTags = new HashSet<>(person.getTags());

            if (!newTags.removeAll(tags) && !tags.isEmpty()) {
                setTags(null);
                return Optional.empty();
            }

            return Optional.of(Collections.unmodifiableSet(newTags));
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof RemoveCommand.RemovePersonDescriptor)) {
                return false;
            }

            // state check
            RemoveCommand.RemovePersonDescriptor e = (RemoveCommand.RemovePersonDescriptor) other;

            return getRemoveProfile().equals(e.getRemoveProfile())
                    && getRemovePhone().equals(e.getRemovePhone())
                    && getRemoveEmail().equals(e.getRemoveEmail())
                    && getRemoveAddress().equals(e.getRemoveAddress())
                    && getRemoveLanguages().equals(e.getRemoveLanguages())
                    && getRemoveTags().equals(e.getRemoveTags());
        }
    }
}
