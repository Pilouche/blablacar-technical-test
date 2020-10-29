package mower.manager;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import entities.Move;
import entities.Mower;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MowerRunner {
  private List<Mower> mowersToRun;

  Map<Point, Integer> currentPositions;

  public List<Mower> runMowers(MowerMover mowerMover) {
    mowersToRun.parallelStream().forEach(mower -> {
      int maxMoves = mower.getInstructions().size();
      for (int i = 0; i < maxMoves; i++) {
        Move instruction = mower.getInstructions().get(0);
        if (instruction == Move.F) {
          // the mower is moving forward
          Point currentPosition = mower.getPosition();
          Point nextPosition = mowerMover.nextPosition(currentPosition, mower.getDirection());
          if (nextPositionIsAvailable(currentPosition, nextPosition, mower.getId())) {
            mower.setPosition(nextPosition);
          }
        } else {
          // the mower is changing direction
          mower.setDirection(mowerMover.nextDirection(instruction, mower.getDirection()));
        }
        mower.getInstructions().remove(0);
      }
    });
    return mowersToRun;
  }

  private boolean nextPositionIsAvailable(Point currentPosition, Point nextPosition, int id) {
    Integer idInPosition = currentPositions.get(nextPosition);
    boolean nextPosAvailable = (idInPosition == null || idInPosition == id);
    if (nextPosAvailable) {
      // the next position is free, the mower can move forward so
      // we free the current position and add to the next one
      currentPositions.remove(currentPosition);
      currentPositions.put(nextPosition, id);
    }
    return nextPosAvailable;
  }
}
