package settings;

import settings.blacklist.BlackClass;

public class ClassIgnored {
    public void test() {
        <caret>new BlackClass().throwBlack();
    }
}