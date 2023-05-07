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
        Path textPath = Path.of("sample1.html");
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


        pattern = "AT_THAT";

        ArrayList<Integer[]> indices; //The indices of all matches



        //BRUTE FORCE
        long beginBruteForce = System.currentTimeMillis();

        indices = bruteForceStringMathcing(text, pattern);
        printOutput(text, indices);

        long endBruteForce = System.currentTimeMillis();
        System.out.printf("Time elapsed for Brute Froce: %d ms\n", endBruteForce-beginBruteForce);






        //HORSPOOL'S ALGORITHM
        long beginHorspool = System.currentTimeMillis();

        indices = horspoolsAlgorithm(text, pattern);
        printOutput(text, indices);

        long endHorspool = System.currentTimeMillis();
        System.out.printf("Time elapsed for Horspool's Algorithm: %d ms\n", endHorspool-beginHorspool);





        //BOYER-MOORE ALGORITHM
        long beginBoyer = System.currentTimeMillis();

        indices = boyerMooreAlgorithm(text, pattern);
        printOutput(text, indices);

        long endBoyer = System.currentTimeMillis();
        System.out.printf("Time elapsed for Boyer-Moore Algorithm: %d ms\n", endBoyer-beginBoyer);






        long endMain = System.currentTimeMillis();
        System.out.printf("Time elapsed in total: %d ms\n", endMain-beginMain);

    }





    public static ArrayList<Integer[]> bruteForceStringMathcing(ArrayList<String> text, String pattern){

        ArrayList<Integer[]> indices = new ArrayList<>();



        return indices;
    }





    public static void createBadSymbolTable(String pattern){

    }





    public static ArrayList<Integer[]> horspoolsAlgorithm(ArrayList<String> text, String pattern){

        ArrayList<Integer[]> indices = new ArrayList<>();



        return indices;
    }






    public static void createGoodSuffixTable(String pattern){


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
