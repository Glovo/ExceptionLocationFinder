package settings;


import settings.blacklist.BlackImpl;
import settings.blacklist.BlackSuper;

public class OverrideIgnored{
    public void test() {
        BlackSuper blackSuper = new BlackImpl();
        <caret>blackSuper.blackThrow();
    }
}