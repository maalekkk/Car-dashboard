package org.Logic;

/**
 * The class allow to catch the exception with bad use of cruise control in the app.
 */
public class CruiseControlException extends Exception {
    /**
     * Instantiates a new Cruise control exception.
     *
     * @param m the message
     */
    public CruiseControlException(String m) {
        super(m);
    }
}
