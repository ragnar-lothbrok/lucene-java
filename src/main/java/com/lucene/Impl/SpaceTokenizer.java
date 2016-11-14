package com.lucene.Impl;

import java.io.Reader;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;

public class SpaceTokenizer extends CharTokenizer {
	
	public SpaceTokenizer(Reader input) {
		super();
	}

	public SpaceTokenizer(AttributeFactory factory, Reader input) {
		super(factory);
	}

	@Override
	protected boolean isTokenChar(int c) {
		return !Character.isSpaceChar(c);
	}
}