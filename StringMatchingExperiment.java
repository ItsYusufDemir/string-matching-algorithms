/* Authors: Eren Duyuk - 150120509
 *          Selin Aydın - 150120061
 *          Yusuf Demir - 150120032
 *
 * Date: 7.05.2023 14:27
 * 
 * Description: Analyzing three algorithms for string matching: Brute force algorithm, Horspool's algorithm and
 * Boyer-Moore algorithm.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;



public class StringMatchingExperiment {

    //GLOBAL VARIABLES
    static String pattern;
    static int sizeOfBadSymbol = 0;
    static int badSymbol[][];
    static int goodSuffix[];
    static long numberOfComparisons = 0;



    public static void main(String args[]) throws IOException {

        long beginMain = System.currentTimeMillis(); //This is for running time

        //Fetching the file into memory
        Path textPath = Path.of("2MBEnglish.html");
        ArrayList<String> text = new ArrayList<>(); //Since we do not know the number of lines in the file, we use ArrayList

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(textPath.toFile()));
            String line = reader.readLine();
            text.add(line);

            while (line != null) {
                line = reader.readLine();
                text.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        pattern = "together";

        ArrayList<Integer[]> indices; //The indices of all matches



        //BRUTE FORCE
        long beginBruteForce = System.currentTimeMillis();

        indices = bruteForceStringMathcing(text, pattern);
        printOutput(text, indices);

        long endBruteForce = System.currentTimeMillis();
        System.out.printf("\nTime elapsed for Brute Force: %d ms\n", endBruteForce-beginBruteForce);
        System.out.printf("Number of comparison for Brute Force: %d\n\n", numberOfComparisons);
        numberOfComparisons = 0;






        //HORSPOOL'S ALGORITHM
        long beginHorspool = System.currentTimeMillis();

        indices = horspoolsAlgorithm(text, pattern);
        printOutput(text, indices);

        long endHorspool = System.currentTimeMillis();
        System.out.printf("Time elapsed for Horspool's Algorithm: %d ms\n", endHorspool-beginHorspool);
        System.out.printf("Number of comparison for Horspool's Algorithm: %d\n\n", numberOfComparisons);
        numberOfComparisons = 0;





        //BOYER-MOORE ALGORITHM
        long beginBoyer = System.currentTimeMillis();

        indices = boyerMooreAlgorithm(text, pattern);
        printOutput(text, indices);

        long endBoyer = System.currentTimeMillis();
        System.out.printf("Time elapsed for Boyer-Moore Algorithm: %d ms\n", endBoyer-beginBoyer);
        System.out.printf("Number of comparison for Boyer-Moore Algorithm: %d\n\n", numberOfComparisons);
        numberOfComparisons = 0;





        long endMain = System.currentTimeMillis();
        System.out.printf("\nTime elapsed in total: %d ms\n", endMain-beginMain);


        //TEST FOR GOOD SUFFİX
       /* createGoodSuffixTable("GCAGAGAG");
        for(int i = 0; i < goodSuffix.length; i++) {
            System.out.println("goodSuffix[" + i + "] = " + goodSuffix[i]);
        }*/


    }





    //this method searchs for the pattern in the given text with brute force algorithm. Text is held in an arraylist line by line.
    public static ArrayList<Integer[]> bruteForceStringMathcing(ArrayList<String> text, String pattern) {

        ArrayList<Integer[]> indices = new ArrayList<>();   //creating arraylist to hold indices of the matching strings in the text
        Integer[] lineAndColumn; //holds line and column number for each matching strings
        String str = ""; //initializing
        int length = pattern.length(); //length variable holds length of the pattern
        char p[] = pattern.toCharArray(); //converting pattern from string to char array

        for (int i = 0; i < text.size() - 1; i++) {
            str = text.get(i); // str is equal to current line of the text
            char token[] = str.toCharArray(); //converting current line to char array

            for (int j = 0; j < token.length-length; j++) {
                int a = length-1; //initializing "a"

                while ((a >= 0) && (p[length - 1 - a] == token[j + length -1 - a]) && (numberOfComparisons++ >= 0)) { //in each match, we decrease the variable "a" to obtain whether we have complete match or not
                       a--;
                   }


                if (a == -1) {  //if pattern matches completely
                    lineAndColumn = new Integer[2]; //creating new array which holds line and column number of match
                    lineAndColumn[0] = i + 1; //line number
                    lineAndColumn[1] = j + 1 ; //column number
                    indices.add(lineAndColumn); //add array to arraylist
                }
            }
        }
        for (int i = 0; i < indices.size(); i++) {
            System.out.println("Brute force:" + indices.get(i)[0] + " " + indices.get(i)[1]);
        }

        return indices;  //returning arraylist named indices
    }




    //this method creates bad symbol table for the Hors-pool algorithm with the given pattern
    public static void createBadSymbolTable(String pattern) {
        String uniquePattern = uniqueElements(pattern); //calling the function unique elements to obtain exact elements that the pattern includes
        int size = uniquePattern.length(); //size of unique elements
        badSymbol = new int[size][2]; //creating 2 dimensional array badSymbol array(global variable)

        for (int j = uniquePattern.length() - 1; j >= 0; j--) {
            for (int i = pattern.length() - 2; i >= 0; i--) {
                if (uniquePattern.charAt(j) == pattern.charAt(i)) { //checks weather pattern matches with unique elements or not
                    badSymbol[j][0] = uniquePattern.charAt(j); //if it is, initialize first column with the matching char
                    badSymbol[j][1] = pattern.length() - 1 - i; //initialize second column with the distance from th rightmost side of the pattern
                    break; //break the loop when any match is first founded
                }
            }
            if (uniquePattern.charAt(j) == pattern.charAt(pattern.length() - 1) && (char)badSymbol[j][0] == 0) {
                //this is statement for the case that if the rightmost element occur only once(in the rightmost) and does not occur the rest of the pattern
                badSymbol[j][0] = uniquePattern.charAt(j); //initialize first column with the rightmost char
                badSymbol[j][1] = pattern.length() ; //initialize second column with the pattern length
            }
        }

        System.out.println("--------------------------------------");
        System.out.println("BadSymbol Table");
        for(int i = 0; i<size;i++){
            System.out.println((char)badSymbol[i][0] + " " + badSymbol[i][1]);

        }
        System.out.println("--------------------------------------");

    }





    //this method searchs for the pattern in the given text with hors-pool algorithm. Text is held in an arraylist line by line.
    public static ArrayList<Integer[]> horspoolsAlgorithm(ArrayList<String> text, String pattern){

        createBadSymbolTable(pattern); //calling the bad symbol table function
        ArrayList<Integer[]> indices = new ArrayList<>(); //creating arraylist to hold indices of the matching strings in the text
        Integer[] lineAndColumn ; //holds line and column number for each matching strings
        String str = ""; //initializing
        int length = pattern.length(); //length variable holds length of the pattern
        char p[] = pattern.toCharArray(); //converting pattern from string to char array

        for(int i = 0 ; i < text.size()-1; i++) {
            str = text.get(i); // str is equal to current line of the text
            char token[] = str.toCharArray(); //converting current line to char array

            for (int j = length - 1; j < token.length;j++ ) {
                int a = 0;
                while ((a < length) && (p[length - 1 - a] == token[j - a]) && numberOfComparisons++ >= 0) { //in each match, we increase the variable "a" to obtain whether we have complete match or not
                    a++;
                }
                if (a == length) { //if pattern matches completely
                    lineAndColumn = new Integer[2]; //creating new array which holds line and column number of match
                    lineAndColumn[0]= i+1; //line number
                    lineAndColumn[1]= j-length+2; //column number
                    indices.add(lineAndColumn); //add array to arraylist
                } else
                    for(int m = 0 ; m<badSymbol.length ; m++) {
                        if (token[j] == (char) badSymbol[m][0]) { //if the currents char is equal to any of the elements in bad symbol table
                            j += badSymbol[m][1]; //number of shift will be the corresponding integer in the table
                            j--; //decrease j by 1(we will increase j by 1 at the beginning of the loop)
                            break; //break the loop
                        }
                        if(m == badSymbol.length-1){  //if there is no match with the bad symbol table
                            j += length-1; //number of shift will be length(we will increase j by 1 at the beginning of the loop)
                        }
                    }

            }

        }
        for(int i = 0 ; i< indices.size() ; i++) {
            System.out.println("Horspool Algorithm " + indices.get(i)[0] + " " + indices.get(i)[1]);
        }
        return indices; //return arraylist indices
    }




    //this method is for founding the exact elements that the pattern has. We can think it as set data type's function
    public static String uniqueElements(String pattern) {
        String str = "" + pattern.charAt(pattern.length() - 1); //initializing str with the last elements of the pattern
        int m = str.length() - 1; // m is the size of str
        boolean unique = false; //initializing unique as false

        for (int i = pattern.length() - 2; i >= 0; i--) {
            for (int j = m; j >= 0; j--) {
                if (pattern.charAt(i) != str.charAt(j)) { //if none of the elements in pattern matches with str
                    unique = true; //means str does not include the current char in the pattern, therefore set unique to true
                }
                else{ //if any match is founded
                    unique =false; //set unique to false
                    break; //break the loop
                }
            }
            if (unique == true) { //if the current char of the pattern is unique
                str += pattern.charAt(i); //add the char to the str
                m++; //increasing size of str by 1
            }
        }
        return str; //returning str;
    }






    public static void createGoodSuffixTable(String pattern){
        goodSuffix = new int[pattern.length()];
        String temp = "";
        String reversedPattern = "";

        for (int i = pattern.length() - 1; i >= 0; i--) {
            reversedPattern += pattern.charAt(i);
        }

        for(int i = 0; i < pattern.length(); i++) {
            temp = pattern.charAt(i) + temp;
            for(int j = 0; j < temp.length(); j++) {
                if(reversedPattern.charAt(j) == temp.charAt(j)) {
                    goodSuffix[i] += 1;
                } else {
                    break;
                }
            }
        }

    }







    public static ArrayList<Integer[]> boyerMooreAlgorithm(ArrayList<String> text, String pattern){

        ArrayList<Integer[]> indices = new ArrayList<>();



        return indices;
    }


    public static void printOutput(ArrayList<String> text, ArrayList<Integer[]> indices) throws IOException {

        FileWriter writer = new FileWriter("output.html");






        writer.close();

    }














}
