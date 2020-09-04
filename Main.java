package converter;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        int sourceRadix = scanner.nextInt();
        String stringNumber = scanner.next();
        int targetRadix = scanner.nextInt();

        int integralPart = 0;
        double decimalPart = 0;
        String fractional;

        int pointIndex = stringNumber.indexOf('.');

        if (sourceRadix != 10) {

            String integral;
            if (pointIndex > 0) {
                integral = stringNumber.substring(0, pointIndex);

                fractional = stringNumber.substring(pointIndex + 1);
                char[] fractionalDigits = fractional.toCharArray();

                for (int i = 0; i < fractionalDigits.length; i++) {
                    int nrRepresentation = Character.getNumericValue(fractionalDigits[i]);
                    double n =  nrRepresentation / Math.pow(sourceRadix, i + 1);
                    decimalPart += n;
                }
            } else {
                integral = stringNumber;
            }

            integralPart = toDecimal(integral, sourceRadix);
        } else {
            if (pointIndex > 0) {
                String integral = stringNumber.substring(0, pointIndex);
                integralPart = Integer.parseInt(integral);

                fractional = stringNumber.substring(pointIndex + 1);
                decimalPart = Double.parseDouble("0." + fractional);
            } else {
                integralPart = toDecimal(stringNumber, sourceRadix);
            }
        }

        String integerNr = Integer.toString(integralPart, targetRadix);
        if (targetRadix == 1) {
            integerNr = String.valueOf(getOnes(integerNr));
        }

        StringBuilder fractionalNr = new StringBuilder();

        if (decimalPart > 0.0) {

            double nr;
            for (int i = 0; i < 5; i++) {
                nr = decimalPart * targetRadix;

                int integer = getIntegerPart(nr, targetRadix);
                if (targetRadix > 10) {
                    String convertedNr = Integer.toString(integer, targetRadix);

                    int intRepresentation = Integer.parseInt(convertedNr);
                    if (intRepresentation >= 10) {
                        char chDigit = getCharDigit(intRepresentation);

                        fractionalNr.append(chDigit);
                    } else {
                        fractionalNr.append(convertedNr);
                    }
                } else {
                    fractionalNr.append(integer);
                }

                decimalPart = getFractionalPart(nr);
            }
        }

        System.out.print(integerNr);
        if (decimalPart > 0.0) {
            System.out.print("." + fractionalNr);
        }
    }

    private static char getCharDigit(int integer) {
        char chA = 'a';

        int digitCount = chA - 10 + integer;

        return (char) digitCount;
    }

    // Get the integer part from a decimal number
    // and converts it to the specified radix
    private static int getIntegerPart(double nr, int radix) {
        String stringNr = Double.toString(nr);
        String integralValue = stringNr.substring(0, stringNr.indexOf('.'));

        String stringInteger = stringNr.indexOf('.') > 0
                               ? integralValue
                               : stringNr;

        return toDecimal(stringInteger, radix);
    }

    private static double getFractionalPart(double nr) {
        String stringNr = Double.toString(nr);
        String decimalValue = stringNr.substring(stringNr.indexOf('.') + 1);

        String stringFractional = stringNr.indexOf('.') > 0
                                  ? decimalValue
                                  : "0";

        return Double.parseDouble("0." + stringFractional);
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
