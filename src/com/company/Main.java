package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        // reading matrix from text file and writing it in char matrix
        char[][] key = new char[8][8];

        String[] lines = new String[8];

        File fileCardan = new File("cardan.txt");

        Scanner fileScanner = new Scanner(fileCardan);

        for (int i = 0; i < 8; i++) {
            lines[i] = fileScanner.nextLine();
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                key[i][j] = lines[i].charAt(j);
            }

        }

        // reading text from input.txt file and getting count of 64 char blocks
        String text = new String(Files.readAllBytes(Paths.get("input.txt")));

        int blocksCount = text.length() / 64 + 1;

        FileWriter fw = new FileWriter("encode.txt");

        // encoding all 64 char blocks
        for (int i = 0; i < blocksCount; i++) {

            char[][] res;
            if(64+i*64 > text.length())
                res = encode(key, text.substring(i*64));
            else
                res = encode(key, text.substring(i*64,64+i*64));
            fileWrite(res,fw);

        }

        fw.close();

        text = new String(Files.readAllBytes(Paths.get("encode.txt")));

        fw = new FileWriter("decode.txt");

        for (int i = 0; i < blocksCount; i++) {

            fw.write(decode(text.substring(i*64,i*64+64),key));

        }

        fw.close();



    }

    public static void fileWrite(char[][] matrix, FileWriter fw) throws IOException {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fw.write(matrix[i][j]);
            }
        }

    }

    public static String decode(String code, char[][] key) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if(key[j][k] == '1'){ // if there is an empty slot
                        sb.append(code.charAt(j*8+k));
                    }
                }
            }
            key = rotate(key);
        }

        return sb.toString();
    }

    public static char[][] encode(char[][] key, String message) {

        char[][] resultMatrix = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                resultMatrix[i][j] = (char)((int)Math.random() * (20) + 1);
            }
        }

        // alphabet, which is using to generate random symbols at the end of the block
        String alphabet = "asddfghjjk123456";
        Random random = new Random();
        int strIndex = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    if(key[j][k] == '1'){ // if there is an empty slot
                        if (strIndex < message.length()) {
                            resultMatrix[j][k] = message.charAt(strIndex);
                            strIndex++;
                        }
                        else {
                            resultMatrix[j][k] = alphabet.charAt(random.nextInt(alphabet.length()));
                        }
                    }
                }
            }
            key = rotate(key);
        }
        return resultMatrix;
    }

    public static char[][] rotate(char[][] array) {

        // after one passing through the cycle matrix need to rotate on 90 degrees
        // here is rotating function to right

        char[][] rotateArray = array;
        char[][] tempArray = new char[8][8];

        for (int i = 0; i < 8; i++ ){

            for (int j = 0; j < 8; j++){

                tempArray[i][j] = rotateArray[7-j][i];
            }
        }
        return tempArray;
    }
}
