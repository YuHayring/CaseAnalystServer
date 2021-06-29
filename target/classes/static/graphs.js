var currentCaseId;

var cy;

var nodeSelectorOpened = false;

var nodeEditorOpened = false;

var nodeEditorFrame;

var nodeSelectorFrame;

var edgeEditorOpened = false;

var edgeEditorFrame;

var currentClickNode = "";

const graphStyle = [
    { selector: 'node[label = "person"]',
        css: {'background-color': '#786ffc', 'content': 'data(name)'}
    },
    { selector: 'node[label = "thing"]',
        css: {'background-color': '#92f55d', 'content': 'data(name)'}
    },
    { selector: 'node[label = "event"]',
        css: {'background-color': '#ee6f1a', 'content': 'data(name)'}
    },
    { selector: 'node[label = "place"]',
        css: {'background-color': '#5de8f5', 'content': 'data(name)'}
    },
    { selector: 'edge',
        css: {'content': 'data(type)',
            'curve-style': 'bezier',
            'target-arrow-shape': 'triangle',
            'target-arrow-color': 'black',
            'line-color': 'black',
            'width': 3,
        }
    }
];


function loadCytoscape() {
    cy = cytoscape({
        container: document.getElementById('cy'),
        style: graphStyle,
        layout: { name: 'grid'}
    });
    cy.on('tap', 'node', onNodeClick);
    cy.on('tap', 'edge', onEdgeClick);
    loadGraph()
    currentClickNode = ""

}


function onNodeClick(event) {
    var nodeData = event.target._private.data;
    currentClickNode = nodeData.id;
    console.log(nodeData);
    if (nodeEditorOpened) nodeEditorFrame.loadNode(nodeData.id);
    if (edgeEditorOpened) edgeEditorFrame.onNodeClick(nodeData.id, nodeData.name, nodeData.label);
    // let element = cy.getElementById(nodeData.id);
    // console.log(element);

}

function onEdgeClick(event) {
    var edgeData = event.target._private.data;
    console.log(edgeData);
    if (edgeEditorOpened) {
        let start = cy.getElementById(edgeData.source)._private.data;
        let end = cy.getElementById(edgeData.target)._private.data;

        edgeEditorFrame.startId = start.id;
        edgeEditorFrame.startName = start.name;
        edgeEditorFrame.startLabel = start.label;

        edgeEditorFrame.endId = end.id;
        edgeEditorFrame.endName = end.name;
        edgeEditorFrame.endLabel = end.label;


        edgeEditorFrame.onEdgeClick(edgeData.id.slice(1));
    }

}



function openNodeSelector() {
    if (currentCaseId == null) {
        alert("请先选择一个案件！")
        return;
    }
    if (nodeSelectorOpened === true) {
        alert("窗口已打开！")
        return;
    }

    nodeSelectorOpened = true;
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.open({
            type: 2
            ,title: '结点选择器'
            ,area: ['200px', '400px']
            ,shade: 0
            ,maxmin: true
            ,offset: [
                $(window).height()-400
                ,$(window).width()-200
            ]
            ,content: '/window/page.html'
            // ,btn: ['继续弹出', '全部关闭'] //只是为了演示
            // ,yes: function(){
            //     $(that).click();
            // }
            // ,btn2: function(){
            //     layer.closeAll();
            // }
            ,id: "nodeSelector"
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            },
            cancel: function(){
                nodeSelectorOpened = false;

                //return false 开启该代码可禁止点击该按钮关闭
            }
        });
    });
    nodeSelectorFrame = document.getElementById("nodeSelector").getElementsByTagName("iframe")[0].contentWindow;


}

function openNodeEditor() {
    if (currentCaseId == null) {
        alert("请先选择一个案件！")
        return;
    }
    if (nodeEditorOpened === true) {
        alert("窗口已打开！")
        return;
    }

    nodeEditorOpened = true;
    layui.use('layer', function(){
        let layer = layui.layer;
        layer.open({
            type: 2
            ,title: '结点编辑器'
            ,area: ['200px', '400px']
            ,shade: 0
            ,maxmin: true
            ,offset: [
                $(window).height()-400
                ,$(window).width()-400
            ]
            ,content: '/window/edit.html'
            ,id: "nodeEditor"
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            },
            cancel: function(){
                nodeEditorOpened = false;

                //return false 开启该代码可禁止点击该按钮关闭
            }
        });
    });
    nodeEditorFrame = document.getElementById("nodeEditor").getElementsByTagName("iframe")[0].contentWindow;

}

