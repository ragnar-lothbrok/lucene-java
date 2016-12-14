/*package com.lucene.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lucene.api.ILuceneIndexer;
import com.lucene.models.Product;

@RestController
public class ProductController {

	@Autowired
	private ILuceneIndexer<Product> luceneIndexer;

	@RequestMapping(
		value = "/index",
		method = RequestMethod.GET,
		produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> startIndexing(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		luceneIndexer.init();
		return map;
	}
}
*/