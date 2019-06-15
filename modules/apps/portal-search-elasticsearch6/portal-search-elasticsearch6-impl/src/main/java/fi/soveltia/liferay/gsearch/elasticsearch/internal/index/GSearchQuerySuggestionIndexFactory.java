package fi.soveltia.liferay.gsearch.elasticsearch.internal.index;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.elasticsearch6.internal.util.ResourceUtil;
import org.elasticsearch.common.xcontent.XContentType;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalRunMode;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.elasticsearch6.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch6.internal.settings.SettingsBuilder;
import com.liferay.portal.search.elasticsearch6.internal.util.LogUtil;
import com.liferay.portal.search.elasticsearch6.internal.util.ResourceUtil;
import com.liferay.portal.search.elasticsearch6.settings.IndexSettingsContributor;
import com.liferay.portal.search.elasticsearch6.settings.IndexSettingsHelper;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.liferay.portal.search.elasticsearch6.internal.index.IndexFactory;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;

/**
 * This class creates GSearch querySuggestion index and type mapping.
 * 
 * See CompanyIndexFactory
 * 
 * @author Petteri Karttunen
 * @author Michael C. Han
 *
 */
@Component(
		configurationPid = "com.liferay.portal.search.elasticsearch6.configuration.ElasticsearchConfiguration", 
		immediate = true
)
public class GSearchQuerySuggestionIndexFactory implements GSearchIndexFactory {

	@Override
	public void createIndices(AdminClient adminClient, long companyId) throws Exception {

		IndicesAdminClient indicesAdminClient = adminClient.indices();

		String indexName = GSearchIndexUtil.getQuerySuggestionIndexName(companyId);

		if (hasIndex(indicesAdminClient, indexName)) {
			return;
		}

		createIndex(indexName, indicesAdminClient);
	}

	@Override
	public void deleteIndices(AdminClient adminClient, long companyId) throws Exception {

		IndicesAdminClient indicesAdminClient = adminClient.indices();

		String indexName = GSearchIndexUtil.getQuerySuggestionIndexName(companyId);

		if (!hasIndex(indicesAdminClient, indexName)) {
			return;
		}

		DeleteIndexRequestBuilder deleteIndexRequestBuilder = indicesAdminClient.prepareDelete(indexName);

		DeleteIndexResponse deleteIndexResponse = deleteIndexRequestBuilder.get();

		LogUtil.logActionResponse(_log, deleteIndexResponse);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		ElasticsearchConfiguration elasticsearchConfiguration = ConfigurableUtil
				.createConfigurable(ElasticsearchConfiguration.class, properties);

		setIndexNumberOfReplicas(elasticsearchConfiguration.indexNumberOfReplicas());
		setIndexNumberOfShards(elasticsearchConfiguration.indexNumberOfShards());
	}

	protected void addGSearchDocumentTypeMappings(CreateIndexRequestBuilder createIndexRequestBuilder,
			GSearchQuerySuggestionTypeFactory gSearchQuerySuggestionTypeFactory) {

		gSearchQuerySuggestionTypeFactory.createRequiredDefaultTypeMappings(createIndexRequestBuilder);
	}

	protected void createIndex(String indexName, IndicesAdminClient indicesAdminClient) throws Exception {

		CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(indexName);

		GSearchQuerySuggestionTypeFactory gSearchQuerySuggestionTypeFactory = new GSearchQuerySuggestionTypeFactory(
				indicesAdminClient, jsonFactory);

		setSettings(createIndexRequestBuilder, gSearchQuerySuggestionTypeFactory);

		addGSearchDocumentTypeMappings(createIndexRequestBuilder, gSearchQuerySuggestionTypeFactory);

		CreateIndexResponse createIndexResponse = createIndexRequestBuilder.get();

		LogUtil.logActionResponse(_log, createIndexResponse);
	}

	protected boolean hasIndex(IndicesAdminClient indicesAdminClient, String indexName) throws Exception {

		IndicesExistsRequestBuilder indicesExistsRequestBuilder = indicesAdminClient.prepareExists(indexName);

		IndicesExistsResponse indicesExistsResponse = indicesExistsRequestBuilder.get();

		return indicesExistsResponse.isExists();
	}

	protected void loadDefaultIndexSettings(SettingsBuilder settingsBuilder) {
		Settings.Builder builder = settingsBuilder.getBuilder();

		String defaultIndexSettings = ResourceUtil.getResourceAsString(getClass(),
				"/META-INF/index-settings-defaults.json");

		builder.loadFromSource(defaultIndexSettings, XContentType.JSON);
	}

	protected void loadIndexConfigurations(SettingsBuilder settingsBuilder) {
		settingsBuilder.put("index.number_of_replicas", _indexNumberOfReplicas);
		settingsBuilder.put("index.number_of_shards", _indexNumberOfShards);
	}

	protected void loadTestModeIndexSettings(SettingsBuilder settingsBuilder) {
		if (!PortalRunMode.isTestMode()) {
			return;
		}

		settingsBuilder.put("index.refresh_interval", "1ms");
		settingsBuilder.put("index.search.slowlog.threshold.fetch.warn", "-1");
		settingsBuilder.put("index.search.slowlog.threshold.query.warn", "-1");
		settingsBuilder.put("index.translog.sync_interval", "100ms");
	}

	protected void setIndexNumberOfReplicas(String indexNumberOfReplicas) {
		_indexNumberOfReplicas = indexNumberOfReplicas;
	}

	protected void setIndexNumberOfShards(String indexNumberOfShards) {
		_indexNumberOfShards = indexNumberOfShards;
	}

	protected void setSettings(CreateIndexRequestBuilder createIndexRequestBuilder,
			GSearchQuerySuggestionTypeFactory gSearchQuerySuggestionTypeFactory) {

		Settings.Builder builder = Settings.builder();

		gSearchQuerySuggestionTypeFactory.createRequiredDefaultAnalyzers(builder);

		SettingsBuilder settingsBuilder = new SettingsBuilder(builder);

		loadDefaultIndexSettings(settingsBuilder);

		loadTestModeIndexSettings(settingsBuilder);

		loadIndexConfigurations(settingsBuilder);

		createIndexRequestBuilder.setSettings(builder);
	}

	@Reference
	protected JSONFactory jsonFactory;

	private static final Log _log = LogFactoryUtil.getLog(GSearchQuerySuggestionIndexFactory.class);

	private String _indexNumberOfReplicas;
	private String _indexNumberOfShards;
}
