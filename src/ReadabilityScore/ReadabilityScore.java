package ReadabilityScore;

import java.io.*;

/**
 * Readability Score from HyperSkill:
 * Program calculates the text readability score by 4 different formulas.
 * Use filename as the only args parameter with one-line text in it, while compiling.
 */

public class ReadabilityScore {

    static double chars;
    static double words;
    static double sentences;
    static double syllables;
    static double polysyllables;


    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
        final String text = br.readLine();
        final String[] allWords = text.replaceAll("[,.!?]", "").split("\\s+");
        chars = text.replaceAll("\\s+", "").length();
        sentences = text.split("[.!?]").length;
        words = allWords.length;
        countSyllables(allWords);
        br.close();
        System.out.printf("The text is:%n%s%n%n" +
                        "Words: %.0f%n" +
                        "Sentences: %.0f%n" +
                        "Characters: %.0f%n" +
                        "Syllables: %.0f%n" +
                        "Polysyllables: %.0f%n" +
                        "Enter the score you want to calculate (ARI, FK, SMOG, CL, all):",
                text, words, sentences, chars, syllables, polysyllables);

        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println();
        switch (br.readLine().toLowerCase()) {
            case "ari":
                getScore(2);
                break;
            case "fk":
                getScore(3);
                break;
            case "smog":
                getScore(4);
                break;
            case "cl":
                getScore(5);
                break;
            default:
                getScore(1);
        }
        br.close();
    }

    static void countSyllables(String[] text) {
        for (String word : text) {
            int wordsylls = 0;
            syllables -= word.equalsIgnoreCase("you") ? 1 : 0;
            for (int i = 0; i < word.length(); i++) {
                char a = Character.toLowerCase(word.charAt(i));
                if ((a == 'e' && i <= word.length() - 2) || (isVowel(a))) {
                    syllables++;
                    wordsylls++;
                    i++;
                }
            }
            syllables += wordsylls == 0 ? 1 : 0;
            polysyllables += wordsylls > 2 ? 1 : 0;
        }
    }

    static boolean isVowel(char ch) { return ch == 'a' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'y'; }

    static double getScore(int trigger) {
        double score = 0;
        switch (trigger) {
            case 1:
                double totalAges = 0;
                while (++trigger < 6) {
                    totalAges += getScore(trigger); // recursion, to get average age
                }
                System.out.printf("%n%nThis text should be understood in average by %.2f year olds.", totalAges / 4);
                return 0;
            case 2:
                System.out.printf("%nAutomated Readability Index: ");
                score = 4.71 * (chars / words) + 0.5 * (words / sentences) - 21.43; // ARI Score
                break;
            case 3:
                System.out.printf("%nFlesch–Kincaid readability tests: ");
                score = 0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59; // FK Score
                break;
            case 4:
                System.out.printf("%nSimple Measure of Gobbledygook: ");
                score = 1.043 * (Math.sqrt(polysyllables * (30 / sentences))) + 3.1291; // SMOG Score
                break;
            case 5:
                System.out.printf("%nColeman–Liau index: ");
                score = 0.0588 * (chars / words * 100) - 0.296 * (sentences / words * 100) - 15.8;  // CL Score
                break;
        }

        long temp = Math.round(score);
        int border = 1;
        if (temp >= 3) {
            border = temp < 13 ? 2 : 7;
        }
        int upperBound = temp > 13 ? 24 : (int)(4 + temp + border);
        System.out.printf("%.2f (about %d year olds)", score, upperBound);
        return upperBound;
    }
}
