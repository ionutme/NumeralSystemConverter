package converter;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        int sourceRadix = scanner.nextInt();
        String stringNumber = scanner.next();
        int targetRadix = scanner.nextInt();

        String nr = convert(stringNumber, sourceRadix, targetRadix);

        System.out.println(nr);
    }

    private static String convert(String nr, int sourceRadix, int targetRadix) {
        if (targetRadix == 1) {
            char[] ones = getOnes(nr);
            return new String(ones);
        }

        int decimal = toDecimal(nr, sourceRadix);

        return Integer.toString(decimal, targetRadix);
    }

    private static int toDecimal(String nr, int sourceRadix) {
        if (sourceRadix == 1) {
            return nr.length();
        }

        return Integer.parseInt(nr, sourceRadix);
    }

    private static char[] getOnes(String stringNumber) {
        int digits = Integer.parseInt(stringNumber);

        var ones = new char[digits];
        Arrays.fill(ones, '1');

        return ones;
    }

    /**
     * You could instead use the built-in method
     * `Integer.toString(sourceNumber, destinationRadix)
     * @param decimal number
     * @param base radix
     * @return number
     */
    private static int toBase(int decimal, int base) {
        if (decimal == 0) {
            return 0;
        }

        var stringRepresentation = new StringBuilder();

        while (decimal != 0) {
            int remainder = decimal % base;
            stringRepresentation.append(remainder);

            decimal /= base;
        }

        return Integer.parseInt(stringRepresentation.reverse().toString());
    }

    private static int getLastDigit(int nr) {
        return nr % 10;
    }
}
