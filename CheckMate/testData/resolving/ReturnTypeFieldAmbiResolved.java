package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.Thrower;

public class ReturnTypeFieldAmbiResolved {
    private static SuperInterface superInterface = null;

    public static SuperInterface getThrower(boolean random) {
        if (random)
            superInterface = new Thrower();
        else
            superInterface = new OtherThrower();
        return superInterface;
    }

    public static void main(String[] args) {
        <caret>getThrower(false).throwSuperInterface();
    }
}