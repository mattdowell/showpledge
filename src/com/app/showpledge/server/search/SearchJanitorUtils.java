package com.app.showpledge.server.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.app.showpledge.shared.entities.iface.Searchable;

/**
 * Taken from: http://fulltext-search-in-objectify.googlecode.com/svn/guestbook/src /guestbook/SearchJanitorUtils.java
 * 
 * @author mjdowell
 * 
 */
public class SearchJanitorUtils {

	public static final int MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX = 200;

	private static final Logger log = Logger.getLogger(SearchJanitorUtils.class.getName());

	/** From StopAnalyzer Lucene 2.9.1 */
	public final static String[] stopWords = new String[] { "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
			"into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this",
			"to", "was", "will", "with" };

	/**
	 * Uses english stemming (snowball + lucene) + stopwords for getting the words.
	 * 
	 * @param index
	 * @return
	 */
	public static Set<String> getTokensForIndexingOrQuery(String index_raw, int maximumNumberOfTokensToReturn) {

		String indexCleanedOfHTMLTags = index_raw.replaceAll("\\<.*?>", " ");

		Set<String> returnSet = new HashSet<String>();

		try {

			Analyzer analyzer = new SnowballAnalyzer(org.apache.lucene.util.Version.LUCENE_CURRENT, "English", stopWords);

			TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(indexCleanedOfHTMLTags));

			Token token = new Token();

			// OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
			TermAttribute termAttribute = tokenStream.getAttribute(TermAttribute.class);

			while (tokenStream.incrementToken()) {
				// int startOffset = offsetAttribute.startOffset();
				// int endOffset = offsetAttribute.endOffset();
				String term = termAttribute.term();

				// Normalize on LOWER CASE!
				returnSet.add(term.toLowerCase());
			}

		} catch (IOException e) {
			log.severe(e.getMessage());
		}

		return returnSet;

	}

	/**
	 * This method rebuilds the List of searchable strings for the search engine
	 * 
	 * @param inSearchableEntity
	 */
	public static void updateFtsForSearchable(Searchable inSearchableEntity) {

		StringBuffer sb = new StringBuffer();
		sb.append(inSearchableEntity.getSearchableContent());

		Set<String> new_ftsTokens = getTokensForIndexingOrQuery(sb.toString(), MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX);
		Set<String> ftsTokens = inSearchableEntity.getFts();
		
		StringTokenizer tokenizer = new StringTokenizer(sb.toString());
		
		
		if (ftsTokens == null) {
			ftsTokens = new HashSet<String>(); 
		} else {
			ftsTokens.clear();
		}
		
		for (String token : new_ftsTokens) {
			ftsTokens.add(token);
		}
		
		while (tokenizer.hasMoreElements()) {
			ftsTokens.add(tokenizer.nextToken());
		}

		inSearchableEntity.setFts(ftsTokens);
	}

}