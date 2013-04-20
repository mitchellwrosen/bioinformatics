package controller;

import view.View;

/**
 * Main controller for running the application.
 * 
 * @author cstearns
 * @author Mitchell Rosen
 * @version 20-Apr-2013
 */
public class Main {
   public static void main(String[] args) {
      Controller c = new Controller();
      View v = new View(c);
      v.setVisible(true);
   }
}
