//written by and08395 and swedz015

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HashTable <T> {
    private NGen<T>[] mainArray;

    public HashTable () {
        mainArray = (NGen<T>[]) new NGen[97]; //prime to improve performance of hash table
    }//HashTable constructor

    //Another constructor used to specify the size of the HashTable
    public HashTable (int num) {
        mainArray = (NGen<T>[]) new NGen[num];
    }//HashTable constructor

    //following method implements ideas from TextScan.java from Canvas (Course: CSCI 1933 Spring 2020)
    private String[] getFileArray(String filename) {
        Scanner scan = null;
        int count = 0;
        Scanner newScan = null;
        try { scan = new Scanner(new File(filename)); }
        catch (FileNotFoundException e) {
            System.out.println(filename +" not found.");
            return null;
        }
        while (scan.hasNext()) {
            scan.next();
            count++;
        }
        scan.close();
        String[] retVal = new String[count];
        count = 0;
        try { newScan = new Scanner(new File(filename)); }
        catch (FileNotFoundException e) { return null; }
        while (newScan.hasNext()) {
            retVal[count] = newScan.next();
            count++;
        }
        newScan.close();
        return retVal;
    }//getFileArray

    //Gets the index using the hash function
    //Checks if the string was already added
    //Puts the node in the front and makes it's next node the previous front
    public void add(T item){
        int index = hash(item); //hashes an index for a given item
        NGen<T> runner = mainArray[index];
        //chaining mechanism
        while (runner!= null){ //dupe checker
            if (runner.getData().equals(item)){
                return; //do nothing with the dupe
            }
            runner = runner.getNext();
        }
        NGen<T> temp = new NGen<T>(item, mainArray[index]); //adding item to array
        mainArray[index] = temp;
    }//add

    //same as add but uses specificHash instead
    public void specificAdd(T item){
        int index = specificHash(item);
        NGen<T> runner = mainArray[index];
        //chaining mechanism
        while (runner!= null){ //dupe checker
            if (runner.getData().equals(item)){
                return;
            }
            runner = runner.getNext();
        }
        NGen<T> temp = new NGen<T>(item, mainArray[index]);
        mainArray[index] = temp;
    }//specificAdd

    //Goes to each index and iterates through the linked list, printing the strings
    //Keeps track of how many elements at each index and if it's a max or min value it resets the max and min
    //Also keeps track of empty spots
    //Prints all that information
    public void display(){
        String output = "";
        int empty = 0; //variable for counting number of empty indexes in array
        int max = 0;
        int min = 100; //large number set for purpose of comparing against min number of collisions (generally 0)
        int count = 0; //counts nodes within
        for (int i = 0; i < mainArray.length; i++){
            count = 0; //resets for each new index in array
            NGen<T> runner = mainArray[i]; //node that traverse across array indexes and collisions within
            output += "At Key " + i + ": ";
            if (runner==null) { empty++; }
            if (runner!=null){
                while (runner != null){
                    count++;
                    output += runner.getData() + "   ";
                    runner = runner.getNext();
                }
            }
            if (count>max){ max = count; } //analysis
            else if (count < min) { min = count; }
            output += "\n";
        }
        System.out.println(output);
        System.out.println("Empty spaces: " + empty);
        System.out.println("Max: "+max +", Min: "+min);
    }//display

    //Uses hashRaw and mods it so that it has a index in the arrays range
    private int hash(T key){
        int retVal = hashRaw(key);
        retVal = Math.abs(retVal) % mainArray.length; //mods by length to ensure index within bounds
        return retVal;
    }//hash

    //Adds the unicode value for each character
    //returns the value
    private int hashRaw(T key){
        String k = (String) key; //casts key to String since key is type T
        int retVal = 0;
        for (int i = 0; i < k.length(); i++){
            retVal += Character.getNumericValue(k.charAt(i));
        }
        return retVal;
    }//hashRaw

    //Uses Java's string class hashcode method equation
    //Found in Java Documentation
    private int stringHash(T key) {
        String k = (String) key;
        int len = k.length();
        int hashCode = 0;
        for (int i = 0; i < len; i++) {
            int addVal = (int) (Character.getNumericValue(k.charAt(i))*Math.pow(31, len-i+1));
            hashCode += addVal;
        }
        return hashCode;
    }//stringHash

    //I just started plugging things in until it worked
    //So far I know it works on array size 347
    private int specificHash(T key){
        String k = (String) key;
        int hashCode = stringHash(key);
        int otherHash = hashRaw(key);
        hashCode = (hashCode/otherHash) + otherHash/k.length();
        hashCode = Math.abs(hashCode) % mainArray.length; //mods by length to ensure index within bounds
        return hashCode;
    }//specificHash

    //Used to test the HashTable functions
    //Creates a HashTable and adds the strings from the array returned from getFileArray
    //Prints out the HashTable
    public static void test(String filename) {
        HashTable<String> hash = new HashTable<String>();
        String[] data = hash.getFileArray(filename);
        if (data != null){
            for (int i = 0; i < data.length; i++){
                hash.add(data[i]);
            }
        }
        else{
            System.out.println("Data unable to be extracted, file does not exist.");
            return;
        }
        hash.display();
    }//test

    //Tests each file given
    //Uses specificAdd for the keywords file
    public static void main (String[] args){
        String file1 = "canterbury.txt";
        String file2 = "gettysburg.txt";
        String file3 = "proverbs.txt";
        String file4 = "that_bad.txt";
        String file5 = "keywords.txt";

        //calls test to handle files other than keywords.txt
        test(file1);
        test(file2);
        test(file3);
        test(file4);

        //specific array creation and test
        HashTable<String> hash2 = new HashTable<String>(347); //specific array for keywords.txt
        String[] data = hash2.getFileArray(file5);
        if (data != null){
            for (int i = 0; i < data.length; i++){
                hash2.specificAdd(data[i]);
            }
        }
        else{
            System.out.println("Data unable to be extracted, file does not exist.");
            return;
        }
        hash2.display();
    }//main
}//HashTable
