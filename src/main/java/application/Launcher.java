package application;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import entities.Lawn;
import entities.Mower;
import file.loading.FileLoader;
import mower.manager.MowerMover;
import mower.manager.MowerRunner;
import static java.util.stream.Collectors.toConcurrentMap;

public class Launcher {
  public static void main(String[] fileNames) {
    if (fileNames.length == 0)
      throw new IllegalArgumentException("Please provide at least one file name.");

    for (String fileName : fileNames) {
      List<Mower> finishedMowers;
      try {
        finishedMowers = runner(fileName);
        finishedMowers.sort(Comparator.comparing(Mower::getId));
        System.out.println("Result for " + fileName);
        finishedMowers.forEach(mower -> System.out.println(
            mower.getPosition().x + " " + mower.getPosition().y + " " + mower.getDirection()));
        System.out.println("-----");
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
    MowerRunner mowerRunner = MowerRunner.builder().mowersToRun(mowersToRun)
        .currentPositions(
            mowersToRun.stream().collect(toConcurrentMap(Mower::getPosition, Mower::getId)))
        .build();

    return mowerRunner.runMowers(mowerMover);
  }
}