function openEdgeEditor() {
    if (currentCaseId == null) {
        alert("请先选择一个案件！")
        return;
    }
    if (edgeEditorOpened === true) {
        alert("窗口已打开！")
        return;
    }

    edgeEditorOpened = true;
    layui.use('layer', function(){
        let layer = layui.layer;
        layer.open({
            type: 2
            ,title: '关系编辑器'
            ,area: ['600px', '200px']
            ,shade: 0
            ,maxmin: true
            ,offset: [
                $(window).height()-600
                ,$(window).width()-400
            ]
            ,content: '/window/edge.html'
            ,id: "edgeEditor"
            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            },
            cancel: function(){
                edgeEditorOpened = false;

                //return false 开启该代码可禁止点击该按钮关闭
            }
        });
    });
    edgeEditorFrame = document.getElementById("edgeEditor").getElementsByTagName("iframe")[0].contentWindow;

}

function loadGraph() {
    let url = "/case/" + currentCaseId;
    request(url, null, loadGrapgSuccess, "get");
}

function loadGrapgSuccess() {
    if (xmlhttp.status === 403) alert("结点过多，请手动选择。")
    else if (xmlhttp.status === 200) {
        let graph = JSON.parse(xmlhttp.responseText)
        graph.nodes[0].forEach(function(node) {
            cy.add({group: 'nodes', data: { id: node.id.toString(), name : node.name, label:"person" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
        });
        graph.nodes[1].forEach(function(node) {
            cy.add({group: 'nodes', data: { id: node.id.toString(), name : node.name, label:"event" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
        });
        graph.nodes[2].forEach(function(node) {
            cy.add({group: 'nodes', data: { id: node.id.toString(), name : node.name, label:"thing" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
        });
        graph.nodes[3].forEach(function(node) {
            cy.add({group: 'nodes', data: { id: node.id.toString(), name : node.name, label:"place" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
        });




        graph.relationships.forEach(function (edge){
            cy.add({ group: 'edges', data: { id: "e"+edge.id.toString(), source: edge.start.toString(), target: edge.end.toString(), type: edge.type } });
        });
        if (nodeSelectorOpened) nodeSelectorFrame.location.reload();
        if (nodeEditorOpened) nodeEditorFrame.location.reload();
        if (edgeEditorOpened) edgeEditorFrame.location.reload();

    }
}

function loadNeighbours() {
    if (currentCaseId == null || currentClickNode === "")  {
        alert("还未选择案件或点击结点！");
        return;
    }
    let url = "/case/"+ parent.currentCaseId +"/node/" + currentClickNode + "/neighbours";
    request(url,null, getNeighboursSuccess,"get")
}

function getNeighboursSuccess() {
    if (xmlhttp.status === 200) {
        let neighbours = JSON.parse(xmlhttp.responseText);
        let persons = neighbours[0];
        let events = neighbours[1];
        let things = neighbours[2];
        let places = neighbours[3];
        let nodeQueue = new Queue();
        for (let i = 0; i < persons.length; i++) {
            if (cy.getElementById(persons[i].id.toString()).length === 0) {
                cy.add({group: 'nodes', data: { id: persons[i].id.toString(), name : persons[i].name, label:"person" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
                nodeQueue.enqueue(persons[i].id);
            }
        }
        for (let i = 0; i < events.length; i++) {
            if (cy.getElementById(events[i].id.toString()).length === 0) {
                cy.add({group: 'nodes', data: { id: events[i].id.toString(), name : events[i].name, label:"event" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
                nodeQueue.enqueue(events[i].id);
            }
        }
        for (let i = 0; i < things.length; i++) {
            if (cy.getElementById(things[i].id.toString()).length === 0) {
                cy.add({group: 'nodes', data: { id: things[i].id.toString(), name : things[i].name, label:"thing" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
                nodeQueue.enqueue(things[i].id);
            }
        }
        for (let i = 0; i < places.length; i++) {
            if (cy.getElementById(places[i].id.toString()).length === 0) {
                cy.add({group: 'nodes', data: { id: places[i].id.toString(), name : places[i].name, label:"place" }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
                nodeQueue.enqueue(places[i].id);
            }
        }
        loadEdgeChain(nodeQueue);
        nodeSelectorFrame.location.reload();
    }
}

function showNeighboursByType() {
    if (currentCaseId == null || currentClickNode === "")  {
        alert("还未选择案件或点击结点！");
        return;
    }
    layui.use('layer', function () {
        let layer = parent.layui.layer;
        let index = layer.confirm('请选择要显示的邻接结点类型：', {
            btn: ["人物", "物品", "地点", "事件"], //可以无限个按钮
            btn4: function () {
                loadNeighboursByType("event")
            },
            btn3:function () {
                loadNeighboursByType("place")
            },
            btn2 :function(index){
                loadNeighboursByType("thing")
            },
            btn1: function () {
                loadNeighboursByType("person")
                layer.close(index);
                return true;
            }
        });

    })
}

function loadNeighboursByType(type) {
    let url = "/case/"+ parent.currentCaseId +"/node/" + currentClickNode + "/neighbours/" + type;
    request(url,null,function () {
        if (xmlhttp.status === 200) {
            let neighbours = JSON.parse(xmlhttp.responseText);
            let nodeQueue = new Queue();
            for (let i = 0; i < neighbours.length; i++) {
                if (cy.getElementById(neighbours[i].id.toString()).length === 0) {
                    cy.add({group: 'nodes', data: { id: neighbours[i].id.toString(), name : neighbours[i].name, label:type }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
                    nodeQueue.enqueue(neighbours[i].id);
                }
            }
            loadEdgeChain(nodeQueue);
            nodeSelectorFrame.location.reload();
        }
    },"get")
}




// function loadEdge(id) {
//     let url = "/case/"+ parent.currentCaseId +"/node/" + id + "/relationships";
//     request(url,null, getEdgesSuccess,"get")
// }
//
// function getEdgesSuccess() {
//     if (xmlhttp.status === 200) {
//         let relationship = JSON.parse(xmlhttp.responseText);
//         let start = relationship[0];
//         let end = relationship[1];
//         for (let i = 0; i < start.length; i++) {
//             if (cy.getElementById("e"+start[i].id.toString()).length === 0 &&
//                 cy.getElementById(start[i].start.toString()).length !== 0) {
//                 cy.add({ group: 'edges', data: { id: "e"+start[i].id.toString(), source: start[i].start, target: currentClickNode, type : start[i].type } });
//             }
//         }
//         for (let i = 0; i < end.length; i++) {
//             if (cy.getElementById("e"+end[i].id.toString()).length === 0 &&
//                 cy.getElementById(end[i].end.toString()).length !== 0) {
//                 cy.add({ group: 'edges', data: { id: "e"+end[i].id.toString(), source: currentClickNode, target: end[i].end, type: end[i].type } });
//             }
//         }
//     }
// }

function clearAll() {
    cy.remove("edge");
    cy.remove("node");
    if (nodeSelectorOpened) nodeSelectorFrame.location.reload();
    currentClickNode = "";
    alert("清除成功！");
}

function loadEdgeChain(queue) {
    if (queue.empty()) return;
    let id = queue.dequeue();
    let url = "/case/"+ parent.currentCaseId +"/node/" + id + "/relationships";
    request(url,null, function () {
        if (xmlhttp.status === 200) {
            let relationship = JSON.parse(xmlhttp.responseText);
            let start = relationship[0];
            let end = relationship[1];
            for (let i = 0; i < start.length; i++) {
                if (cy.getElementById("e"+start[i].id.toString()).length === 0 &&
                    cy.getElementById(start[i].start.toString()).length !== 0) {
                    cy.add({ group: 'edges', data: { id: "e"+start[i].id.toString(), source: start[i].start, target: id, type : start[i].type } });
                }
            }
            for (let i = 0; i < end.length; i++) {
                if (cy.getElementById("e"+end[i].id.toString()).length === 0 &&
                    cy.getElementById(end[i].end.toString()).length !== 0) {
                    cy.add({ group: 'edges', data: { id: "e"+end[i].id.toString(), source: id, target: end[i].end, type: end[i].type } });
                }
            }
            loadEdgeChain(queue);
        }
    },"get")
}



