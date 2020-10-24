package application;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import entities.Direction;
import entities.Lawn;
import entities.Move;
import entities.Mower;
import file.loading.FileLoader;

public class Launcher {
  public static void main(String[] args) {
    FileLoader fileLoader = FileLoader.builder().fileName("scenarios/scenario1.txt").build();
    ImmutablePair<Lawn, List<Mower>> initialState;
    Lawn lawn;
    List<Mower> runningMowers = new ArrayList<Mower>();

    try {
      initialState = fileLoader.loadScenarios();
      lawn = initialState.getKey();
      runningMowers = initialState.getValue();
      System.out.println(lawn.toString());
      runningMowers.forEach(mower -> System.out.println(mower.toString()));
    } catch (FileNotFoundException e) {
      System.err.println("Error, file was not found : " + e.getMessage());
    }

    int maxMoves = 0;
    for (Mower mower : runningMowers) {
      if (mower.getInstructions().size() > maxMoves)
        maxMoves = mower.getInstructions().size();
    }

    List<Mower> finishedMowers = new ArrayList<Mower>();

    for (int i = 0; i < maxMoves; i++) {
      for (Mower mower : runningMowers) {
        Move instruction = mower.getInstructions().get(0);
        if (instruction == Move.F) {
          // the mower is moving forward
          nextPoint(mower.getPosition(), mower.getDirection());
        } else {
          // the mower is changing direction
          mower.setDirection(nextDirection(instruction, mower.getDirection()));
        }
        mower.getInstructions().remove(0);
        if (mower.getInstructions().size() == 0) {
          System.out.println("Finished at i : " + i + " | " + mower.toString());
          finishedMowers.add(mower);
        }
      }
      runningMowers.removeAll(finishedMowers);
    }
    System.out.println("");
    finishedMowers.forEach(m -> System.out.println(m.toString()));
  }

  private static Direction nextDirection(Move instruction, Direction currentDirection) {
    if (instruction == Move.R) {
      // the mower is turning right
      return Direction.nextRight(currentDirection);
    } else {
      // the mower is turning left
      return Direction.nextLeft(currentDirection);
    }
  }

  private static void nextPoint(Point currentPosition, Direction currentDirection) {
    switch (currentDirection) {
      case N:
        currentPosition.translate(0, 1);
        break;
      case E:
        currentPosition.translate(1, 0);
        break;
      case S:
        currentPosition.translate(0, -1);
        break;
      case W:
        currentPosition.translate(-1, 0);
        break;
    }
  }
}
