To use synonym dictionaries:

1. Backup the index-settings.json
2. Replace the index-settings.json with index-settings.json.gsearch-with-synonym-dictionaries
3. Copy the folder gsearch-synonyms to all the ES nodes under config folder

Whenever you update the dictionaries just close and open the index to refresh the analyzer.
