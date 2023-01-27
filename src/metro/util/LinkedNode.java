package metro.util;

/**
 * typical doubly linked node, reinventing the wheel for practice,
 * could just replace this by LinkedList
 * @param <T> the type that the node stores as the value
 */
public class LinkedNode<T>{
    private final T val;
    private LinkedNode<T> prev;
    private LinkedNode<T> next;

    public LinkedNode(){
        val=null;
    }
    public LinkedNode(T val) {
        this.val = val;
    }

    public T getVal() {
        return val;
    }

    public LinkedNode<T> getNext() {
        return next;
    }

    public LinkedNode<T> getPrev() {
        return prev;
    }

    public void setNext(LinkedNode<T> next) {
        this.next = next;
        if(next!=null) this.next.prev = this;    //setPrev() method outside of setNext() for flexibility?
    }

    public void removePrev(){
        this.prev = null;
    }

    @Override
    public String toString() {
        if(next!=null) {
            return val.toString() + " - " + next.toString();
        } else{
            return val.toString();
        }
    }
}