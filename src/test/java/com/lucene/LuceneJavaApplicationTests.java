/*package com.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
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
	private ApplicationContext applicationContext;

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

	*//**
	 * This will tell you which analyzer to use.
	 *//*
	// @Test
	public void contextLoads() {

		Map<String, Analyzer> fieldAnalyserMap = new HashMap<String, Analyzer>();
		fieldAnalyserMap.put("field", standardAnalyzer);

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

	@Value("${lucene.index.dir}")
	private String indexDir;

	// @Test
	public void termFrequency() throws Exception {
		IndexWriter indexWriter = applicationContext.getBean(IndexWriter.class);
		FieldType textFieldType = new FieldType();
		textFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		textFieldType.setTokenized(true);
		textFieldType.setStored(true);
		textFieldType.setStoreTermVectors(true);
		textFieldType.setStoreTermVectorPositions(true);
		textFieldType.setStoreTermVectorOffsets(true);

		Document doc = new Document();
		Field textField = new Field("content", "", textFieldType);
		String[] contents = { "Humpty Dumpty sat on a wall,", "Humpty Dumpty had a great fall.", "All the king's horses and all the king's men",
				"Couldn't put Humpty together again." };

		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);
			indexWriter.commit();
		}
		indexWriter.close();
		Path path = Paths.get(indexDir);
		Directory directory = FSDirectory.open(path);
		IndexReader indexReader = DirectoryReader.open(directory);
		Terms termsVector = null;
		TermsEnum termsEnum = null;
		BytesRef term = null;
		String val = null;

		for (int i = 0; i < indexReader.maxDoc(); i++) {
			termsVector = indexReader.getTermVector(i, "content");
			if (termsVector != null) {
				termsEnum = termsVector.iterator();
				while ((term = termsEnum.next()) != null) {
					val = term.utf8ToString();
					System.out.println(
							"DocId: " + i + " == " + " term: " + val + " == " + " length: " + term.length + "==" + "Freq : " + termsEnum.docFreq());
					PostingsEnum PostingsEnum = null;
					PostingsEnum docsAndPositionsEnum = termsEnum.postings(PostingsEnum);
					if (docsAndPositionsEnum.nextDoc() >= 0) {
						int freq = docsAndPositionsEnum.freq();
						System.out.println(" freq: " + docsAndPositionsEnum.freq());
						for (int j = 0; j < freq; j++) {
							System.out.println(" [");
							System.out.println(" position: " + docsAndPositionsEnum.nextPosition());
							System.out.println("offset start: " + docsAndPositionsEnum.startOffset());
							System.out.println(" offset end: " + docsAndPositionsEnum.endOffset());
							System.out.println(" ]");
						}
					}
				}
			}
		}
	}

	@Test
	public void searchQuery() throws Exception {
		Path path = Paths.get(indexDir);
		Directory directory = FSDirectory.open(path);
		IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		String[] contents = { "foxtrot", "echo", "delta", "foxtoyi", "bravo", "alpha", "is baker" };
		Integer[] nums = { 10, 20, 30, 40, 50, 60, 70 };
		for (int i = 0; i < contents.length; i++) {
			Document doc = new Document();
//			doc.add(new StringField("content", contents[i], Field.Store.YES));
			doc.add(new TextField("content", contents[i], Field.Store.YES));
			doc.add(new IntPoint("num", nums[i]));
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		indexWriter.close();

		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		SortField sortField = new SortField("content", SortField.Type.STRING);
		Sort sort = new Sort(sortField);
		WildcardQuery wildcardQuery = new WildcardQuery(new Term("content", "*"));
		TopDocs topDocs = indexSearcher.search(wildcardQuery, 2);
		ScoreDoc lastDoc = null;
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
			lastDoc = scoreDoc;
		}

		// Pagination
		topDocs = indexSearcher.searchAfter(lastDoc, wildcardQuery, 12);
		System.out.println("Total " + topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============TERM QUERY===============");
		TermRangeQuery termRangeQuery = new TermRangeQuery("content", new BytesRef("a".getBytes()), new BytesRef("f".getBytes()), true, false);
		topDocs = indexSearcher.search(termRangeQuery, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============NUMERIC RANGE QUERY===============");
		Query rangeQuery = IntPoint.newRangeQuery("num", 10, 20);
		topDocs = indexSearcher.search(rangeQuery, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============PREFIX QUERY===============");
		PrefixQuery prefixQuery = new PrefixQuery(new Term("content", "f"));
		topDocs = indexSearcher.search(prefixQuery, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============QUERY PARSER 1===============");
		QueryParser queryParser = new QueryParser("content", standardAnalyzer);
		Query query = queryParser.parse("[f TO l}");
		topDocs = indexSearcher.search(query, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============QUERY PARSER 2===============");
		query = queryParser.parse("fox*");
		topDocs = indexSearcher.search(query, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		
		 * System.out.println("============QUERY PARSER 3===============");
		 * queryParser.setAutoGeneratePhraseQueries(true); query =
		 * queryParser.parse("fox+trot"); topDocs = indexSearcher.search(query,
		 * 8); for (ScoreDoc scoreDoc : topDocs.scoreDocs) { Document doc =
		 * indexReader.document(scoreDoc.doc); System.out.println(scoreDoc.score
		 * + ": " + doc.getField("content").stringValue()); }
		 

		System.out.println("============QUERY PARSER 4===============");
		queryParser.setDefaultOperator(QueryParser.Operator.OR);
		query = queryParser.parse("foxtrot echo");
		topDocs = indexSearcher.search(query, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		// TODO
		System.out.println("============QUERY PARSER 5===============");
		queryParser.setFuzzyMinSim(2f);
		queryParser.setFuzzyPrefixLength(3);
		query = queryParser.parse("foxt~");
		topDocs = indexSearcher.search(query, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============QUERY PARSER 6===============");
		queryParser.setLowercaseExpandedTerms(true);
		query = queryParser.parse("FOX*");
		topDocs = indexSearcher.search(query, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		// TODO
		System.out.println("============QUERY PARSER 7===============");
		queryParser.setPhraseSlop(4);
		queryParser.setDefaultOperator(QueryParser.Operator.AND);
		query = queryParser.parse("foxtrot sss");
		topDocs = indexSearcher.search(query, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}

		System.out.println("============QUERY PARSER 8===============");
		TermQuery termQuery = new TermQuery(new Term("content", "baker"));
		topDocs = indexSearcher.search(termQuery, 8);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
		}
	}

//	@Test
	public void displayTokenUsingStandardAnalyzer() throws IOException {
		String text = "is baker";
		TokenStream tokenStream = standardAnalyzer.tokenStream("content", new StringReader(text));
		CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			System.out.print("[" + termAtt.toString() + "] ");
		}
	}
}
*/