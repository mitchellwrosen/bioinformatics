package model.suffixtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Super ugly inneficient suffix tree.
 * @author Cameron Stearns
 *
 */
public class SuffixTree {

   
   public static void main(String args[]) {
      SuffixTree tree = new SuffixTree("");
      Map<String, Integer> map = tree.findAllSubstrs();
      for(String n : map.keySet()) {
         if(map.get(n) > 10) 
         System.out.println(n + map.get(n));
      }
   }
   private String searchString;


   private Node rootNode = new Node(0);
   
   public SuffixTree(String searchString) {
      int count = 0;
      searchString += "$";
      this.searchString = searchString;

      for(int i = 0; i < searchString.length(); i++) {
         List<String> suffixes = new ArrayList<String>();
         for(int j = i; j < searchString.length(); j++) {
            suffixes.add(searchString.charAt(j) + "");
         }
         Node insertNode = search(rootNode, suffixes);
         for(String suffix : suffixes) {
            Node newNode = new Node(insertNode, suffix, ++count, insertNode.nodeDepth);
            insertNode.children.add(newNode);
            insertNode = newNode;
         }
      }
   }

   private Node search(Node startNode, List<String> suffixes) {
      while (true) {
         boolean repeat = false;
         for (Node child : startNode.children) {
            if (child.incomingSuffix.equals(suffixes.get(0))) {
               suffixes.remove(0);
               if (suffixes.isEmpty()) {
                  return child;
               }
               startNode = child;
               repeat = true;
               break;
            }
         }
         if (!repeat) {
            return startNode;
         }
      }
   }

   public void findAllSubstrs(Node startNode, Map<String, Integer> strings, String path) {
      for (Node n : startNode.children) {
         String pathToNode = path + n.incomingSuffix;
         int count = 0;
         if(pathToNode.contains("$")) {
            continue;
         }
         strings.put(pathToNode, getNumOcc(n, count));
         findAllSubstrs(n, strings,pathToNode);
      }
   }

   private int getNumOcc(Node startNode, int count) {
      for(Node n : startNode.children) {
         if(n.incomingSuffix.contains("$")) {
            count++;
         }
         count = getNumOcc(n, count);
      }
      return count;
   }

   public Map<String, Integer> findAllSubstrs() {
      Map<String, Integer> strings = new HashMap<String, Integer>();
      findAllSubstrs(rootNode, strings, "");
      return strings;
   }

}
