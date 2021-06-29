var startSelect = false;
var endSelect = false;


var startId;

var startName;

var startLabel;

var endId;

var endName;

var endLabel;

var currentRelationship;

var status = "0"; // 0 未选择 1 编辑中 2非法 3未创建

const labelMap = {
    "person":{
      "person" : "P2P",
      "thing" : "P2T",
      "place" : "P2PLACE",
    },
    "event":{
        "person" : "TAKE_PART",
        "place" : "HAPPENED_IN",
        "event" : "CAUSE",
        "thing" : "USE",
    },
    "thing":{
        "place":"APPEAR_AT",
    },
    "place":{
        "place":"NEAR",
    }
}

const fieldHTML = "<div class=\"layui-form-item\"><label class=\"layui-form-label mylabel\">类型</label><div class=\"layui-input-block myblock\">\n" +
    "<input type=\"text\" id=\"type\"  class=\"layui-input\"></div></div><div class=\"layui-form-item\">\n" +
    "<label class=\"layui-form-label mylabel\">权值</label><div class=\"layui-input-block myblock\"><input type=\"text\" id=\"weight\"  class=\"layui-input\">\n" +
    "</div></div><div class=\"layui-form-item\"><label class=\"layui-form-label mylabel\">信息</label>\n" +
    "<div class=\"layui-input-block myblock\"><input type=\"text\" id=\"info\"  class=\"layui-input\"></div></div>";

const emptyHTML = "<label class=\"layui-form-label mylabel\">&emsp;请选择结点</label>";


const illegalHTML = "<label class=\"layui-form-label mylabel\">&emsp;非法关系</label>";

function select(end) {
    if (end) {
        if (startSelect) {
            startSelect = false;
            document.getElementById("startButton").classList.add("layui-btn-primary");
        }
        if (endSelect) {
            endSelect = false;
            document.getElementById("endButton").classList.add("layui-btn-primary");
        } else {
            endSelect = true;
            document.getElementById("endButton").classList.remove("layui-btn-primary");
        }

    } else {
        if (endSelect) {
            endSelect = false;
            document.getElementById("endButton").classList.add("layui-btn-primary");
        }
        if (startSelect) {
            startSelect = false;
            document.getElementById("startButton").classList.add("layui-btn-primary");
        } else {
            startSelect = true;
            document.getElementById("startButton").classList.remove("layui-btn-primary");
        }
    }
}


function onEdgeClick(edgeId) {

    document.getElementById("startButton").innerText = startName;
    document.getElementById("endButton").innerText = endName;
    let url = "/case/"+ parent.currentCaseId +"/relationship/"+ edgeId ;
    // data.append("label", labelMap[startLabel][endLabel]);
    request(url, null, getRelationshipSuccess, "get");
}

function onNodeClick(id, name, label) {
    if (startSelect) {
        startId = id;
        startName = name;
        startLabel = label;
        document.getElementById("startButton").innerText = name;
    } else if (endSelect) {
        endId = id;
        endName = name;
        endLabel = label;
        document.getElementById("endButton").innerText = name;
    } else {
        return;
    }
    if (startId !== null && endId != null){
        if (labelMap[startLabel] !== undefined && labelMap[startLabel][endLabel] !== undefined && startId !== endId){
            let url = "/case/"+ parent.currentCaseId +"/node/"+ startId +"/relationship" +"?end=" +endId;
            // data.append("label", labelMap[startLabel][endLabel]);
            status = "0";
            request(url, null, getRelationshipSuccess, "get");
        } else {
            alert("非法关系!")
            document.getElementById("fields").innerHTML = illegalHTML;
            status = "2";
        }
    } else {
        document.getElementById("fields").innerHTML = "";
        status = "0";
    }
}

function getRelationshipSuccess() {
    if (xmlhttp.status === 200) {
        currentRelationship = JSON.parse(xmlhttp.responseText);
        document.getElementById("fields").innerHTML = fieldHTML;
        document.getElementById("type").value = currentRelationship.type;
        document.getElementById("info").value = currentRelationship.info;
        document.getElementById("weight").value = currentRelationship.weight;
        status = "1";
        if (parent.cy.getElementById("e"+currentRelationship.id.toString()).length === 0) {
            parent.cy.add({ group: 'edges', data: { id: "e"+currentRelationship.id.toString(), source: startId, target: endId, type : currentRelationship.type } });
        }
    } else if (xmlhttp.status === 403) {
        alert("非法关系！")
        document.getElementById("fields").innerHTML = illegalHTML;
        status = "2";
    } else if (xmlhttp.status === 404) {
        alert("没有关系")
        document.getElementById("fields").innerHTML = emptyHTML;
        status = "3";
    }
}

function save() {
    if (status !== "1") return;
    let url = "/case/"+ parent.currentCaseId +"/relationship/" + currentRelationship.id;
    let data = new FormData();
    data.append("type", document.getElementById("type").value);
    data.append("weight", document.getElementById("weight").value);
    data.append("info", document.getElementById("info").value);
    request(url, data, saveSuccess, "put");


}

function saveSuccess() {
    if (xmlhttp.status === 204) {
        alert("保存成功")
        currentRelationship.type = document.getElementById("type").value;
        currentRelationship.weight = document.getElementById("weight").value;
        currentRelationship.info = document.getElementById("info").value;
        parent.cy.getElementById("e"+currentRelationship.id.toString()).data("type",currentRelationship.type);
    }

    else alert("保存失败")
}

function create() {
    if (status !== "3") return;
    let url = "/case/"+ parent.currentCaseId +"/node/" + startId +"/relationship";
    let data = new FormData();
    data.append("end", endId);
    data.append("label", labelMap[startLabel][endLabel]);
    request(url, data, getRelationshipSuccess, "post");

}

function del() {
    if (status !== "1") return;
    let url = "/case/"+ parent.currentCaseId +"/relationship/" + currentRelationship.id;
    request(url, null, deleteSuccess, "delete")
}

function deleteSuccess() {
    if (xmlhttp.status === 204) {
        alert("删除成功")
        parent.cy.remove('edge[id = "e'+currentRelationship.id+'"]');
        currentRelationship = null;
        document.getElementById("fields").innerHTML = emptyHTML;
        status = 3;
    }

    else alert("删除失败")
}
