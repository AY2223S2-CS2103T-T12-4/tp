package seedu.socket.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.socket.logic.commands.EditProjectCommand.EditProjectDescriptor;
import seedu.socket.model.person.Address;
import seedu.socket.model.person.Email;
import seedu.socket.model.person.GitHubProfile;
import seedu.socket.model.person.Name;
import seedu.socket.model.person.Person;
import seedu.socket.model.person.Phone;
import seedu.socket.model.person.tag.Language;
import seedu.socket.model.person.tag.Tag;
import seedu.socket.model.project.*;

/**
 * A utility class to help with building EditProjectDescriptor objects.
 */
public class EditProjectDescriptorBuilder {

    private EditProjectDescriptor descriptor;

    public EditProjectDescriptorBuilder() {
        descriptor = new EditProjectDescriptor();
    }

    public EditProjectDescriptorBuilder(EditProjectDescriptor descriptor) {
        this.descriptor = new EditProjectDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditProjectDescriptor} with fields containing {@code project}'s details
     */
    public EditProjectDescriptorBuilder(Project project) {
        descriptor = new EditProjectDescriptor();
        descriptor.setName(project.getName());
        descriptor.setRepoHost(project.getRepoHost());
        descriptor.setRepoName(project.getRepoName());
        descriptor.setDeadline(project.getDeadline());
        descriptor.setMeeting(project.getMeeting());
    }

    /**
     * Sets the {@code Name} of the {@code EditProjectDescriptor} that we are building.
     */
    public EditProjectDescriptorBuilder withName(String name) {
        descriptor.setName(new ProjectName(name));
        return this;
    }

    /**
     * Sets the {@code ProjectRepoHost} of the {@code EditProjectDescriptor} that we are building.
     */
    public EditProjectDescriptorBuilder withRepoHost(String repoHost) {
        descriptor.setRepoHost(new ProjectRepoHost(repoHost));
        return this;
    }

    /**
     * Sets the {@code ProjectRepoName} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditProjectDescriptorBuilder withRepoName(String repoName) {
        descriptor.setRepoName(new ProjectRepoName(repoName));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditProjectDescriptor} that we are building.
     */
    public EditProjectDescriptorBuilder withDeadline(String deadline) {
        descriptor.setDeadline(new ProjectDeadline(deadline));
        return this;
    }

    /**
     * Sets the {@code ProjectMeeting} of the {@code EditProjectDescriptor} that we are building.
     */
    public EditProjectDescriptorBuilder withMeeting(String meeting) {
        descriptor.setMeeting(new ProjectMeeting(meeting));
        return this;
    }

    public EditProjectDescriptor build() {
        return descriptor;
    }
}
