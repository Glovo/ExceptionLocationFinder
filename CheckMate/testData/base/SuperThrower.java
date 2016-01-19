package base;

public class SuperThrower {
    public void superThrow() {
        throw new CustomUncheckedException("SuperThrow");
    }

    public void throwRuntime() {
        //overridden by thrower
    }

    public void throwCustomUnChecked() {
        //overridden by thrower
    }
}
