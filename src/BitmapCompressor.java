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

        // TODO: complete compress()
        String s = BinaryStdIn.readString();
        long n = s.length();
        int bits = getBits(n);
        ArrayList<Integer> ints = new ArrayList<>();
        char c = s.charAt(0);
        int entryCount = 0;
        int index = 1;
        while (index < n) {
            int i = 0;
            while (index < n && s.charAt(index) == c) {
                i++;
                index++;
            }

            index++;

            ints.add(i);
            entryCount++;
        }

        BinaryStdOut.write(entryCount, 32);

        BinaryStdOut.write(bits, 32);

        BinaryStdOut.write(c, 1);


        for (int i : ints) {
            BinaryStdOut.write(i, bits);
        }

        BinaryStdOut.close();
    }

    private static int getBits(long l) {
        int i = 0;
        while (Math.pow(2, i) - 1 < l) {
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
        boolean isOne = BinaryStdIn.readBoolean();

        int zeroOrOne;
        int value;

        for (int i = 0; i < entryCount; i++) {
            value = BinaryStdIn.readInt(bits);
            if (isOne) zeroOrOne = 1;
            else zeroOrOne = 0;
            for (int j = 0; j < value; j++) {
                BinaryStdOut.write(zeroOrOne, 1);
            }
            isOne = !isOne;
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