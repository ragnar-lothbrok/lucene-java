package com.lucene.Impl;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.lucene.api.ILuceneIndexer;
import com.lucene.models.Product;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.MappingStrategy;

@Service(
	value = "productLuceneIndexer")
public class ProductLuceneIndexerImpl implements ILuceneIndexer<Product> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductLuceneIndexerImpl.class);

	@Autowired
	private IndexSearcher indexSearcher;

	@Autowired
	@Qualifier(
		value = "nGramAnalyzer")
	private Analyzer analyzer;

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${lucene.import.filename}")
	private String importPath;

	@Override
	public void init() {
		IndexWriter indexWriter = applicationContext.getBean(IndexWriter.class);
		if (indexSearcher == null)
			indexSearcher = applicationContext.getBean(IndexSearcher.class);
		try {
			CsvToBean<Product> csv = new CsvToBean<Product>();
			CSVReader csvReader = new CSVReader(new FileReader(new ClassPathResource("Lokad_Items.tsv").getFile()), '\t');
			csvReader.readNext();
			List<Product> list = csv.parse(setColumMapping(), csvReader);
			List<Document> documents = new ArrayList<Document>();
			for (Object object : list) {
				Product product = (Product) object;
				Document document = new Document();
				document.add(new IntPoint("pogId", product.getId().intValue()));
				document.add(new TextField("name", product.getName(), Field.Store.YES));
				document.add(new TextField("brand", product.getBrand(), Field.Store.YES));
				document.add(new TextField("buycurrency", product.getBuyCurrency(), Field.Store.YES));
				document.add(new TextField("sell_currency", product.getSellCurrency(), Field.Store.YES));
				document.add(new TextField("category", product.getCategory(), Field.Store.YES));
				document.add(new TextField("sub_category", product.getSubCategory(), Field.Store.YES));
				document.add(new TextField("supplier", product.getSupplier(), Field.Store.YES));
				documents.add(document);
			}
			if (documents.size() > 0) {
				long num = indexWriter.addDocuments(documents);
				LOGGER.info("Indexed Documents {} ", num);
			}
			indexWriter.close();

			search("name", "arTAC");
		} catch (Exception e) {
			LOGGER.error("Exception occured while parsing {}", e);
		}
	}

	private void search(String key, String value) {
		if (indexSearcher == null)
			indexSearcher = applicationContext.getBean(IndexSearcher.class);
		QueryBuilder parser = new QueryBuilder(analyzer);
		Query query = parser.createBooleanQuery(key, value);
		try {
			TopDocs topDocs = indexSearcher.search(query, 100);
			if (topDocs.scoreDocs.length > 0) {
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					System.out.println(key + " == " + document.get(key) + " === " + scoreDoc.doc + "====" + scoreDoc.score);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Exception occured while searching {}", e);
		}
	}
	
	private void search(String key, int value) {
		if (indexSearcher == null)
			indexSearcher = applicationContext.getBean(IndexSearcher.class);
		QueryBuilder parser = new QueryBuilder(analyzer);
		Query query = parser.createBooleanQuery(key, value+"");
		try {
			TopDocs topDocs = indexSearcher.search(query, 100);
			if (topDocs.scoreDocs.length > 0) {
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document document = indexSearcher.doc(scoreDoc.doc);
					System.out.println(key + " == " + document.get(key) + " === " + scoreDoc.doc + "====" + scoreDoc.score);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Exception occured while searching {}", e);
		}
	}

	@Override
	public MappingStrategy<Product> setColumMapping() {
		ColumnPositionMappingStrategy<Product> strategy = new ColumnPositionMappingStrategy<Product>();
		strategy.setType(Product.class);
		String[] columns = new String[] { "Id", "Name", "Category", "SubCategory", "Brand", "Supplier", "MinOrderQuantity", "TargetBatchVolume",
				"Volume", "SellPrice", "SellCurrency", "BuyPrice", "BuyCurrency", "StockOnHand", "StockOnOrder" };
		strategy.setColumnMapping(columns);
		return strategy;
	}

}
