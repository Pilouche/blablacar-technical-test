package entities;

public enum Direction {
  N, E, S, W;

  public static Direction nextRight(Direction nextDirection) {
    Direction[] values = Direction.values();
    for(int i = 0; i < Direction.values().length; i++) {
      if(values[i] == nextDirection) {
        if(i == Direction.values().length - 1)
          // that means we reached end of Enum so W
          nextDirection = Direction.N;
        else
          nextDirection = values[i+1];  
        break;
      }
    }
    return nextDirection;
  }
  
  public static Direction nextLeft(Direction nextDirection) {
    Direction[] values = Direction.values();
    for(int i = Direction.values().length - 1; i >= 0; i--) {
      if(values[i] == nextDirection) {
        if(i == 0)
          // that means we reached end of Enum so N
          nextDirection = Direction.W;
        else
          nextDirection = values[i-1]; 
        break;
      }
    }
    return nextDirection;
  }
}
