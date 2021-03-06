package file.loading;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import entities.Direction;
import entities.Lawn;
import entities.Move;
import entities.Mower;

public class FileLoader {
  public static ImmutablePair<Lawn, List<Mower>> loadScenarios(String fileName)
      throws FileNotFoundException {
    List<Mower> mowers = new ArrayList<Mower>();

    InputStream inputFileStream = FileLoader.class.getClassLoader().getResourceAsStream(fileName);

    if (inputFileStream == null)
      throw new FileNotFoundException();

    Scanner scanner = new Scanner(inputFileStream);

    Lawn lawn = Lawn.builder().xDimension(scanner.nextInt()).yDimension(scanner.nextInt()).build();

    // skipping the \n after the last digit
    scanner.nextLine();

    int id = 1;

    while (scanner.hasNext()) {
      String[] mowerParameters = scanner.nextLine().split(" ");
      String[] mowerInstructions = scanner.nextLine().split("");

      mowers.add(Mower.builder().id(id++)
          .position(
              new Point(Integer.parseInt(mowerParameters[0]), Integer.parseInt(mowerParameters[1])))
          .direction(Direction.valueOf(mowerParameters[2])).instructions(Arrays
              .asList(mowerInstructions).stream().map(Move::valueOf).collect(Collectors.toList()))
          .build());
    }
    scanner.close();
    return new ImmutablePair<Lawn, List<Mower>>(lawn, mowers);
  }
}
