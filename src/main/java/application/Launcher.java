package application;

import java.awt.Point;

import entities.Direction;
import entities.Mower;

public class Launcher {
    public static void main( String[] args )
    {
    	Mower mower = new Mower(new Point(), Direction.EAST);
    	System.out.println(mower.toString());
        System.out.println( "Hello World!" );
    }
}
