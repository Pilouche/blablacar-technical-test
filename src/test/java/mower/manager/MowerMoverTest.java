package mower.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.awt.Point;
import org.junit.jupiter.api.Test;
import entities.Direction;
import entities.Lawn;
import entities.Move;

public class MowerMoverTest {
  MowerMover mowerMover =
      MowerMover.builder().lawn(Lawn.builder().xDimension(5).yDimension(5).build()).build();

  @Test
  void nextRightFromSouth() {
    assertEquals(Direction.W, mowerMover.nextDirection(Move.R, Direction.S));
  }

  @Test
  void nextLeftFromSouth() {
    assertEquals(Direction.E, mowerMover.nextDirection(Move.L, Direction.S));
  }

  @Test
  void nextRightFromNorth() {
    assertEquals(Direction.N, mowerMover.nextDirection(Move.R, Direction.W));
  }


  @Test
  void nextLeftFromNorth() {
    assertEquals(Direction.W, mowerMover.nextDirection(Move.L, Direction.N));
  }
  
  @Test
  void nextPositionNorthSimple() {
    Point position = new Point(2,2);
    Point expectedPosition = new Point(2, 3);
    assertEquals(expectedPosition, mowerMover.nextPosition(position, Direction.N));
  }
  
  @Test
  void nextPositionEastSimple() {
    Point position = new Point(2,2);
    Point expectedPosition = new Point(3, 2);
    assertEquals(expectedPosition, mowerMover.nextPosition(position, Direction.E));
  }
  
  @Test
  void nextPositionSouthSimple() {
    Point position = new Point(2,2);
    Point expectedPosition = new Point(2, 1);
    assertEquals(expectedPosition, mowerMover.nextPosition(position, Direction.S));
  }
  
  @Test
  void nextPositionWestSimple() {
    Point position = new Point(2,2);
    Point expectedPosition = new Point(1, 2);
    assertEquals(expectedPosition, mowerMover.nextPosition(position, Direction.W));
  }
  
  @Test
  void nextPositionHittingNorthBound() {
    Point position = new Point(5,5);
    assertEquals(position, mowerMover.nextPosition(position, Direction.N));
  }
  
  @Test
  void nextPositionHittingEastBound() {
    Point position = new Point(5,5);
    assertEquals(position, mowerMover.nextPosition(position, Direction.E));
  }
  
  @Test
  void nextPositionHittingSouthBound() {
    Point position = new Point(0,0);
    assertEquals(position, mowerMover.nextPosition(position, Direction.S));
  }
  
  @Test
  void nextPositionHittingWestBound() {
    Point position = new Point(0,0);
    assertEquals(position, mowerMover.nextPosition(position, Direction.W));
  }
}
