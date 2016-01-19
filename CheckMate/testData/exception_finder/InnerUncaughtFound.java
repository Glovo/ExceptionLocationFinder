package exception_finder;
import base.TestBase;

public class InnerUncaughtFound extends TestBase {
    public void testInnerUncaughtFound() {
        <caret>thrower.uncaughtTryCatch();
    }
}
