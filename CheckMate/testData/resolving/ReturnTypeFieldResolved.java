package resolving;

import base.SuperInterface;
import base.Thrower;

public class ReturnTypeFieldResolved {
    private static SuperInterface superInterface = new Thrower();

    public static SuperInterface getThrower() {
        return superInterface;
    }

    public static void main(String[] args) {
        <caret>getThrower().throwSuperInterface();
    }
}