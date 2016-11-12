package com.lucene;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class LuceneJavaApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneJavaApplication.class);

	@Value("${lucene.index.dir}")
	private String indexDir;

	public static void main(String[] args) {
		SpringApplication.run(LuceneJavaApplication.class, args);
	}

	@Bean
	@Scope(
		scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IndexWriter getIndexWriter() {
		IndexWriter indexWriter = null;
		try {
			Path path = Paths.get(indexDir);
			Directory directory = FSDirectory.open(path);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getWhitespaceAnalyzer());
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		} catch (Exception e) {
			LOGGER.error("Exception occured {}", e);
		}
		return indexWriter;
	}

	@Bean
	@Scope(
		scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public IndexSearcher getIndexSearcher() {
		IndexSearcher indexSearcher = null;
		try {
			Path path = Paths.get(indexDir);
			Directory directory = MMapDirectory.open(path);
			IndexReader indexReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
		} catch (Exception e) {
			LOGGER.error("Exception occured {}", e);
		}
		return indexSearcher;
	}

	@Bean
	public WhitespaceAnalyzer getWhitespaceAnalyzer() {
		return new WhitespaceAnalyzer();
	}

	@Bean
	public StandardAnalyzer getStandardAnalyzer() {
		return new StandardAnalyzer();
	}

	@Bean
	public StopAnalyzer getStopAnalyzer() {
		return new StopAnalyzer();
	}

	@Bean
	public SimpleAnalyzer getSimpleAnalyzer() {
		return new SimpleAnalyzer();
	}

}
