/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import 'codemirror/addon/display/autorefresh';

import 'codemirror/addon/edit/closebrackets';

import 'codemirror/addon/edit/closetag';

import 'codemirror/addon/edit/matchbrackets';

import 'codemirror/addon/fold/brace-fold';

import 'codemirror/addon/fold/comment-fold';

import 'codemirror/addon/fold/foldcode';

import 'codemirror/addon/fold/foldgutter.css';

import 'codemirror/addon/fold/foldgutter';

import 'codemirror/addon/fold/indent-fold';

import 'codemirror/lib/codemirror.css';

import 'codemirror/mode/javascript/javascript';
import CodeMirror from 'codemirror';
import React, {useEffect, useRef} from 'react';

const MODES = {
	json: {
		name: 'JSON',
		type: 'application/json',
	},
};

const CodeMirrorEditor = ({
	onChange = () => {},
	mode = 'json',
	value = '',
	readOnly = false,
}) => {
	const editor = useRef();
	const editorWrapper = useRef();

	useEffect(() => {
		if (editorWrapper.current) {
			const codeMirror = CodeMirror(editorWrapper.current, {
				autoCloseTags: true,
				autoRefresh: true,
				extraKeys: {
					'Ctrl-Space': 'autocomplete',
				},
				foldGutter: true,
				gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
				indentWithTabs: true,
				inputStyle: 'contenteditable',
				lineNumbers: true,
				matchBrackets: true,
				mode: {globalVars: true, name: MODES[mode].type},
				readOnly,
				tabSize: 2,
				value,
				viewportMargin: Infinity,
			});

			codeMirror.on('change', (cm) => {
				onChange(cm.getValue());
			});

			editor.current = codeMirror;
		}
	}, [editorWrapper]); // eslint-disable-line

	return (
		<div className="codemirror-editor-wrapper" ref={editorWrapper}></div>
	);
};

export default CodeMirrorEditor;
