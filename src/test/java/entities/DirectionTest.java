package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DirectionTest {
  @Test
  void nextRightFromSouth() {
    assertEquals(Direction.nextRight(Direction.S), Direction.W);
  }

  @Test
  void nextLeftFromSouth() {
    assertEquals(Direction.nextLeft(Direction.S), Direction.E);
  }

  @Test
  void nextRightFromNorth() {
    assertEquals(Direction.nextRight(Direction.W), Direction.N);
  }


  @Test
  void nextLeftFromNorth() {
    assertEquals(Direction.nextLeft(Direction.N), Direction.W);
  }
}
