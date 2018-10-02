/////////////////////////////////////////////////////////////////////
// LinkedMap.java - hash map maintains insertion order             //
//                                                                 //
// Author: Zhen Xia                                                //
/////////////////////////////////////////////////////////////////////

package dbServer;

import java.util.*;

/////////////////////////////////////////////////////////////////////
// LinkedMap: 
// maintains the insertion sequence. It was implemented
// by a hash map and a double linked list. Hash map recorded the
// key/value pair, double linked list maintained insertion sequence

public class LinkedMap implements Iterable<LinkedMap.Node>{
    private Map<String, Node> map;
    private Node head, tail;
    
    /* constructor */
    public LinkedMap() {
        map = new HashMap<>();
        head = new Node(null, -1, false);
        tail = new Node(null, -1, false);
        head.post = tail;
        tail.prv = head;
    }
    
    /* copy constructor */
    public LinkedMap(LinkedMap m) {
    		map = new HashMap<>();
        Node cur = head = new Node(null, -1, false);
        for (Node node : m) {
        		cur.post = new Node(node.key, node.value, node.isValid());
        		cur.post.prv = cur;
        		cur = cur.post;
        		map.put(cur.key, cur);
        }
        cur.post = tail = new Node(null, -1, false);
        tail.prv = cur;
    }
    
    /* return size of the this map */
    public int size() {
        return map.size();
    }
    
    /* return valid value or invalid value indicated by validRequired */
    public int get(String key, boolean validRequired) {
        if (!map.containsKey(key)) return -2; // -2 key not exist
        
        Node node = map.get(key);
        if (!node.valid && validRequired) return -1; // -1 means data not valid
        
        return node.value;
    }
    
    /* return a set of valid or invalid keys indicated by validRequired 
     * between time1 and time2 */
    public List<String> keyList(Integer time1, Integer time2, boolean validRequired) {
        List<String> keys = new LinkedList<>();
        
        for (Node node : this) {
        		if (validRequired && !node.isValid()) continue; // check data validation
        		
        		int t = node.getValue();
        		if (time1 != null && t < time1) continue;
        		if (time2 != null && t > time2) break;
        		
        		keys.add(node.getKey());
        }
        
        return keys;
    }
    
    /* add or update the key with value. Meanwhile, append with new 
     * time line and move the node to the tail */
    public void put(String key, int value) {
        Node node = map.get(key);
        
        if (node != null) {
        		// update the nodes with given key and value
            node.value = value;
            node.valid = true;
            moveToTail(node);
        } else {
        		// create a new node with given key and value
            Node newNode = new Node(key, value, true);
            addNode(newNode);
            map.put(key, newNode);
        }
    }

    /* invalidate the node for a given key */
    public boolean remove(String key) {
        if (!map.containsKey(key)) return false;
        
        map.get(key).valid = false;
        return true;
    }
    
    /* invalidate all nodes */
    public void clear() {
        Node cur = head.post;
        while (cur != tail) {
            cur.valid = false;
            cur = cur.post;
        }
    }

    /* append the node to the tail */
    private void addNode(Node node) {
        node.prv = tail.prv;
        node.post = tail;
        tail.prv.post = node;
        tail.prv = node;
    }

    /* remove node */
    private void removeNode(Node node) {
        Node prv = node.prv, post = node.post;
        prv.post = post;
        post.prv = prv;
    }
    
    /* move node to the tail */
    private void moveToTail(Node node) {
        this.removeNode(node);
        this.addNode(node);
    }
    
	/////////////////////////////////////////////////////////////////////
	// Node:
    // represent one entry of LinkedMap
    
    static class Node {
        private String key;
        private int value;
        private boolean valid; // whether this node is valid
        private Node prv, post;

        public Node(String k, int v, boolean vld) {
            key = k;
            value = v;
            valid = vld;
            prv = null;
            post = null;
        }
        
        public String getKey() {
        		return key;
        }
        
        public int getValue() {
            return value;
        }
        
        public boolean isValid() {
            return valid;
        }
    }
    
	/////////////////////////////////////////////////////////////////////
	// LinkedMapIterator:
	// iterator of LinkedMap
    
    
    public Iterator<LinkedMap.Node> iterator() {
        return new LinkedMapIterator();
    }
    
    private class LinkedMapIterator implements Iterator<LinkedMap.Node> {
        private Node next;
        
        public LinkedMapIterator() {
            next = head.post;
        }
        
        @Override
        public boolean hasNext() {
            return next != tail;
        }
        
        @Override
        public Node next() {
            Node tem = next;
            next = next.post;
            return tem;
        }
        
        @Override
        public void remove() { }
    }
}