package application;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import entities.Lawn;
import entities.Mower;
import file.loading.FileLoader;

public class Launcher {
  public static void main(String[] args) {
    FileLoader fileLoader = FileLoader.builder().fileName("scenarios/scenario1.txt").build();
    ImmutablePair<Lawn, List<Mower>> initialState;
    Lawn lawn;
    List<Mower> mowers = new ArrayList<Mower>();

    try {
      initialState = fileLoader.loadScenarios();
      lawn = initialState.getKey();
      mowers = initialState.getValue();
      System.out.println(lawn.toString());
      mowers.forEach(mower -> System.out.println(mower.toString()));
    } catch (FileNotFoundException e) {
      System.err.println("Error, file was not found : " + e.getMessage());
    }
    
    int maxMoves = 0;
    for(Mower mower : mowers) {
      if(mower.getInstructions().size() > maxMoves)
        maxMoves = mower.getInstructions().size();
    }
    
    for(int i = 0; i < maxMoves; i++) {
      //do something smart
    }
  }
}
