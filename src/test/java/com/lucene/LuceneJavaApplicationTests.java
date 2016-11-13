package com.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.FlagsAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(
	classes = LuceneJavaApplication.class)
public class LuceneJavaApplicationTests {

	@Autowired
	@Qualifier(
		value = "nGramAnalyzer")
	private Analyzer nGramAnalyzer;

	@Autowired
	@Qualifier(
		value = "whitespaceAnalyzer")
	private Analyzer whiteSpaceAnalyzer;

	@Autowired
	@Qualifier(
		value = "standardAnalyzer")
	private Analyzer standardAnalyzer;

	@Autowired
	@Qualifier(
		value = "lowerCaseAnalyzer")
	private Analyzer lowerCaseAnalyzer;

	/**
	 * This will tell you which analyzer to use.
	 */
	@Test
	public void contextLoads() {

		Map<String, Analyzer> fieldAnalyserMap = new HashMap<String, Analyzer>();
		fieldAnalyserMap.put("field", lowerCaseAnalyzer);

		PerFieldAnalyzerWrapper perFieldAnalyzerWrapper = new PerFieldAnalyzerWrapper(nGramAnalyzer, fieldAnalyserMap);

		StringReader reader = new StringReader(
				"Lucene is mainly used for information retrieval and you can read more about it at lucene.apache.org.");
		TokenStream ts = null;
		try {
			ts = perFieldAnalyzerWrapper.tokenStream("field", reader);
			OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
			CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);
			PositionIncrementAttribute posAtt = ts.addAttribute(PositionIncrementAttribute.class);
			TypeAttribute typeAtt = ts.addAttribute(TypeAttribute.class);
			FlagsAttribute flagAtt = ts.addAttribute(FlagsAttribute.class);
			PayloadAttribute payAtt = ts.addAttribute(PayloadAttribute.class);

			ts.reset();
			while (ts.incrementToken()) {
				String token = termAtt.toString();
				System.out.println("[" + token + "]");
				// System.out.println("Token starting offset: " +
				// offsetAtt.startOffset());
				// System.out.println(" Token ending offset: " +
				// offsetAtt.endOffset());
				// System.out.println("");

				// System.out.println("Pos Increment :
				// "+posAtt.getPositionIncrement());

				// System.out.println("TypeAttribute : "+typeAtt.getClass());

				// System.out.println("FlagsAttribute : "+flagAtt.getFlags());
				//
				// System.out.println("PayloadAttribute :
				// "+payAtt.getPayload());
			}
			ts.end();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ts.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			perFieldAnalyzerWrapper.close();
		}
	}

}
