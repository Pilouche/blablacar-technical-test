package application;

import java.io.FileNotFoundException;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import entities.Lawn;
import entities.Mower;
import file.loading.FileLoader;
import mower.manager.MowerMover;
import mower.manager.MowerRunner;

public class Launcher {
  public static void main(String[] fileNames) {
    if (fileNames.length == 0)
      throw new IllegalArgumentException("Please provide at least one file name.");

    for (String fileName : fileNames) {
      List<Mower> finishedMowers;
      try {
        finishedMowers = runner(fileName);
        finishedMowers.forEach(mower -> System.out.println(
            mower.getPosition().x + " " + mower.getPosition().y + " " + mower.getDirection()));
      } catch (FileNotFoundException e) {
        System.err.println("Error, file with name " + fileName + " was not found.");
      }
    }
  }

  public static List<Mower> runner(String fileName) throws FileNotFoundException {
    ImmutablePair<Lawn, List<Mower>> initialState = null;

    initialState = FileLoader.loadScenarios(fileName);

    Lawn lawn = initialState.getKey();
    List<Mower> mowersToRun = initialState.getValue();
    MowerMover mowerMover = MowerMover.builder().lawn(lawn).build();
    MowerRunner mowerRunner = MowerRunner.builder().mowersToRun(mowersToRun).build();

    return mowerRunner.runMowers(mowerMover);
  }
}
