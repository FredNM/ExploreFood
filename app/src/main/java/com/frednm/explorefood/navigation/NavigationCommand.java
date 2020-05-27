package com.frednm.explorefood.navigation;

import androidx.navigation.NavDirections;

public class NavigationCommand {

    private NavigationCommand(){}

    public static final class To extends NavigationCommand {
        private final NavDirections directions;

        public To(NavDirections directions) {
            this.directions = directions;
        }

        public NavDirections getDirections() {
            return directions;
        }
        //TODO Ce doit être une data class, donc implémenter toString(), hashCode(), equals(), etc
        // Look https://medium.com/asos-techblog/kotlin-classes-in-java-world-365323843e6e

    }

    public static final class Back extends NavigationCommand {
        public Back() { }
    }

}





