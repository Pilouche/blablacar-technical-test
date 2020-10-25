package sceranios.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.junit.jupiter.api.Test;
import application.Launcher;
import entities.Direction;
import entities.Move;
import entities.Mower;

public class MultipleScenariosTest {
  public static class MowerEquator implements Equator<Mower> {
    @Override
    public boolean equate(Mower mower1, Mower mower2) {
      if (mower1.getPosition().equals(mower2.getPosition())
          && mower1.getDirection() == mower2.getDirection())
        return true;
      else
        return false;
    }

    @Override
    public int hash(Mower mower) {
      return 0;
    }
  }
  
  Equator<Mower> mowerEquator = new MowerEquator();
  
  @Test
  void simpleOneMower() {
    List<Mower> expectedMowers = Arrays.asList(Mower.builder().position(new Point(1, 3))
        .direction(Direction.N).instructions(new ArrayList<Move>()).build());
    List<Mower> finishedMowers = Launcher.runner("scenarios/simple_one_mower.txt");
    assertTrue(CollectionUtils.isEqualCollection(expectedMowers, finishedMowers, mowerEquator));
  }

  @Test
  void simpleTwoMowers() {
    List<Mower> expectedMowers = Arrays.asList(
        Mower.builder().position(new Point(1, 3)).direction(Direction.N)
            .instructions(Arrays.asList()).build(),
        Mower.builder().position(new Point(5, 1)).direction(Direction.E)
            .instructions(Arrays.asList()).build());
    List<Mower> finishedMowers = Launcher.runner("scenarios/simple_two_mowers.txt");
    assertTrue(CollectionUtils.isEqualCollection(expectedMowers, finishedMowers, mowerEquator));
  }

  @Test
  void oneMowerOutOfBounds() {
    List<Mower> expectedMowers = Arrays.asList(Mower.builder().position(new Point(1, 5))
        .direction(Direction.N).instructions(Arrays.asList()).build());
    List<Mower> finishedMowers = Launcher.runner("scenarios/one_mower_out_of_bounds.txt");
    assertTrue(CollectionUtils.isEqualCollection(expectedMowers, finishedMowers, mowerEquator));
  }

  @Test
  void simpleOneMowerOutOfBounds() {
    List<Mower> expectedMowers = Arrays.asList(
        Mower.builder().position(new Point(5, 5)).direction(Direction.N)
            .instructions(Arrays.asList()).build(),
        Mower.builder().position(new Point(0, 5)).direction(Direction.W)
            .instructions(Arrays.asList()).build());
    List<Mower> finishedMowers = Launcher.runner("scenarios/two_mowers_out_of_bounds.txt");
    assertTrue(CollectionUtils.isEqualCollection(expectedMowers, finishedMowers, mowerEquator));
  }
}
