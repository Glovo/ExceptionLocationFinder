package settings;


import settings.blacklist.BlackImpl;

public class ExceptionIgnored {
    public void test() {
        BlackImpl black = new BlackImpl();
        <caret>black.blackException();
    }
}