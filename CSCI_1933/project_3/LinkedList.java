//written by and08395 and swedz015
public class LinkedList<T extends Comparable<T>> implements List<T> {
    // Initialize the member variables
    private Node<T> head;
    private boolean isSorted;
    private int size;

    //Simple constructor
    public LinkedList() {
        head = null;
        isSorted = true;
        size = 0;
    }

    //Makes a bunch of checks before it interates from node to node until next = null and sets run's next node to the new node
    public boolean add(T element) { 
        if (element == null) return false;
        else if (head == null) head = new Node<T>(element);
        else {
            Node<T> run = head;
            Node<T> next = head.getNext();
            while (next != null) {
                run = run.getNext();
                next = next.getNext();
            }
            run.setNext(new Node<T>(element));
        }
        size++;
        if (size >= 2) isSorted = false;
        return true;
    }

    //Makes a bunch of checks. Index = 0 is a special case that doesn't work with the main function
    //Uses a for loop that iterates from node to node n number of times where n is the index chosen.
    //Once there is simply swaps a bunch of pointers to insert a new node
    public boolean add(int index, T element) {
        if (index > size-1 || index < 0 || element == null) return false;
        if (index == 0) {
            Node<T> set = new Node<T>(element, head);
            head = set;
            return true;
        }
        Node<T> run = head;
        Node<T> fol = head.getNext();
        for (int i = index; i > 1; i--) {
            run = run.getNext();
            fol = fol.getNext();
        }
        Node<T> set = new Node<T>(element, fol);
        run.setNext(set);
        size++;
        if (size >= 2) isSorted = false;
        return true;
    }

    //Simply resets all the member variables.
    //Java's garbage collector takes care of the rest
    public void clear() {
        head = null;
        size = 0;
        isSorted = true;
    }

    //Uses a while loop to go from node to node and checks if the node data matches the desired element
    //When the while loops stops, it's either because the node with the right element was found or it reached the end of the list
    //It checks if it reached the end of the list and returns false if true and true if false
    public boolean contains(T element) {
        Node<T> next = head;
        while (next != null && !next.getData().equals(element)) {
            next = next.getNext();
        }
        if (next == null) return false;
        else return true;
    }

    
    //Uses a for loop using the desired index and goes from node to node index number of times until the correct index
    public T get(int index) {
        if (index > size-1 || index < 0) return null;
        else {
            Node<T> temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.getNext();
            }
            return temp.getData();
        }
    }

    //Very similar to contains but now it keeps track of how many times it's iterated
    public int indexOf(T element) {
        Node<T> next = head;
        int ind = 0;
        while (next != null && !next.getData().equals(element)) {
            next = next.getNext();
            ind++;
        }
        if (next == null) return -1;
        else return ind;
    }

    //Simply checks if head points to null. Easy and simple
    public boolean isEmpty() {
        if (head == null) return true;
        return false;
    }

    //This interates through the entire list, if it finds another element it makes the ind that number in the for loop
    //The ith number of the for loop is also the index of the current node that run points too
    //ind keeps track of whenever the node's data matches the element
    //As it iterates from start to finish, ind will reflect the most recent node found that matches the desired element
    public int lastIndexOf(T element) {
        int ind = -1;
        Node<T> run = head;
        for (int i = 0; i < size; i++) {
           if (run.getData().equals(element)) ind = i;
           run = run.getNext(); 
        }
        return ind;
    }

    //Very similar to get except it now changes the value of run when it reaches the correct index
    public T set(int index, T element) {
        if (index > size-1 || index < 0 || element == null) return null;
        Node<T> run = head;
        T retVal;
        for (int i = 0; i < index; i++) {
            run = run.getNext();
        }
        retVal = run.getData();
        run.setData(element);
        return retVal;
    }

    //Getter method for the size memeber variable
    public int size() {
        return size;
    }

    //Bubble sort
    //Simply switches the data values instead of switching the nodes
    //The ord changes the value that compareTo returns to either positive or negative
    //This effectively changes how the values are sorted.
    public void sort(boolean order) {
        int ord;
        if (order) {
            ord = 1;
            isSorted = true;
        } else {
            ord = -1;
            isSorted = false;
        } 
        for (int i = size-1; i > 0; i--) {
            Node<T> run = head;
            Node<T> fol = head.getNext();
            for (int j = 0; j < i; j++) {
                if (run.getData().compareTo(fol.getData())*ord > 0) {
                    T temp = run.getData();
                    run.setData(fol.getData());
                    fol.setData(temp);
                }
                run = run.getNext();
                fol = fol.getNext();
            }
        }
    }

    public boolean remove(T element) {
        if (size == 0 || element == null) return false;
        if (head.getData().equals(element)) {
            head = head.getNext() ;
            size--;
            return true;
        }
        Node<T> run = head;
        Node<T> fol = run.getNext();
        while (fol != null && !fol.getData().equals(element)) {
            run = run.getNext();
            fol = fol.getNext();
        }
        if (fol == null) return false;
        else {
            run.setNext(fol.getNext());
            size--;
            return true;
        }
    }

    //Makes some preliminary checks
    //Index == 0 is a special case and that's reflected
    //It goes through index number of times and removes fol.
    //This is why index = 0 is a special case. fol will never be the head index
    //It removes fol by setting run's next node to fol's next node.
    //fol's data is recorded before it's removed and that's the data that's returned
    public T remove(int index) {
        if (index > size-1 || index < 0) return null;
        if (index == 0) {
            T retVal = head.getData();
            head = head.getNext();
            size--;
            return retVal;
        }
        Node<T> run = head;
        Node<T> fol = head.getNext();
        for (int i = 0; i < index-1; i++) {
            run = run.getNext();
            fol = fol.getNext();
        }
        T retVal = fol.getData();
        run.setNext(fol.getNext());
        size--;
        return retVal;
    }
    
    //Simply interates through the list and prints out each node
    public String toString() {
        Node<T> run = head;
        String string = "";
        for (int i = 0; i < size; i++) {
            string += run.getData() + "\n";
            run = run.getNext();
        }
        return string;
    }
}
