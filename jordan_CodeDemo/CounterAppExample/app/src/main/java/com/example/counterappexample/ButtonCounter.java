package com.example.counterappexample;

/*
* **Recommendation 05 OBJ51-J Minimize the accessibility of classes and their members**
*
* Giving classes and their members the minimum possible access gives malicious code
* the least opportunity to compromise security
*
* NON-COMPLIANT SOLUTION
* Leaving the class as public is non-compliant
*/

// COMPLIANT SOLUTION
// Defining the class as package private and final is compliant, and allows methods to remain public
final class ButtonCounter {

    /*
    **Rule 24 OBJ01-J Limit Accessibility of fields**

    Fields must be declared private or package-private
    Attackers can manipulate public non-final fields or final fields referencing mutable objects

    NON-COMPLIANT SOLUTION
    public int count = 0;
     */

    // COMPLIANT SOLUTION
    // Declaring the field as private stops attackers
    private int count;
    /*
    End of OBJ01-J
    */



    /*
    **Rule 24 OBJ11-J Be wary of letting constructors throw exceptions**

    If the constructor has begun, but not finished, the object is partially initialized
    Attackers can maliciously obtain the instance of the object

    NON-COMPLIANT SOLUTION
    public ButtonCounter(int startingAmount) {
        if (startingAmount < 0) {
            throw new IllegalArgumentException("Count cannot be initialized below zero!");
        }

        count = startingAmount;
    }
    */

    // COMPLIANT SOLUTION
    ButtonCounter(int startingAmount) {
        if (startingAmount < 0) {
            throw new IllegalArgumentException("Count cannot be initialized below zero!");
        }
        count = startingAmount;
    }
    // By declaring the class's own final finalize() method, attackers will be thwarted
    public final void finalize() {
        // Do nothing
    }
    /*
    End of OBJ11-J
    */



    /*
    **Rule 24 OBJ07-J Sensitive classes must not let themselves be copied**

    Not defining copy mechanism is insufficient to prevent the class from being copied
    Attackers manufacture new instances of a class by copying memory images of existing objects

    NON-COMPLIANT SOLUTION
    By leaving the class as is (with no copy mechanisms) the class can still be copied
    */

    // COMPLIANT SOLUTION
    // Defining a final clone() method that always fails will prevent an instance from being copied
    public final ButtonCounter clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    /*
    End of OBJ07-J
    */



    /*
    **Rule 22 MET00-J Validate Method Arguments**

    Validate method arguments to ensure they are within the bounds of the intended design
    Otherwise methods can result in wrong calculations, exceptions, and inconsistent state

    // NON-COMPLIANT SOLUTION
    public void increaseCount(int count){
        count += count;
    }
    */

    // COMPLIANT SOLUTION
    public void increaseCount(int amount) {
        // Prevents the count from being increased by a negative number
        if (amount < 0) {
            return;
        }
        count += amount;
    }
    /*
    End of MET00-J
    */



    /*
    **Rule 11 ERR07-J Do not throw RuntimeException, Exception, or Throwable**

    Throwing these exceptions prevents classes from catching intended exceptions
    without catching other unintended exceptions as well

    // NON-COMPLIANT SOLUTION
    public void decreaseCount(int amount) {
        if (amount < 0) {
            return;
        }
        if (count - amount < 0) {
            throw new RuntimeException("Count cannot decrease below zero!");
        }
        count -= amount;
    }
    */

    // COMPLIANT SOLUTION
    public void decreaseCount(int amount) {
        if (amount < 0) {
            return;
        }
        if (count - amount < 0) {
            throw new IllegalArgumentException("Count cannot decrease below zero!");
        }
        count -= amount;
    }
    /*
    End of ERR07-J
    */



    /*
    **Recommendation 05 OBJ52-J Write garbage-collection-friendly code**

    Use short-lived immutable objects, avoid large objects, and do not invoke the garbage collector

    // NON-COMPLIANT SOLUTION
    // Invoking the garbage collector can degrade performance
    private void cleanup() {
        System.gc();
    */

    // COMPLIANT SOLUTION
    // This class is a good example of a garbage-collector friendly class
    /*
    End of OBJ52-J
    */



    public int getCount() {
        return count;
    }

}
