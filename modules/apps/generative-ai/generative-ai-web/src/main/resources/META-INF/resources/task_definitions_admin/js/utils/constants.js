/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

export const ACTIVE = Liferay.Language.get('active');
export const ALL = Liferay.Language.get('all');
export const INACTIVE = Liferay.Language.get('inactive');

export const ASCENDING = Liferay.Language.get('ascending');
export const DESCENDING = Liferay.Language.get('descending');

export const ASSET_CATEGORY_ID = 'asset_category_id';
export const GROUP_ID = 'group_id';

export const CONFIG_PREFIX = 'configuration';

export const COPY_BUTTON_CSS_CLASS = 'sxp-copy-button';

export const DEFAULT_INDEX_CONFIGURATION = {
	external: false,
	indexName: '',
};

export const SXP_ELEMENT_PREFIX = {
	QUERY: 'querySXPElement',
};

export const NAME_LABELS = {
	chain: "Chain",
	gemini_chat_model: "Gemini Chat Model",
	google_imagen: "Google Imagen",
	local_document_retrieval: "Local Document Retrieval",
	retrieve_local_documents: "Retrieve Local Documents",
	openai_image_model: "OpenAI Image Model",
	task_context_agent: "Task Context Agent",
	task_context_parameter_agent: "Task Context Parameter Agent",
	text_input_agent: "Text Input Agent",
	webhook: "Webhook",
}

export const ATTRIBUTES_LABELS = {
	context_output_parameter_name: "Context Output Parameter Name",
	location: "Location",
	max_output_tokens: "Max Output Tokens",
	memory_max_messages: "Memory Max Messages",
	model_name: "Model Name",
	project: "Project",
	prompt_template: "Prompt Template",
	system_message: "System Message",
	top_k: "Top K",
	webhook: "Webhook",
	use_chat_memory: "Use Chat Memory",
}

export const ATTRIBUTES_INPUT_TYPES = {
	context_output_parameter_name: "text",
	location: "text",
	max_output_tokens: "number",
	memory_max_messages: "number",
	model_name: "text",
	project: "text",
	prompt_template: "text",
	system_message: "text",
	top_k: "number",
	webhook: "text",
	use_chat_memory: "checkbox",
}
