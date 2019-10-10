
package fi.soveltia.liferay.gsearch.elasticsearch.internal.query;

import java.util.function.Supplier;
import java.util.List;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;

import ciir.umass.edu.learning.RankerFactory;

import com.o19s.es.ltr.feature.store.FeatureStore;
import com.o19s.es.ltr.feature.store.index.IndexFeatureStore;
import com.o19s.es.ltr.query.StoredLtrQueryBuilder;

import com.o19s.es.ltr.ranker.parser.LtrRankerParserFactory;
import com.o19s.es.ltr.ranker.parser.LinearRankerParser;
import com.o19s.es.ltr.ranker.parser.XGBoostJsonParser;
import com.o19s.es.ltr.ranker.ranklib.RanklibModelParser;
import com.o19s.es.ltr.utils.FeatureStoreLoader;
import com.o19s.es.ltr.utils.Suppliers;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import fi.soveltia.liferay.gsearch.elasticsearch.query.GSearchLTRQueryTranslator;
import fi.soveltia.liferay.gsearch.query.GSearchLTRQuery;


/**
 * Liferay GSearch LTR query translator impl.
 * 
 * @author Petteri Karttunen
 *
 */
@Component(
	immediate = true, 
	service = GSearchLTRQueryTranslator.class
)
public class GSearchLTRQueryTranslatorImpl implements GSearchLTRQueryTranslator {

    private Supplier<RankerFactory> ranklib = Suppliers.memoize(RankerFactory::new);
    private final LtrRankerParserFactory parserFactory = new LtrRankerParserFactory.Builder()
            .register(RanklibModelParser.TYPE, () -> new RanklibModelParser(ranklib.get()))
            .register(LinearRankerParser.TYPE, LinearRankerParser::new)
            .register(XGBoostJsonParser.TYPE, XGBoostJsonParser::new)
            .build();

	@Override
	public QueryBuilder translate(GSearchLTRQuery ltrQuery) {

		try {
			
			String storeName = IndexFeatureStore.DEFAULT_STORE;
			
			Client client = _elasticsearchConnectionManager.getClient();
			
			FeatureStoreLoader storeLoader = _getFeatureStoreLoader();
			
			StoredLtrQueryBuilder queryBuilder = new StoredLtrQueryBuilder(storeLoader);

			// Active features
			
			queryBuilder.modelName(ltrQuery.getModel());

			// Active features
			
			if (ltrQuery.getActiveFeatures() != null) {
				queryBuilder.activeFeatures(ltrQuery.getActiveFeatures());
			}

			// Params

			if (ltrQuery.getParams() != null) {
				queryBuilder.params(ltrQuery.getParams());
			}
			
			// Store name
			
			// queryBuilder.storeName(IndexFeatureStore.DEFAULT_STORE);			
			
	        return queryBuilder;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return null;
	}
	
    public void unsetElasticsearchConnectionManager(
            ElasticsearchConnectionManager elasticsearchConnectionManager) {

            _elasticsearchConnectionManager = null;
    }
    
    @Reference
    protected void setElasticsearchConnectionManager(
            ElasticsearchConnectionManager elasticsearchConnectionManager) {

            _elasticsearchConnectionManager = elasticsearchConnectionManager;
    }

    private FeatureStoreLoader _getFeatureStoreLoader() {
        return (storeName, client) -> 
        	new IndexFeatureStore(storeName, client, parserFactory);
    }
    
	private ElasticsearchConnectionManager 
		_elasticsearchConnectionManager;
}
