package mower.manager;

import java.util.ArrayList;
import java.util.List;
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
    int maxMoves = findMaxMoves();

    for (int i = 0; i < maxMoves; i++) {
      for (Mower mower : mowersToRun) {
        Move instruction = mower.getInstructions().get(0);
        if (instruction == Move.F) {
          // the mower is moving forward
          mowerMover.nextPoint(mower.getPosition(), mower.getDirection());
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
