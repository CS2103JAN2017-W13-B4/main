package seedu.doist.commons.util;

import java.util.Stack;

public class History<S> {

    /**
     * history is the main stack that contains the past states
     * overflow acts as a buffer, when history is popped off but needs to be refilled
     */
    private Stack<S> history = new Stack<S>();
    private Stack<S> overflow = new Stack<S>();

    public History() {
        history = new Stack<S>();
        overflow = new Stack<S>();
    }


    /**
     * Method that pushes new state onto the top of the stack
     * @param state
     * Returns a boolean if successful
     */
    public boolean addToHistory(S state) {
        return history.add(state);
    }


    /**
     * Method that returns the previous state entered by user
     * Return null if history is empty
     */
    public S getPreviousState() {
        if (hasPreviousState()) {
            S previousState = history.pop();
            overflow.push(previousState);
            return previousState;
        } else {
            return null;
        }
    }

    boolean hasPreviousState() {
        return !history.isEmpty();
    }


    /**
     * Method that returns the next state entered by the user, if it exists
     * Returns null if overflow is empty
     */
    public S getNextState() {
        if (hasNextState()) {
            history.push(overflow.pop());
            return overflow.peek();
        } else {
            return null;
        }
    }

    public boolean hasNextState() {
        return !overflow.isEmpty();
    }
    /**
     * Move the current state and everything in the overflow stack
     * back into history stack
     */
    public void restore() {
        while (hasNextState()) {
            history.push(overflow.pop());
        }
    }

    /**
     * forget all the states in the overflow except the peek
     */
    public void forgetStatesAfter() {
        if (hasNextState()) {
            history.push(overflow.pop());
        }
        overflow.clear();
    }

    public boolean isAtMostRecentState() {
        return overflow.isEmpty();
    }
}

