package com.lucene;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.NoDeletionPolicy;
import org.apache.lucene.index.PersistentSnapshotDeletionPolicy;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import com.snapdeal.adtech.dto.AdTechPogRequest;
import com.snapdeal.adtech.services.IAdTechQuotaClientService;
import com.snapdeal.adtech.services.impl.AdTechQuotaClientServiceImpl;
import com.snapdeal.base.startup.config.AppEnvironmentContext;
import com.snapdeal.base.transport.service.ITransportService;

@SpringBootApplication
@ComponentScan(
	basePackages = { "com.lucene", "com.snapdeal.base.transport", "com.snapdeal.catalog.client.service.impl",
			"com.snapdeal.catalog.admin.client.service.impl", "com.snapdeal.product.client.service.impl",
			"com.snapdeal.product.admin.client.service.impl", "com.snapdeal.brandCategory.client.service.impl",
			"com.snapdeal.locality.client.service.impl", "com.snapdeal.locality.admin.client.service.impl", "com.snapdeal.service",
			"com.snapdeal.cfs.client.service.impl", "com.snapdeal.ipms", "com.snapdeal.coms", "com.snapdeal.ops", "com.snapdeal.adtech" })
@EnableAutoConfiguration(
	exclude = { DataSourceTransactionManagerAutoConfiguration.class, DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
			SecurityAutoConfiguration.class })
public class LuceneJavaApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneJavaApplication.class);

	@Value("${lucene.index.dir}")
	private String indexDir;

	@Bean
	public AppEnvironmentContext appEnvironmentContext() {
		return new AppEnvironmentContext("exclusively");
	}

	@Autowired
	@Qualifier("adTechClientService")
	private IAdTechQuotaClientService adTechQuotaClientService;

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
			// IndexWriterConfig indexWriterConfig = new
			// IndexWriterConfig(getNGramAnalyser());
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getStandardAnalyzer());
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			PersistentSnapshotDeletionPolicy policy = new PersistentSnapshotDeletionPolicy(NoDeletionPolicy.INSTANCE,
					FSDirectory.open(Paths.get(indexDir + "/backup")));
			indexWriterConfig.setIndexDeletionPolicy(policy);
			indexWriterConfig.setRAMBufferSizeMB(1000);
			indexWriterConfig.setMaxBufferedDocs(1000);
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

	@Bean(
		value = "whitespaceAnalyzer")
	public WhitespaceAnalyzer getWhitespaceAnalyzer() {
		return new WhitespaceAnalyzer();
	}

	@Bean(
		value = "standardAnalyzer")
	public StandardAnalyzer getStandardAnalyzer() {
		return new StandardAnalyzer();
	}

	@Bean(
		value = "lowerCaseAnalyzer")
	public Analyzer getLowerCaseAnalyzer() {
		/*
		 * return new Analyzer() {
		 * 
		 * @Override protected TokenStreamComponents createComponents(String
		 * fieldName) { final KeywordTokenizer src = new KeywordTokenizer();
		 * TokenStream tok = new LowerCaseFilter(src); return new
		 * TokenStreamComponents(src, tok) {
		 * 
		 * @Override protected void setReader(final Reader reader) {
		 * super.setReader(reader); } }; } };
		 */
		return null;
	}

	@Bean
	public StopAnalyzer getStopAnalyzer() {
		return new StopAnalyzer();
	}

	@Bean
	public SimpleAnalyzer getSimpleAnalyzer() {
		return new SimpleAnalyzer();
	}

	@Bean(
		value = "nGramAnalyzer")
	public Analyzer getNGramAnalyser() {
		Analyzer analyzer = new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				Tokenizer tokenizer = new LowerCaseTokenizer();
				TokenStream filter = new NGramTokenFilter(tokenizer, 3, 5);
				return new TokenStreamComponents(tokenizer, filter);
			}
		};
		return analyzer;
	}
}
