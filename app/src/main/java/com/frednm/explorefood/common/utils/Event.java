package com.frednm.explorefood.common.utils;


/**
 * Used as a wrapper for data that are exposed via a LiveData that represents an event.
 * see https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
public class Event<T> {

    private Boolean hasBeenHandled = false;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    /**
     * Returns the content and prevents its use again.
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    public T peekContent() {
        return content;
    }

    /**
     * Utils function, just to be able to handle declaration site-variance if needed.
     * Covariance not allowed in generic classes, and better to use wildcard as esplained in the following link (french)
     * See https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/22404-apprehendez-la-genericite-en-java
     * See also
     * See https://kotlinlang.org/docs/reference/generics.html
     * See http://blog.vavr.io/declaration-site-variance-in-a-future-java/
     */
    static <T> Event<T> narrow(Event<? extends T> event) {
        return (Event<T>) event;
    }
}
