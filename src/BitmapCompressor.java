/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

import java.util.ArrayList;

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Tyler Hinkie
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {
        int i = BinaryStdIn.readInt(1);
        ArrayList<Integer> ints = new ArrayList<>();
        int entryCount = 0;
        int length = 1;

        int compare;
        while (!BinaryStdIn.isEmpty()) {
            compare = BinaryStdIn.readInt(1);
            if (compare == i) length++;
            else {
                ints.add(length);
                entryCount++;
                length = 1;
                i = compare;
            }
        }

        ints.add(length);
        entryCount++;

        int maxLength = 0;
        for (int lengths : ints) {
            maxLength = Math.max(maxLength, lengths);
        }

        int bits = getBits(maxLength);

        BinaryStdOut.write(entryCount, 32);
        BinaryStdOut.write(bits, 32);
        BinaryStdOut.write(i, 1);

        for (int x : ints) {
            BinaryStdOut.write(x, bits);
        }

        BinaryStdOut.close();
    }

    private static int getBits(int maxLength) {
        int i = 0;
        while ((1 << i) - 1 < maxLength) {
            i++;
        }
        return i;
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        int entryCount = BinaryStdIn.readInt();
        int bits = BinaryStdIn.readInt();
        int value = BinaryStdIn.readInt(1);

        int length;
        for (int i = 0; i < entryCount; i++) {
            length = BinaryStdIn.readInt(bits);
            for (int j = 0; j < length; j++) {
                BinaryStdOut.write(value, 1);
            }
            value = 1 - value;
        }

        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}