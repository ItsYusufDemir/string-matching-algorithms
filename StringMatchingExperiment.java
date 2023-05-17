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
    static  String textName;



    public static void main(String args[]) throws IOException {

        long beginMain = System.currentTimeMillis(); //This is for running time

        //Fetching the file into memory
        textName = "English_Sample_1";
        Path textPath = Path.of(textName + ".html");
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



        

        pattern = "the most";


        ArrayList<Integer[]> indices; //The indices of all matches



        //BRUTE FORCE
        long beginBruteForce = System.currentTimeMillis();

        indices = bruteForceStringMathcing(text, pattern);
        long endBruteForce = System.currentTimeMillis();
        printOutput(text, indices);


        System.out.printf("\nTime elapsed for Brute Force: %d ms\n", endBruteForce-beginBruteForce);
        System.out.printf("Number of comparison for Brute Force: %d\n\n", numberOfComparisons);
        numberOfComparisons = 0;






        //HORSPOOL'S ALGORITHM
        long beginHorspool = System.currentTimeMillis();


        indices = horspoolsAlgorithm(text, pattern);
        long endHorspool = System.currentTimeMillis();
        printOutput(text, indices);


        System.out.printf("Time elapsed for Horspool's Algorithm: %d ms\n", endHorspool-beginHorspool);
        System.out.printf("Number of comparison for Horspool's Algorithm: %d\n\n", numberOfComparisons);
        numberOfComparisons = 0;





        //BOYER-MOORE ALGORITHM
        long beginBoyer = System.currentTimeMillis();

        indices = boyerMooreAlgorithm(text, pattern);
        long endBoyer = System.currentTimeMillis();
        printOutput(text, indices);

        System.out.printf("Time elapsed for Boyer-Moore Algorithm: %d ms\n", endBoyer-beginBoyer);
        System.out.printf("Number of comparison for Boyer-Moore Algorithm: %d\n\n", numberOfComparisons);
        numberOfComparisons = 0;





        long endMain = System.currentTimeMillis();
        System.out.printf("\nTime elapsed in total: %d ms\n", endMain-beginMain);

    }




    //this method searchs for the pattern in the given text with brute force algorithm. Text is held in an arraylist line by line.
    public static ArrayList<Integer[]> bruteForceStringMathcing(ArrayList<String> text, String pattern) {

        ArrayList<Integer[]> indices = new ArrayList<>();   //creating arraylist to hold indices of the matching strings in the text
        Integer[] lineAndColumn; //holds line and column number for each matching strings
        String str = ""; //initializing
        int length = pattern.length(); //length variable holds length of the pattern

        for (int i = 0; i < text.size() - 1; i++) {
            str = text.get(i); // str is equal to current line of the text

            for (int j = 0; j < str.length() - length; ) {
                int a = length - 1; //initializing "a"

                if (str.charAt(j) == '<') { //if contains "<" sign
                    j++;
                    while (str.charAt(j) != '>') { //search for ">" sign
                        j++; //increase j by 1
                    }

                }

                if (str.charAt(j) != '<') {  //if it is not "<" sign
                    while ((a >= 0) && (pattern.charAt(length - 1 - a) == str.charAt(j + length - 1 - a)) && (numberOfComparisons++ >= 0)) { //in each match, we decrease the variable "a" to obtain whether we have complete match or not
                        a--;
                    }

                    if (a == -1) {  //if pattern matches completely
                        lineAndColumn = new Integer[2]; //creating new array which holds line and column number of match
                        lineAndColumn[0] = i + 1; //line number
                        lineAndColumn[1] = j + 1; //column number
                        indices.add(lineAndColumn); //add array to arraylist
                        j++;

                    }
                    else{
                        j++;
                    }

                }

            }
        }

        /*
        for (int i = 0; i < indices.size(); i++) {
            System.out.println("Brute force:" + indices.get(i)[0] + " " + indices.get(i)[1]);
        }

         */

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
        System.out.println("Others: " + pattern.length());
        System.out.println("--------------------------------------");

    }






    //this method searchs for the pattern in the given text with hors-pool algorithm. Text is held in an arraylist line by line.
    public static ArrayList<Integer[]> horspoolsAlgorithm(ArrayList<String> text, String pattern){

        String sub = ""; //initializing sub which holds to substring
        createBadSymbolTable(pattern); //calling the bad symbol table function
        ArrayList<Integer[]> indices = new ArrayList<>(); //creating arraylist to hold indices of the matching strings in the text
        Integer[] lineAndColumn ; //holds line and column number for each matching strings
        String str = ""; //initializing str which holds the current line
        int length = pattern.length(); //length variable holds length of the pattern

        for(int i = 0 ; i < text.size()-1; i++) {
            str = text.get(i); // str is equal to current line of the text
            if (str.length() >= pattern.length()) {
                sub = str.substring(0, length); //initializing substring

                for (int j = length - 1; j < str.length(); ) {
                    int a = 0;  //initializing "a"

                    if (sub.contains("<")) { //if substring contains "<" sign
                        if (j - length > 0) {
                            j -= length - 1; //decrease j by length-1
                        }
                        while (str.charAt(j) != '>') { //search for ">" sign
                            j++; //increase j by 1
                        }
                        j += length; //at the end of the while loop, increase j by length

                    }

                    if (!sub.contains("<")) {  //if substring does not contain "<" sign
                        while ((a < length) && (pattern.charAt(length - 1 - a) == str.charAt(j - a)) && numberOfComparisons++ >= 0) { //in each match, we increase the variable "a" to obtain whether we have complete match or not
                            a++;
                        }
                        if (a == length) { //if pattern matches completely
                            lineAndColumn = new Integer[2]; //creating new array which holds line and column number of match
                            lineAndColumn[0] = i + 1; //line number
                            lineAndColumn[1] = j - length + 2; //column number
                            indices.add(lineAndColumn); //add array to arraylist
                            j++;
                        } else
                            for (int m = 0; m < badSymbol.length; m++) {
                                if (str.charAt(j) == (char) badSymbol[m][0]) { //if the currents char is equal to any of the elements in bad symbol table
                                    j += badSymbol[m][1]; //number of shift will be the corresponding integer in the table
                                    //decrease j by 1(we will increase j by 1 at the beginning of the loop)
                                    break; //break the loop
                                }
                                if (m == badSymbol.length - 1) {  //if there is no match with the bad symbol table
                                    j += length; //number of shift will be length(we will increase j by 1 at the beginning of the loop)
                                }
                            }
                    }

                    if (j < str.length()) {
                        sub = str.substring(j - length + 1, j + 1);  // set substring to search if there is "<" sign or not
                    }
                }
            }
        }

        /*
        for(int i = 0 ; i< indices.size() ; i++) {
            System.out.println("Horspool Algorithm " + indices.get(i)[0] + " " + indices.get(i)[1]);
        }
       */

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





    //This method create goodSuffix table of pattern for boyermoore algorithm
    public static void createGoodSuffixTable(String pattern){
        goodSuffix = new int[pattern.length() - 1];
        String searchPattern = "";//string to be compared with pattern
        String reversedPattern = "";//reversal of the pattern
        String temp = "";

        for (int i = pattern.length() - 1; i >= 0; i--) {//reverses the pattern to make it easier
            reversedPattern += pattern.charAt(i);
        }


        

        for(int i = 0; i < goodSuffix.length; i++) {
            goodSuffix[i] = pattern.length();
        }


        for(int i = 0; i < pattern.length() - 1; i++) {//Cycles through all the characters in the pattern
            searchPattern = reversedPattern.substring(0, i+2);//set searchPattern
            for(int j = 1; j < reversedPattern.length(); j++) {//goodsuffix stops if found
                if(goodSuffix[i] != 0 && goodSuffix[i] != pattern.length()) {
                    break;
                }
                if(reversedPattern.charAt(j) == searchPattern.charAt(0)) {//Checks whether there is a match between the search pattern and the reversed pattern.
                    //length control of strings
                    if(j+searchPattern.length() - 1 > reversedPattern.length() - 1) {
                        temp = reversedPattern.substring(j);
                        for(int m = 0; m < temp.length(); m++) {//calculate how many characters match
                            if(temp.charAt(m) != searchPattern.charAt(m)) {
                                goodSuffix[i] = pattern.length();
                                break;
                            } else {
                                goodSuffix[i] = j;
                            }
                        }
                    } else {
                        temp = reversedPattern.substring(j, j + searchPattern.length());
                        //If the search pattern and reversed pattern match some of them, it looks at the next characters of both, and if different, goodSuffix is found.
                        if(temp.substring(0, temp.length() - 1).equals(searchPattern.substring(0, searchPattern.length() - 1)) && searchPattern.charAt(searchPattern.length() - 1) != temp.charAt(temp.length() - 1)) {
                            goodSuffix[i] = j;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("GoodSuffix Table");
        for(int i = 0; i < goodSuffix.length; i++) {//print the goodSuffix table
            System.out.println("goodSuffix[" + (i+1) + "] = " + goodSuffix[i]);
        }
        System.out.println("--------------------------------------");
    }





    //this method does string matching using boyermoore algorithm
    public static ArrayList<Integer[]> boyerMooreAlgorithm(ArrayList<String> text, String pattern){
        createBadSymbolTable(pattern);
        createGoodSuffixTable(pattern);
        ArrayList<Integer[]> indices = new ArrayList<>();
        Integer[] lineAndColumn ;

        for(int i = 0; i < text.size() - 1; i++) { //loop for access every line in text
            int size = text.size();
            String searchPattern = "";

            int shiftCount = pattern.length();

            for(int j = 0; j < text.get(i).length(); j++) { //Checks if a search pattern exists
                if(pattern.length() - (text.get(i).length() - j) >= 1) {
                    break;
                }
                searchPattern = text.get(i).substring(j, j + pattern.length());
                int count = 0;
                if(searchPattern.contains("<")) { //checks for jumps
                    if(searchPattern.contains(">")) {
                        shiftCount = searchPattern.indexOf(">") + 1;
                    } else {
                        while(text.get(i).charAt(j) != '>') {
                            j++;
                        }
                        j++;
                    }
                } else {
                    int d1 = 0;
                    int d2 = 0;
                    int index = searchPattern.length();
                    for(int m = searchPattern.length() - 1; m >= 0; m--) {//Count how many characters the pattern matches
                        if(searchPattern.charAt(m) == pattern.charAt(m)) {
                            count++;
                        } else {
                            numberOfComparisons++;
                            for(int n = 0; n < badSymbol.length; n++) {//d1 is found by looking at badSymbol table
                                char tes = (char)badSymbol[n][0];
                                char a = searchPattern.charAt(m);
                                if(searchPattern.charAt(m) == tes) {
                                    d1 = badSymbol[n][1];
                                    break;
                                } else {
                                    d1 = pattern.length();
                                }
                            }
                            if(count != 0) {//According to the number of matches, d2 is found by looking at goodSuffix
                                d2 = goodSuffix[count - 1];
                            }
                            //d1 and d2 are compared, whichever is greater, the number of shifts is determined accordingly
                             if(d1 - count > d2) {
                                shiftCount = d1 - count;
                                break;
                            } else {
                                shiftCount = d2;
                                break;
                            }

                        }
                    }

                }
                if(count == pattern.length()) {
                    numberOfComparisons++;
                    lineAndColumn = new Integer[2]; //creating new array which holds line and column number of match
                    lineAndColumn[0] = i + 1; //line number
                    lineAndColumn[1] = j + 1 ; //column number
                    indices.add(lineAndColumn);
                    shiftCount = 1;
                }
                //the more it needs to be scrolled the more scrolling -1 is because at the end of the for loop j is being incremented by any 1 to prevent it
                j += shiftCount - 1;
            }

        }

        return indices;
    }

    //this method prints the desired output to the text file
    public static void printOutput(ArrayList<String> text, ArrayList<Integer[]> indices) throws IOException {

        String fileName = textName + "_output";
        FileWriter writer = new FileWriter(fileName + ".html");
        Integer[] lineAndColumn = new Integer[2];
        String patterWithMark = "<mark>" + pattern + "</mark>";//String to replace pattern in
        String newLine = "";
        String oldLine = "";

        for(int i = 0; i < text.size() - 1; ++i) {//traverses each line of text
            oldLine = (String)text.get(i);
            int count = 0;//counts how many matching patterns are in the row
            newLine = oldLine;
            for(int j = 0; j < indices.size() - 1; j++) {//travels indices
                lineAndColumn = (Integer[])indices.get(j);
                if (lineAndColumn[0] - 1 == i) {//checks if there is a patter on that line
                    //changes the line
                    newLine = oldLine.substring(0, lineAndColumn[1] - 1 + count * (patterWithMark.length() - pattern.length())) + patterWithMark + oldLine.substring(lineAndColumn[1] - 1 + pattern.length() + count * (patterWithMark.length() - pattern.length()));
                    oldLine = newLine;
                    ++count;

                }
            }
            writer.write(newLine + "\n");
        }


        writer.close();

    }














}
