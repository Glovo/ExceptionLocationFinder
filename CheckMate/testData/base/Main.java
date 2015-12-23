package base;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Main {
    private static Thrower thrower = new Thrower();

    public static void superInterface() {
        thrower.throwSuperInterface();
    }

    public static void superThrow() {
        thrower.superThrow();
    }

    public static void commentException() {
        thrower.commentedMethod();
    }

    public static void UncheckedFound() {
        thrower.throwRuntime();
    }

    public static void CustomFound() {
        thrower.throwCustomUnChecked();
        thrower.throwRuntime();
    }

    public static void CheckedIgnored() throws CustomCheckedException {
        thrower.throwCustomChecked();
    }

    public static void otherUnchecked() {
        new Thrower();
        thrower.throwOther();
    }

    public static void ConstructorFound() {
        thrower = new Thrower();
    }

    public static void ChainingDetected() {
        new Thrower();
        thrower.chainOne().chainTwo();
        thrower.throwCustomUnChecked();
        if (thrower.getBooleanWithRuntimeExceptions()) {
            thrower.throwCustomUnChecked();
            thrower.throwAssertion();
        }
        int i = thrower.getIntWithRuntimeException();
        try {
            thrower.throwRuntime();
        } catch (RuntimeException e) {
            //
        }
        throw new UncheckedIOException(new IOException());
    }


    public static void ClassDetection() {
        try {
            thrower.throwCustomUnChecked();
            thrower.throwRuntime();
        } catch (CustomUncheckedException e0) {
            e0.printStackTrace();
        }
    }

    public static void stackOverFlow() {
        thrower.generateStackOverflow();
    }


    public static void tryCatchNoResult() {
        thrower.tryCatch();
    }

    public static void tryCatchDeeperNoResult() {
        try {
            thrower.throwCustomUnChecked();
        } catch (CustomUncheckedException e) {
            //
        }
    }

    public static void tryCatchDisjunctionNoResult() {
        try {
            thrower.throwCustomUnChecked();
        } catch (UncheckedIOException | CustomUncheckedException e) {
            //
        }
    }

    public static void multiCatchNoResult() {
        try {
            thrower.throwCustomUnChecked();
        } catch (UncheckedIOException e) {
            int two = 1 + 1;
        } catch (CustomUncheckedException e) {
            //
        }
    }

    public static void superCatchNoResult() {
        try {
            thrower.throwCustomUnChecked();
        } catch (RuntimeException e) {
            //
        }
    }
}
