package mower.manager;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  public List<Mower> runMowers(MowerMover mowerMover) {
    List<Mower> finishedMowers = new ArrayList<Mower>();
    Set<Point> currentPositions = new HashSet<Point>();
    int maxMoves = findMaxMoves();

    for (int i = 0; i < maxMoves; i++) {
      for (Mower mower : mowersToRun) {
        Move instruction = mower.getInstructions().get(0);
        if (instruction == Move.F) {
          // the mower is moving forward
          Point nextPosition = mowerMover.nextPosition(mower.getPosition(), mower.getDirection());
          if (!currentPositions.contains(nextPosition)) {
            // the next position is free, the mower can move forward
            // we free the current position and move to the next one
            currentPositions.remove(mower.getPosition());
            mower.setPosition(nextPosition);
            currentPositions.add(nextPosition);
          } else {
            // the next position is not free or the bound is reached, mower can't move forward
            // current positions stay the same
            System.err.println("Mower is blocked by a bound or another mowner.");
          }
        } else {
          // the mower is changing direction
          mower.setDirection(mowerMover.nextDirection(instruction, mower.getDirection()));
        }
        mower.getInstructions().remove(0);
        if (mower.getInstructions().size() == 0) {
          // the mower doesn't have any instruction anymore
          System.out.println("Finished at i : " + i + " | " + mower.toString());
          finishedMowers.add(mower);
        }
      }
      mowersToRun.removeAll(finishedMowers);
    }
    return finishedMowers;
  }

  private int findMaxMoves() {
    int maxMoves = 0;
    for (Mower mower : mowersToRun) {
      if (mower.getInstructions().size() > maxMoves)
        maxMoves = mower.getInstructions().size();
    }
    return maxMoves;
  }

}
