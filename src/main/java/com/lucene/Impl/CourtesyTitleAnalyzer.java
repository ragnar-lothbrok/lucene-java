/*package com.lucene.Impl;

public class CourtesyTitleAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer letterTokenizer = new LetterTokenizer();
		TokenStream filter = new CourtesyTitleFilter(letterTokenizer);
		return new TokenStreamComponents(letterTokenizer, filter);
	}

}*/