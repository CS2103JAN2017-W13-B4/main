package seedu.doist.testutil;

import seedu.doist.commons.exceptions.IllegalValueException;
import seedu.doist.model.tag.Tag;
import seedu.doist.model.tag.UniqueTagList;
import seedu.doist.model.task.Description;
import seedu.doist.model.task.Priority;

/**
 *
 */
public class TaskBuilder {

    private TestPerson person;

    public TaskBuilder() {
        this.person = new TestPerson();
    }

    /**
     * Initializes the TaskBuilder with the data of {@code personToCopy}.
     */
    public TaskBuilder(TestPerson personToCopy) {
        this.person = new TestPerson(personToCopy);
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.person.setName(new Description(name));
        return this;
    }

    public TaskBuilder withPriority(String priority) throws IllegalValueException {
        this.person.setPriority(new Priority(priority));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        person.setTags(new UniqueTagList());
        for (String tag: tags) {
            person.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TestPerson build() {
        return this.person;
    }

}