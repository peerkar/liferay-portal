import React, { useEffect, useRef, useState, MouseEvent } from 'react';

import ReactFlow, {
  removeElements,
  addEdge,
  isNode,
  Background,
  Controls,
  Elements,
  BackgroundVariant,
  FlowElement,
  Node,
  Edge,
  Connection,
  OnLoadParams,
  useZoomPanHelper,
} from 'react-flow-renderer';

import { openModal } from 'frontend-js-web';

import ConfigurationForm from '../ConfigurationForm';
import getMockedElements from './mockedElements';

// const onNodeDragStop = (_: MouseEvent, node: Node) => console.log('drag stop', node);

const initialElements = [
  {
    id: '1',
    type: 'input',
    data: {
      label: 'Task'
    },
    position: {
      x: 0,
      y: 0
    },
    className: 'light'
  },
];

const MockedFlow = ({ 
  editingMode, 
  externalReferenceCode,
  taskConfigWithIds, 
  setTaskConfigWithIds 
}) => {
  const [rfInstance, setRfInstance] = useState(null);

  const [elements, setElements] = useState (
    getMockedElements(externalReferenceCode) ??
    initialElements
  );

  // const onElementsRemove = (elementsToRemove) => setElements((els) => removeElements(elementsToRemove, els));
  // const onConnect = (params) => setElements((els) => addEdge(params, els));

  const onLoad = (reactFlowInstance) => setRfInstance(reactFlowInstance);

  const onNodeDoubleClick = (_, element) => {
    openModal({
      bodyComponent: () => (
        <ConfigurationForm
          setTaskConfigWithIds={setTaskConfigWithIds}
          taskConfigWithIds={taskConfigWithIds}
        />),
      title: 'Edit task',
      size: 'lg',
    })
  };

  useEffect(() => {
    if (rfInstance) {
      // rfInstance.setTransform({ x: (1174 / 2) - (260 / 2), y: (708 / 2) - (60 / 2), zoom: 1.5 });
      rfInstance.project({ x: 100, y: 100 });
      rfInstance.fitView();
      rfInstance.zoomTo(1.5);
    }
  }, [editingMode, rfInstance])

  // const logToObject = () => console.log(rfInstance?.toObject());
  // const resetTransform = () => rfInstance?.setTransform({ x: 0, y: 0, zoom: 1 });

  // const toggleClassnames = () => {
  //   setElements((elms) => {
  //     return elms.map((el) => {
  //       if (isNode(el)) {
  //         el.className = el.className === 'light' ? 'dark' : 'light';
  //       }

  //       return el;
  //     });
  //   });
  // };

  return (
    <ReactFlow
      elements={elements}
      onLoad={onLoad}
      onNodeDoubleClick={onNodeDoubleClick}
      // onElementsRemove={onElementsRemove}
      // onConnect={onConnect}
      className="react-flow-basic-example"
      defaultZoom={1.5}
      minZoom={0.2}
      maxZoom={4}
    >
      <Background variant={BackgroundVariant.Lines} />
      <Controls />
    </ReactFlow>
  );
};

export default MockedFlow;
