package base;

import base.other_package.OtherCustomUncheckedException;

import java.lang.RuntimeException;

public class Thrower extends SuperThrower implements SuperInterface, IgnoredSuperInterface {

    public Thrower() {
        throw new RuntimeException("ConstructorRuntime");
    }

    public void throwRuntime() {
        throw new RuntimeException("Runtime");
    }

    public void throwCustomUnChecked() {
        throw new CustomUncheckedException("CustomUnChecked");
    }

    public void throwOther() {
        throw new OtherCustomUncheckedException("OtherUnchecked");
    }

    public void throwCustomChecked() throws CustomCheckedException {
        throw new CustomCheckedException("CustomChecked");
    }

    public void construct() {
        new Thrower();
    }
    

    public void tryCatch() {
        try {
            throwRuntime();
        } catch (RuntimeException e) {
            //Caught Runtime
        }
    }

    public void superTryCatch() {
        try {
            throwCustomUnChecked();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void uncaughtTryCatch() {
        try {
            throwCustomUnChecked();
            throwOther();
        } catch (CustomUncheckedException e0) {
            e0.printStackTrace();
        }
    }

    public void throwBoth() {
        throwCustomUnChecked();
        throwOther();
    }

    public Thrower chainOne() {
        throw new CustomUncheckedException("ChainCustom1");
    }

    public Thrower chainTwo() {
        throw new RuntimeException("ChainRunTime2");
    }

    public int getIntWithRuntimeException() {
        throw new RuntimeException();
    }

    public boolean getBooleanWithRuntimeExceptions() {
        throw new RuntimeException();
    }

    /**
     * @throws CustomUncheckedException: l;kasjdflasjkasdlfkjsadlf
     *                                   alsdfjasldkjfl;aksj
     */
    public void commentedMethod() {
        return;
    }

    @Override
    public void throwSuperInterface() {
        throw new CustomUncheckedException("hehe");
    }

    public void superThrow() {
        super.superThrow();
        //nothing
    }

    public void generateStackOverflow() {
        generateStackOverflowOther();
    }

    public void generateStackOverflowOther() {
        generateStackOverflow();
    }

    @Override
    public void ignoredDefault() {
        throw new OtherCustomUncheckedException("hehe");
    }

    public void throwRepeated(){
        try {
            throwCustomUnChecked();
        } catch (RuntimeException e) {
            //nothing
        }
        throwCustomUnChecked();
    }
}
