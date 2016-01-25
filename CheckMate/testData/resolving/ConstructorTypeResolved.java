package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ConstructorTypeResolved{

    public static void main(String[] args) {
        <caret>new Thrower().throwSuperInterface();
    }
}