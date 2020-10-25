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

  public Point nextPosition(Point currentPosition, Direction currentDirection) {
    Point nextPosition = new Point(currentPosition);
    switch (currentDirection) {
      case N:
        if (!(nextPosition.getY() + 1 > lawn.getYDimension()))
          nextPosition.translate(0, 1);
        break;
      case E:
        if (!(nextPosition.getX() + 1 > lawn.getXDimension()))
          nextPosition.translate(1, 0);
        break;
      case S:
        if (!(nextPosition.getY() - 1 < 0))
          nextPosition.translate(0, -1);
        break;
      case W:
        if (!(nextPosition.getX() - 1 < 0))
          nextPosition.translate(-1, 0);
        break;
    }
    return nextPosition;
  }
}
