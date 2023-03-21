import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Dictionary;
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

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        g("", letters);
    }

    public void g(String word, String letters)
    {
        if (letters.length() == 0)
        {
            words.add(word);
            return;
        }
        for (int i = 0; i < letters.length(); i++)
        {
            String w = word + letters.charAt(i);
            String l = letters.substring(0, i) + letters.substring(i + 1);
            g( w, l);
        }
        words.add(word);
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort()
    {
        s(words);
    }

    public ArrayList<String> s(ArrayList<String> arr)
    {
        if (arr.size() <= 1)
        {
            return arr;
        }

        ArrayList<String> a = new ArrayList<String>();
        ArrayList<String> b = new ArrayList<String>();
        int mp = arr.size()/2;
        for (int i = 0; i < arr.size(); i++)
        {
            if (i <= mp)
            {
                a.add(arr.remove(i));
            }
            else
            {
                b.add(arr.remove(i));
            }
        }
        a = s(a);
        b = s(b);

        int counter = 0;
        while(!a.isEmpty() || !b.isEmpty())
        {
            if (a.isEmpty())
            {
                arr.add(b.remove(0));
            }
            else if (b.isEmpty())
            {
                arr.add(a.remove(0));
            }
            else if (a.get(0).compareTo(b.get(0)) < 0)
            {
                arr.add(a.remove(0));
            }
            else
            {
                arr.add(b.remove(0));
            }
        }
        return arr;
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
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++)
        {
            int s = 0;
            int e = DICTIONARY_SIZE;
            String str = words.get(i);
            int mp = DICTIONARY_SIZE / 2;

            while(s != e)
            {
                if(str.compareTo(DICTIONARY[mp]) < 0)
                {
                    e = mp - 1;
                }
                else if (str.equals(DICTIONARY[mp]))
                {
                    break;
                }
                else
                {
                    s = mp + 1;
                }
                mp = (e - s)/2 + s;
            }
            if (!DICTIONARY[mp].equals(str))
            {
                words.remove(i);
                i--;
            }
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
