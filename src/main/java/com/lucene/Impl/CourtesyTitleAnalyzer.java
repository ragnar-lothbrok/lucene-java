package com.lucene.Impl;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

public class CourtesyTitleAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer letterTokenizer = new LetterTokenizer();
		TokenStream filter = new CourtesyTitleFilter(letterTokenizer);
		return new TokenStreamComponents(letterTokenizer, filter);
	}

}