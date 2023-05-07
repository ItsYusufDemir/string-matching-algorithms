/* Authors: Eren Duyuk - 150120509
 *          Selin Aydın - 150120061
 *          Yusuf Demir - 150120032
 *
 * Date: 7.05.2023 14:27
 * 
 * Description: Analyzing three algorithms for string matching: Brute force algorithm, Horspool's algorithm and
 * Boyer-Moore algorithm.
 */

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



    public static void main(String args[]) throws IOException {

        Path textPath = Path.of("1MBsample2.html");
        String text = Files.readString(textPath); //The file fetched to memory

        pattern = "AT_THAT";





    }





    public static ArrayList<Integer> bruteForceStringMathcing(String text, String pattern){

        ArrayList<Integer> indices = new ArrayList<>();



        return indices;
    }





    public static void createBadSymbolTable(String pattern){

    }





    public static ArrayList<Integer> horspoolsAlgorithm(String text, String pattern){

        ArrayList<Integer> indices = new ArrayList<>();



        return indices;
    }






    public static void createGoodSuffixTable(String pattern){


    }







    public static ArrayList<Integer> boyerMooreAlgorithm(String text, String pattern){

        ArrayList<Integer> indices = new ArrayList<>();



        return indices;
    }














}
