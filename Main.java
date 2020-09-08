package converter;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

interface IFractionPart<N extends Number> {
    String toString(int toBase);
}

public class Main {

    private static class Input {
        final Scanner scanner;
        String number;
        int base;
        int toBase;

        private Input(Scanner scanner) {
            this.scanner = scanner;
        }

        public void read() {
            tryReadBase();
            tryReadNumber();
            tryReadToBase();
        }

        private void tryReadNumber() {
            try {
                this.number = this.scanner.next();
            } catch (Exception exception) {
                throw new InputMismatchException("There was an error reading the number!");
            }

            if (!this.number.matches("^[a-zA-Z0-9]*\\.?[a-zA-Z0-9]*$")){
                throw new InputMismatchException("There was an error reading the number! " +
                                                 "The number is not a number.");
            }
        }

        private void tryReadBase() {
            try {
                this.base = this.scanner.nextInt();

                validateBase(this.base);
            } catch (Exception exception) {
                throw new InputMismatchException("There was an error reading the base for the number!");
            }
        }

        private void tryReadToBase() {
            try {
                this.toBase = this.scanner.nextInt();

                validateBase(this.toBase);
            }
            catch (Exception exception) {
                throw new InputMismatchException("There was an error reading the base for converting the number!");
            }
        }

        private void validateBase(int base) {
            if (base < 1) {
                throw new IllegalArgumentException("There was an error ! Base is less than 1.");
            }
            if (base > Character.MAX_RADIX) {
                throw new IllegalArgumentException("There was an error ! Base is more than " + Character.MAX_RADIX + ".");
            }
        }
    }

    private static class Fraction {

        /**
         * Represents integer part of a fraction.
         * It's value will always be represented in base 10.
         */
        private class IntegerPart implements IFractionPart<Integer> {

            int value;
            final int base;
            final String stringValue;

            IntegerPart (String number, int base) {
                String stringValue = getIntegerPart(number);

                this.stringValue = stringValue;
                this.base = base;

                this.value = getDecimalValue(stringValue, base);
            }

            /*
            * Returns the string representation of this Integer part.
            */
            @Override
            public String toString(int toBase) {
                if (toBase == 1) {
                    return getOnes(this.value);
                }

                return Integer.toString(this.value, toBase);
            }

            private int getDecimalValue(String stringValue, int base) {
                switch (base) {
                    case 1:
                        return stringValue.length();
                    case 10:
                        return Integer.parseInt(stringValue);
                    default:
                        return Integer.parseInt(stringValue, base);
                }
            }

            private String getIntegerPart(String number) {
                int pointIndex = number.indexOf('.');

                return pointIndex > 0 ? number.substring(0, pointIndex) : number;
            }

            private String getOnes(int nr) {
                var ones = new char[nr];
                Arrays.fill(ones, '1');

                return String.valueOf(ones);
            }
        }

        private class FractionalPart implements IFractionPart<Double> {
            public static final int DIGITS = 5;

            double value;
            final int base;
            final String stringValue;

            FractionalPart (String number, int base) {
                String stringValue = getFractionalPart(number);

                this.stringValue = stringValue;
                this.base = base;

                if (this.isFraction()) {
                    this.value = getDecimalValue(stringValue, base);
                }
            }

            @Override
            public String toString(int toBase) {
                if (!isFraction()) {
                    return null;
                }
                if (toBase == 1) {
                    return null;
                }
                if (toBase == 10) {
                    return this.getFractionalPartToString(this.value);
                }
                return getStringRepresentation(toBase);
            }

            private double getDecimalValue(String stringValue, int base) {
                if (base == 10) {
                    return Double.parseDouble("0." + stringValue);
                }

                return getDecimalValueForBase(stringValue, base);
            }

            private double getDecimalValueForBase(String stringValue, int base) {
                double decimal = 0.0;

                char[] digits = stringValue.toCharArray();
                for (int i = 0; i < digits.length; i++) {
                    decimal += getNextDecimalValue(digits[i], base, i + 1);
                }

                return decimal;
            }

            /**
             * @param digit Next decimal digit 0.X...
             * @param base Number initial base.
             * @param pow Next power for raising the base to.
             * @return Numeric Value (1, 2, f, g, ..) / Math.pow(base, pow)
             */
            private double getNextDecimalValue(char digit, int base, int pow){
                int numericValue = Character.getNumericValue(digit);

                return numericValue / Math.pow(base, pow);
            }

            private String getStringRepresentation(int toBase) {
                if (toBase == 10) {
                    return Double.toString(this.value);
                }

                StringBuilder stringBuilder = new StringBuilder();
                double fractionalPart = this.value;
                for (int i = 0; i < DIGITS; i++) {

                    double nr = fractionalPart * toBase;
                    String nextDigit = getDigitRepresentation(nr, toBase);

                    stringBuilder.append(nextDigit);

                    fractionalPart = this.getFractionalPart(nr);
                }

                return stringBuilder.toString();
            }

            private String getDigitRepresentation(double nr, int toBase) {
                int integerPart = this.getIntegerPart(nr, toBase);

                // Convert the previously obtained integer part to base.
                String sIntegerPart = Integer.toString(integerPart, toBase);

                int integer = Integer.parseInt(sIntegerPart);
                if (integer >= 10) {
                    return getLiteralRepresentation(integer);
                }

                return sIntegerPart;
            }

            // Get the integer part from a decimal fractional number,
            // and converts it to the specified base
            private int getIntegerPart(double nr, int base) {
                String stringNr = Double.toString(nr);
                String integralPart = stringNr.substring(0, stringNr.indexOf('.'));

                return Integer.parseInt(integralPart, base);
            }

            private String getFractionalPart(String number) {
                int pointIndex = number.indexOf('.');
                if (pointIndex < 0) {
                    return null;
                }

                return number.substring(pointIndex + 1);
            }

            private double getFractionalPart(double nr) {
                String fractionalPart = getFractionalPartToString(nr);

                return Double.parseDouble("0." + fractionalPart);
            }

            private String getFractionalPartToString(double nr) {
                String stringNr = Double.toString(nr);

                return getFractionalPart(stringNr);
            }

            private String getLiteralRepresentation(int integer) {
                char chA = 'a';

                int digitCount = chA - 10 + integer;

                return String.valueOf((char) digitCount);
            }

            private boolean isFraction() {
                if (this.base == 1) {
                    return false;
                }

                return this.stringValue != null;
            }
        }

        final String number;
        final int sourceRadix;

        IntegerPart integerPart;
        FractionalPart fractionalPart;

        Fraction(String number, int sourceRadix) {
            this.number = number;
            this.sourceRadix = sourceRadix;

            this.integerPart = new IntegerPart(this.number, this.sourceRadix);
            this.fractionalPart = new FractionalPart(this.number, this.sourceRadix);
        }

        public String convert(int toBase) {
            var numberRepresentation = new StringBuilder(integerPart.toString(toBase));
            if (this.fractionalPart.isFraction()) {
                numberRepresentation.append(".").append(fractionalPart.toString(toBase));
            }

            return numberRepresentation.toString();
        }
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var input = new Input(scanner);

        try {
            input.read();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return;
        }

        var fraction = new Fraction(input.number, input.base);
        String convertedFraction = fraction.convert(input.toBase);

        System.out.println(convertedFraction);
    }
}
