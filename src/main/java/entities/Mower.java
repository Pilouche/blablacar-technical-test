package entities;

import java.awt.Point;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Mower {
	private Point position;
	public Direction direction;
	
	public Mower (Point position, Direction direction) {
		this.position = position;
		this.direction = direction;
	}
}
