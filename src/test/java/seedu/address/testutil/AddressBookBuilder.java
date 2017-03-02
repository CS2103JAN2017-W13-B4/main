package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TodoList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniqueTaskList;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class AddressBookBuilder {

    private TodoList addressBook;

    public AddressBookBuilder(TodoList addressBook) {
        this.addressBook = addressBook;
    }

    public AddressBookBuilder withPerson(Person person) throws UniqueTaskList.DuplicatePersonException {
        addressBook.addPerson(person);
        return this;
    }

    public AddressBookBuilder withTag(String tagName) throws IllegalValueException {
        addressBook.addTag(new Tag(tagName));
        return this;
    }

    public TodoList build() {
        return addressBook;
    }
}
