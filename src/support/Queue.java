/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

/**
 *
 * @author ruchiranga
 */
public class Queue<T> {

    private Node<T> root;
    int count;

    public Queue(T value) {
        root = new Node<T>(value);
        count = 0;
    }
    
    public int getCount(){
        return count;
    }

    public void enque(T value) {
        Node<T> node = new Node<T>(value);
        node.setNext(root);
        root = node;
        count++;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public T deque() {

        Node<T> node = root;
        Node<T> previous = null;

        if(node == null){
            return null;
        }
        
        while (node.next() != null) {
            previous = node;
            node = node.next();
        }
        if (previous != null) {
            node = previous.next();
            previous.setNext(null);
            count--;
            return node.getValue();
        } else {
            return null;
        }

    }

    static class Node<T> {

        private T value;
        private Node<T> next;

        public Node(T value) {
            this.value = value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> next() {
            return next;
        }
    }
}
