package fi.soveltia.liferay.gsearch.elasticsearch.internal.index;

/**
 * GSearch index utility class.
 * 
 * @author Petteri Karttunen
 */
public class GSearchIndexUtil {

	public static String getQuerySuggestionIndexName(long companyId) {
		return "gsearch-query-suggestion-" + companyId;
	}

	public static String getIndexSettingsFileName() {
		return GSEARCH_INDEX_SETTINGS_FILE_NAME;
	}

	public static String getQuerySuggestionType() {
		return GSEARCH_QUERY_SUGGESTION_TYPE;
	}
	
	public static String getQuerySuggestionTypeMappingFileName() {
		return GSEARCH_QUERY_SUGGESTION_TYPE_MAPPING_FILE_NAME;
	}
	
	public static final String GSEARCH_INDEX_SETTINGS_FILE_NAME = 
	"/META-INF/gsearch/gsearch-suggestion-index-settings.json";

	public static final String GSEARCH_QUERY_SUGGESTION_TYPE = "GSearchSuggestionType";

	public static final String GSEARCH_QUERY_SUGGESTION_TYPE_MAPPING_FILE_NAME =
		"/META-INF/gsearch/gsearch-querysuggestion-mapping.json";

}
