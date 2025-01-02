package project.model.util;

import project.model.movement.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static final String ILLEGAL_MOVE_MESSAGE_TEMPLATE = "%s is not legal move specification";

    private static String createIllegalMoveMessage(String illegalOption) {
        return String.format(OptionsParser.ILLEGAL_MOVE_MESSAGE_TEMPLATE, illegalOption);
    }

    public static List<MoveDirection> parseStringArray (String[] options) {
        ArrayList<MoveDirection> directions = new ArrayList<>();

        for (var option : options) {
            switch (option) {
                case "f", "forward" -> directions.add(MoveDirection.FORWARD);
                case "b", "backward" -> directions.add(MoveDirection.BACKWARD);
                case "l", "left" -> directions.add(MoveDirection.LEFT);
                case "r", "right" -> directions.add(MoveDirection.RIGHT);
                default -> throw new IllegalArgumentException(OptionsParser.createIllegalMoveMessage(option));
            }
        }

        return directions;
    }
}
