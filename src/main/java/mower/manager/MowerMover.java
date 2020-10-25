package mower.manager;

import java.awt.Point;
import entities.Direction;
import entities.Lawn;
import entities.Move;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MowerMover {
  private Lawn lawn;

  public Direction nextDirection(Move instruction, Direction currentDirection) {
    if (instruction == Move.R) {
      // the mower is turning right
      return Direction.nextRight(currentDirection);
    } else {
      // the mower is turning left
      return Direction.nextLeft(currentDirection);
    }
  }

  public void nextPoint(Point currentPosition, Direction currentDirection) {
    String log = "Mower is blocked in direction "+ currentDirection;
    switch (currentDirection) {
      case N:
        if (currentPosition.getY() + 1 > lawn.getYDimension())
          System.out.println(log);
        else
          currentPosition.translate(0, 1);
        break;
      case E:
        if (currentPosition.getX() + 1 > lawn.getXDimension())
          System.out.println(log);
        else
          currentPosition.translate(1, 0);
        break;
      case S:
        if (currentPosition.getY() - 1 < 0)
          System.out.println(log);
        else
          currentPosition.translate(0, -1);
        break;
      case W:
        if (currentPosition.getX() - 1 < 0)
          System.out.println(log);
        else
          currentPosition.translate(-1, 0);
        break;
    }
  }
}
