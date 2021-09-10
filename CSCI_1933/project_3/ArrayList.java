//written by and08395 and swedz015
public class ArrayList<T extends Comparable<T>> implements List<T> {

    private T[] array; //underlying array used in
    private boolean isSorted; //T if sorted alphabetically
    private int index; //starts at 0, index of item being currently added, also the size

    public ArrayList(){
        array = (T[]) new Comparable[2]; //ignore unchecked cast (UC)
        isSorted = true;
    }//ArrayList constructor

    private T[] increaseSize(){ //helper method for the add methods, returns new (larger) array
        T[] temp = (T[]) new Comparable[(array.length*2)+1]; //ignore UC
        for (int i = 0; i < index; i++){ //manual array copy
            temp[i] = array[i];
        }
        return temp;
    }//increaseSize

    public boolean add (T element){
        if (element == null){ //check for null element, since null cant be an element in the array
            return false;
        }
        if (index == array.length){//check if array is full
            array = increaseSize();//assigns larger array from increaseSize() to this array
        }
        array[index++] = element; //adds element and then increases the index for the next open spot
        isSorted = false;
        return true;
    }//add

    public boolean add (int index, T element){
        if ((element == null)||(index<0)||(index>=this.index)){//checking for false cases
            return false;
        }
        if (this.index == array.length){ //check if array is full
            array = increaseSize(); //assign this array to new large array
        }
        for (int i = this.index; i > index; i--){//shifting elements down
            array[i] = array[i-1];
        }
        array[index] = element; //add the element passed in to the desired index
        this.index++; //increase index since new element added
        isSorted = false;
        return true;
    }//add, specific index

    public void clear(){
        index = 0; //reset index to next open spot (now zero)
        T[] temp = (T[]) new Comparable[2]; //array similar to that made in the constructor
        array = temp; //assigns this array to new, reset array
    }//clear

    public boolean contains(T element){
        if (element == null){//check for null element, since null cant be an element in the array
            return false;
        }
        if (isSorted){
            for (int i = 0; i < index; i++){//manual search through array, more efficient
                if (element.compareTo(array[i]) < 0){ //stops if elements in array are larger than element being searched for
                    return false;
                }
                if (element.compareTo(array[i]) == 0){ //check if equal
                    return true;
                }
            }
        }
        for (int i = 0; i < index; i++){//manual search through array
            if (element.compareTo(array[i]) == 0){ //check if equal
                return true;
            }
        }
        return false;
    }//contains

    public T get(int index){
        if ((index < 0)||(index>this.index)){ //check for out-of-bounds
            return null;
        }
        return array[index];
    }//get

    public int indexOf(T element){
        if (element == null){ //check for null element, since null cant be an element in the array
            return -1;
        }
        if (isSorted){
            for (int i = 0; i < index; i++){//manual search through array, more efficient
                if (element.compareTo(array[i]) < 0){ //stops if elements in array are larger than element being searched for
                    return -1;
                }
                if (element.compareTo(array[i]) == 0){ //check if equal
                    return i;
                }
            }
        }
        for (int i = 0; i < index; i++){ //manual search through array
            if (array[i].compareTo(element)==0){ //check if equal
                return i;
            }
        }
        return -1; //no presence of element in array
    }//indexOf

    public boolean isEmpty(){
        if (index == 0){ //if index is zero, then the next avail. spot is 0, meaning there are no elements in the array
            return true;
        }
        return false;
    }//isEmpty

    public int lastIndexOf(T element){
        if (element == null){//check for null element, since null cant be an element in the array
            return -1;
        }
        if (isSorted){
            for (int i = index-1; i >= 0; i--){//manual search through array, more efficient
                if (element.compareTo(array[i]) > 0){ //stops if items in array are smaller than element
                    return -1;
                }
                if (element.compareTo(array[i]) == 0){ //check if equal
                    return i;
                }
            }
        }
        for (int i = index-1; i >= 0; i--){//manual search going backwards through array
            if (array[i].compareTo(element)==0){ //check if equal
                return i;
            }
        }
        return -1; //no presence of element in array
    }//lastIndexOf

    public T set(int index, T element){
        T temp;
        if ((index >= this.index)||(index<0)||(element == null)){ //checks for failing cases
            return null;
        }
        temp = array[index]; //rescue element that's being replaced
        array[index] = element;
        return temp;
    }//set

    public int size(){
        return index; //next available index is the same value as the # of elements in the array
    }//size

    public void sort(boolean order){ //doesn't sort if isSorted is T and order is T, would be a waste
        T temp;
        int i,j; //counter variables
        if ((order)&&(!isSorted)){ //sort alphabetically
            for (i = 1; i < index; i++){ //insertion sort
                temp = array[i];
                for (j = i-1; j >= 0 && temp.compareTo(array[j]) < 0; j--){
                    array[j+1] = array[j];
                }
                array[j+1] = temp;
            }
            isSorted = true; //update sorted so its known array is sorted alphabetically
        }
        else if (!order){ //sort reverse alphabetical
            for (i = 1; i < index; i++){ //insertion sort
                temp = array[i];
                for (j = i-1; j >= 0 && temp.compareTo(array[j]) > 0; j--){
                    array[j+1] = array[j];
                }
                array[j+1] = temp;
            }
            isSorted = false; //update to reflect that the array is not in increasing order
        }
    }//sort

    public boolean remove(T element){
        int flagIndex = -1; //variable used to track index of element (if applicable)
        for (int i = 0; i < index; i++){ //manual search through array
            if (array[i]==element){
                flagIndex = i; //index of element assigned
            }
        }
        if (flagIndex != -1){ //if still -1, then element is not in array
            array[flagIndex] = null;
            for (int i = flagIndex; i < index; i++){ //moving elements in front of index down to correct spots
                array[i] = array[i+1];
            }
            index--; //new index is now one less
            return true;
        }
        return false;
    }//remove

    public T remove(int index){
        T temp;
        if ((index<0)||(index>=this.index)){ //out of bounds check
            return null;
        }
        temp = array[index]; //used to hold element being removed
        array[index] = null;
        for (int i = index; i < this.index; i++){ //moving elements in front of index down to correct spots
            array[i] = array[i+1];
        }
        this.index--; //updates instance index so it reflects having one less element in array
        return temp;
    }//remove

    public String toString(){
        String output = "";
        for (int i = 0; i < index; i++){ //manual loop through array
            output += array[i];
            if (i < index-1){ //adds newline if it isn't the last element
                output += "\n";
            }
        }
        return output;
    }//toString

}//ArrayList class
