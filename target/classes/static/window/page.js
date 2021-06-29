var nodeData;

var newNode;

var currentLabel = "person";

var pageNo;

function loadNodesFirstByPage() {
    let url = "/case/"+ parent.currentCaseId +"/" + currentLabel +"s"
    request(url,null, getNodesSuccessFirst,"get")
}


function getNodesSuccessFirst() {
    if (xmlhttp.status === 200) {
        nodeData = JSON.parse(xmlhttp.responseText);
        layui.use(['laypage', 'layer'], function() {
            var laypage = layui.laypage;
            laypage.render({
                elem: 'pager'
                , count: nodeData.total
                , jump: loadNodesByPage
            });
        });
        document.getElementById('node_list').innerHTML = getNodeList(nodeData.content);

    } else if (xmlhttp.status === 404) {
        document.getElementById('node_list').innerHTML = "";
    }

}

function getNodesSuccess() {
    if (xmlhttp.status === 200) {
        nodeData = JSON.parse(xmlhttp.responseText);

        document.getElementById('node_list').innerHTML = getNodeList(nodeData.content);
    } else if (xmlhttp.status === 404) {
        document.getElementById('node_list').innerHTML = "";
    }
}

function getNodeList(content){
    let arr = [];
    layui.each(content, function(index, item){
        if (parent.cy.getElementById(item.id.toString()).length === 0) {
            arr.push('<li><button onclick="onNodeClick(this)" class="layui-btn layui-btn-primary layui-btn-radius neo4jnode" id="' + index + '" >' + item.name + '</button></li>');
        } else {
            arr.push('<li><button onclick="onNodeClick(this)" class="layui-btn layui-btn-radius neo4jnode" id="' + index + '" >' + item.name + '</button></li>');
        }
    });
    return arr.join('');
}



function loadNodesByPage(obj,first) {
    pageNo = obj.curr - 1;
    if (first) return;
    loadNodeByPageNum();
}

function loadNodeByPageNum() {
    let url = "/case/"+ parent.currentCaseId +"/" + currentLabel +"s?page="+pageNo;
    request(url,null, getNodesSuccess,"get")
}

function onNodeClick(e) {
    let index = parseInt(e.id);
    let node = nodeData.content[index];
    if (e.className.indexOf('layui-btn-primary') !== -1) {
        parent.cy.add({group: 'nodes', data: { id: node.id.toString(), name : node.name, label:currentLabel }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
        e.classList.remove("layui-btn-primary");
        newNode = node.id.toString();
        loadEdge(node.id);
    } else {
        e.classList.add("layui-btn-primary");
        parent.cy.remove('node[id = "'+ node.id + '"]')
    }
}

function loadEdge(id) {
    let url = "/case/"+ parent.currentCaseId +"/node/" + id + "/relationships";
    request(url,null, getEdgesSuccess,"get")
}

function getEdgesSuccess() {
    if (xmlhttp.status === 200) {
        let relationship = JSON.parse(xmlhttp.responseText);
        let start = relationship[0];
        let end = relationship[1];
        for (let i = 0; i < start.length; i++) {
            if (parent.cy.getElementById("e"+start[i].id.toString()).length === 0 &&
                parent.cy.getElementById(start[i].start.toString()).length !== 0) {
                parent.cy.add({ group: 'edges', data: { id: "e"+start[i].id.toString(), source: start[i].start, target: newNode, type : getType(start[i].type) } });
            }
        }
        for (let i = 0; i < end.length; i++) {
            if (parent.cy.getElementById("e"+end[i].id.toString()).length === 0 &&
                parent.cy.getElementById(end[i].end.toString()).length !== 0) {
                parent.cy.add({ group: 'edges', data: { id: "e"+end[i].id.toString(), source: newNode, target: end[i].end, type: getType(end[i].type) } });
            }
        }
    }
}

// function test() {
//     alert(parent.cy.nodes('node[id = "1"]'));
// }


function getType(s) {
    // switch (s) {
    //     case "HAPPENED_IN": return "发生于";
    //     case "BORROW": return "借";
    //     case "FRIEND": return "朋友";
    //     case "OWN": return "拥有";
    // }
    return s;
}

function labelChanged(e) {
    currentLabel = e.value;
    loadNodesFirstByPage();
}