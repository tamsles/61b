package ngrams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // TODO: Add any necessary static/instance variables.
    private Map<String, TimeSeries> wordToCounts;
    private TimeSeries totalCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.

        wordToCounts = new HashMap<>();
        totalCounts = new TimeSeries();

        loadWordsFile(wordsFilename);
        loadCountsFile(countsFilename);
    }

    private void loadWordsFile(String wordsFilename) {
        try (BufferedReader br = new BufferedReader(new FileReader(wordsFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                String word = parts[0];
                int year = Integer.parseInt(parts[1]);
                double count = Double.parseDouble(parts[2]);

                if (!wordToCounts.containsKey(word)) {
                    wordToCounts.put(word, new TimeSeries());
                }
                wordToCounts.get(word).put(year, count);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCountsFile(String countsFilename) {
        try (BufferedReader br = new BufferedReader(new FileReader(countsFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int year = Integer.parseInt(parts[0]);
                double totalCount = Double.parseDouble(parts[1]);

                totalCounts.put(year, totalCount);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordToCounts.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordToCounts.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */

    public TimeSeries countHistory(String word) {
        if (!wordToCounts.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordToCounts.get(word));
    }

    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCounts);
    }

    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries wordHistory = countHistory(word, startYear, endYear);
        if (wordHistory.isEmpty()) {
            return new TimeSeries();
        }
        TimeSeries totalsInRange = new TimeSeries(totalCounts, startYear, endYear);
        return wordHistory.dividedBy(totalsInRange);
    }

    public TimeSeries weightHistory(String word) {
        TimeSeries wordHistory = countHistory(word);
        if (wordHistory.isEmpty()) {
            return new TimeSeries();
        }
        return wordHistory.dividedBy(totalCounts);
    }

    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            result = result.plus(weightHistory(word, startYear, endYear));
        }
        return result;
    }

    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            result = result.plus(weightHistory(word));
        }
        return result;
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
