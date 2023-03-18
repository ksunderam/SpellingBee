import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters)
    {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate()
    {
        //helper method that recurses, and is able to take in these parameters
        recursiveGenerate("", letters);
    }
    public void recursiveGenerate(String combo, String leftover)
    {
        //every time this method is called, the combo, or first string if we're going by the example, is added to words
        words.add(combo);
        //base case: to stop when there are no more leftover letters, or second word (from hints), is just ""
        if (leftover.length() == 0)
        {
            return;
        }
        //for loop because what this does is recursively calling the method leftover length the number of times.
        //In the loop, I take whatever my parameters are, and I add the new amount.
        //for combo, I'm only adding one letter from the leftovers, and I iterate the number of times that we have
        //leftover chars, meaning that all leftover chars will each be separately added onto the combo.
        //the leftover is basically just whatever is left from after I its char to newCombo
        for (int i = 1; i < leftover.length()+1; i++)
        {
            String newCombo = combo + leftover.substring(i-1, i);
            String newLeftover = leftover.substring(0, i-1) + leftover.substring(i);
            recursiveGenerate(newCombo, newLeftover);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort()
    {
        //calls the helper method which returns the sorted arraylist, and we can just say that words is now equal
        //to this sorted ArrayList
        words = mergeSort(words, 0, words.size() - 1);
    }
    private ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high)
    {
        //Because we keep reducing our array cause we're dividing until the base case where the list has just 1 element,
        //we have the base case being this. This means that when the left index equals the rightmost index, we only have
        //one element
        if (high - low <= 0)
        {
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(arr.get(low));
            return newArr;
        }
        //just the average of low and high so that we can divide this arraylist into two separate arraylists
        int med = (high + low) / 2;
        ArrayList<String> arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = mergeSort(arr, med + 1, high);
        //call to merge which will combine these two lists in order
        return merge(arr1, arr2);
    }
    private ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2)
    {
        //this will be the sorted arraylist of words that we'll return
        ArrayList<String> merged = new ArrayList<String>();
        int i = 0;
        int j = 0;
        while(i < arr1.size() && j < arr2.size())
        {
            //if arr2 is greater (lexicographically) than arr1
            if (arr1.get(i).compareTo(arr2.get(j)) < 0)
            {
                //you add the lesser arr1 to merged, and then the next time you compare the same arr2 with the next
                //element in arr1. This keeps repeating until we get to just the leftover elements of the bigger array
                merged.add(arr1.get(i));
                i++;
            }
            //if arr1 is greater (lexicographically) than arr2, or they are equal (if they're equal it doesn't matter
            //which one is added to the merged list first)
            //the idea for adding is just the opposite of the if-statement
            else
            {
                merged.add(arr2.get(j));
                j++;
            }
        }
        //these two while loops just add on the remaining elements in the bigger arraylist (they'll be sorted cause its
        //recursively called from mergeSort)
        while(j < arr2.size())
        {
            //merged.set(i+j, arr2.get(j));
            merged.add(arr2.get(j));
            j++;
        }
        while(i < arr1.size())
        {
            //merged.set(i+j, arr1.get(i));
            merged.add(arr1.get(i));
            i++;
        }
        //returns sorted and merged list
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords()
    {
        //goes through all the sorted combinations we've generated from the input, to check whether it's in the dictionary
        for (int i = 0; i < words.size(); i++)
        {
            //if its not in the dictionary (which we find by doing binary search to see if our word matches with something
            //in the dictionary in a recursive boolean method, "found", which returns true if its in the dictionary)
            //we remove this combination from the ArrayList of words
            if (!found(words.get(i), 0, DICTIONARY.length - 1))
            {
                words.remove(i);
                //when you remove something, you also have to do i-- cause we have to account that the next element
                //will be at the same index as the precviously removed element
                i--;
            }
        }
    }
    //s is just the word that we're checking for in the dictionary, low and high are the boundaries of what we're looking at
    public boolean found(String s, int low, int high)
    {
        //this is the midpoint of the range that we're looking at
        int avg = (high+low)/2;
        //if there is a dictionary match for the word (meaning it equals something in the dictionary), return true:
        //it is found! (first two ifs are base cases)
        if (DICTIONARY[avg].equals(s))
        {
            return true;
        }
        //if our rightmost index has become equal to or less than our leftmost index, we return false:
        //we'll never find a match in the dictionary for this combo
        if (high - low <= 0)
        {
            return false;
        }
        //if s is lexicographically less than DICTIONARY[avg], then we make the high the average, therby cutting off
        //half of the words that we'd half to search for
        if (s.compareTo(DICTIONARY[avg]) < 0)
        {
            return found(s, low, avg);
        }
        //if s is lexicographically greater than DICTIONARY[avg], the low now moves up to the average.
        //we do the plus one, because we've already accounted for the avg, and it won't match
        else
        {
            return found(s, avg+1, high);
        }
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
