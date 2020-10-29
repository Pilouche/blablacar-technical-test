package entities;

import java.awt.Point;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Mower {
  private int id;
  private Point position;
  private Direction direction;
  private List<Move> instructions;
}
