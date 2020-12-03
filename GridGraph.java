package hw4.student;

import java.io.FileNotFoundException;

/**
 * Class to represent a Weighted Grid Graph. Methods are 
 * provided to build a graph from a file and to find 
 * shortest paths.
 * 
 */

public class GridGraph implements GridGraphInterface {

  /**
   * Default constructor 
   */
  public GridGraph( ) {
  }
  
  /**
   * Builds a grid graph from a specified file. It is assumed
   * that the input file is formatted correctly.
   * 
   * @param filename
   */
  public void buildGraph( String filename ) throws FileNotFoundException {
  }

  /**
   * Finds the shortest path between a source vertex and a target vertex
   * using Dijkstra's algorithm. 
   * @param s Source vertex (one based index)
   * @param t Target vertex (one based index) 
   * @param weighted Whether edge weights should be used or not.
   * @return A String encoding the shortest path. Vertices are
   *         separated by whitespace.  
   */
  public String findShortestPath( int s, int t, boolean weighted ) {
    return "";
  }

}
