package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Suffix tree implementation.
 * @author Mitchell Rosen
 * @version 26-Apr-2013
 */
public class SuffixTree extends Observable {
   protected class Node {
      public List<Edge> edges;
      public Node() { edges = new ArrayList<Edge>(); }
      public void addEdge(Edge edge) { edges.add(edge); }
   }
   protected class InternalNode extends Node {}
   protected class LeafNode extends Node {
      public int val;
      public LeafNode(int val) {
         this.val = val;
      }
   }
   
   protected class Edge {
      int start, stop; // indexes into |string|
      Node node;
      
      public Edge(int start, int stop, Node node) {
         this.start = start;
         this.stop = stop;
         this.node = node;
      }
      
      /**
       * Creates a new edge to a leaf node with value |val|.
       */
      public Edge(int start, int stop, int val) {
         this.start = start;
         this.stop = stop;
         this.node = new LeafNode(val);
      }
   }
   
   protected String string;
   protected Node root;
   
   protected SuffixTree(String string) {
      this.string = string;
      root = new InternalNode();
   }
   
   public SuffixTree create(String string) {
      SuffixTree tree = new SuffixTree(string + "$");
      tree.fill();
      return tree;
   }
   
   /**
    * Fills this SuffixTree out with its |string|.
    */
   protected void fill() {
      root.addEdge(new Edge(0, string.length(), 0));
      for (int i = 1; i < string.length(); ++i)
         addNode(root, i);
   }
   
   /**
    * Adds a node with value |n| to node |node|, with edge string |string|.subString(n, end)
    */
   protected void addNode(Node node, int n) {
   }
}
