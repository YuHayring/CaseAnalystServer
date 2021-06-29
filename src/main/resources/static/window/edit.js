var currentNode;

var currentNodeId;



function getFieldHtml(fieldName, fieldLabel, val) {
    let html =  "<div class=\"layui-form-item\"><label class=\"layui-form-label mylabel\">"
        + fieldName +
        "</label><div class=\"layui-input-block myblock\"><input type=\"text\" id=\""
        + fieldLabel +
        "\"";
    if (val != null) html+= " value=\"" + val + "\"";
    html += " class=\"layui-input\"></div></div>"
    return html;

}


function getHtmlByNode() {
    let html = "";
    for (let key in currentNode) {
        if (key === "id" || key === "label") continue;
        html += getFieldHtml(getTranslations(key), key, currentNode[key]);
    }
    return html;

}


function loadNode(id) {
    if (typeof id == "string") {
        id = parseInt(id);
    }
    if (currentNodeId === id) return;
    let url = "/case/" + parent.currentCaseId + "/node/" + id;
    request(url, null, getNodeSuccess, "get");
}

function getNodeSuccess() {
    if (xmlhttp.status === 404) {
        parent.alert("没有该节点！")
    } else if (xmlhttp.status === 200) {
        let data = JSON.parse(xmlhttp.responseText);
        currentNode = data.node;
        currentNode.label = data.label.toLowerCase();
        document.getElementById("type").innerText = getTranslations(currentNode.label);
        document.getElementById("fields").innerHTML = getHtmlByNode();
        document.getElementById("save").hidden = false;
        currentNodeId = currentNode.id;
    }
}


function save() {
    if (currentNode === null) return;

    let data = new FormData();
    let inputSet = document.getElementById("fields").getElementsByTagName("input");
    for (let i = 0; i < inputSet.length; i++) {
        if (inputSet[i].id === "dob" && inputSet[i].value !== "") {
            let reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
            let regExp = new RegExp(reg);
            if(!regExp.test(inputSet[i].value)){
                alert("日期格式不正确，正确格式为：2014-01-01");
                return;
            }
        }
        if (inputSet[i].id === "identity" && inputSet[i].value !== "") {
            if (!checkIDCard(inputSet[i].value)) {
                alert("身份证号码格式有误！");
                return;
            }
        }
        data.append(inputSet[i].id, inputSet[i].value);
    }
    data.append("id", currentNodeId);
    let url = "/" + currentNode.label + "/" + currentNodeId;
    request(url, data, saveSuccess, "put");
}


function saveSuccess() {
    if (xmlhttp.status === 204) {
        alert("更新成功！");
        let name = document.getElementById("name").value;
        parent.cy.getElementById(currentNodeId.toString()).data("name",name);
        parent.nodeSelectorFrame.loadNodeByPageNum(parent.nodeSelectorFrame.pageNo);
    }
    else alert("更新失败");
}

function getTranslations(eng) {
    switch(eng) {
        case "person":return "人物";
        case "thing":return "物品";
        case "place":return "地点";
        case "event":return "事件";

        case "type":return "类型";
        case "brand":return "品牌";
        case "name":return "名称";
        case "dob":return "生日";
        case "info":return "信息";
        case "identity" :return "身份证";
        case "level" :return "保密级别";
        default : return eng;
    }
}

function createNode() {
    parent.layui.use('layer', function () {
        let layer = parent.layui.layer;
        let index = layer.confirm('请选择结点类型：', {
            btn: ["人物", "物品", "地点", "事件"], //可以无限个按钮
            btn4: function () {
                create("event")
            },
            btn3:function () {
                create("place")
            },
            btn2 :function(index){
                create("thing")
            },
            btn1: function () {
                create("person")
                layer.close(index);
                return true;
            }
        });

    })
}

function create(label) {
    let url = "/case/" +parent.currentCaseId+"/" + label;
    request(url, null, function () {
        createSuccess(label)
    }, "post");

}

function createSuccess(label) {
    if (xmlhttp.status === 200) {
        let node = JSON.parse(xmlhttp.responseText);
        if (!parent.nodeEditorOpened) parent.openNodeEditor();
        parent.cy.add({group: 'nodes', data: { id: node.id.toString(), name :  node.name, label:label }, position: { x: 1000*Math.random(), y: 700*Math.random() }});
        currentNode = node;
        currentNode.label = label;
        document.getElementById("type").innerText = getTranslations(currentNode.label);
        document.getElementById("fields").innerHTML = getHtmlByNode();
        document.getElementById("save").hidden = false;
        currentNodeId = currentNode.id;
    } else {
        alert("创建失败")
    }
}

function deleteNode() {
    if (currentNode === null) return;
    let url = "/case/" +parent.currentCaseId+"/node/"+currentNodeId;
    request(url, null, deleteSuccess, "delete");
}

function deleteSuccess() {
    if (xmlhttp.status === 204) {
        alert("删除成功")
        parent.cy.remove('node[id = "'+currentNodeId+'"]');
        if (parent.nodeSelectorOpened) parent.nodeSelectorFrame.loadNodesFirstByPage();
        document.getElementById("type").innerText = "请先选择一个节点";
        document.getElementById("fields").innerHTML = "";
    } else if (xmlhttp.status === 403) {
        alert("删除失败！结点还有其他关系相连，无法删除！")
    }
}

function checkIDCard(idcode){
    // 加权因子
    var weight_factor = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
    // 校验码
    var check_code = ['1', '0', 'X' , '9', '8', '7', '6', '5', '4', '3', '2'];

    var code = idcode + "";
    var last = idcode[17];//最后一位

    var seventeen = code.substring(0,17);

    // ISO 7064:1983.MOD 11-2
    // 判断最后一位校验码是否正确
    var arr = seventeen.split("");
    var len = arr.length;
    var num = 0;
    for(var i = 0; i < len; i++){
        num = num + arr[i] * weight_factor[i];
    }

    // 获取余数
    var resisue = num%11;
    var last_no = check_code[resisue];

    // 格式的正则
    // 正则思路
    /*
    第一位不可能是0
    第二位到第六位可以是0-9
    第七位到第十位是年份，所以七八位为19或者20
    十一位和十二位是月份，这两位是01-12之间的数值
    十三位和十四位是日期，是从01-31之间的数值
    十五，十六，十七都是数字0-9
    十八位可能是数字0-9，也可能是X
    */
    var idcard_patter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;

    // 判断格式是否正确
    var format = idcard_patter.test(idcode);

    // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
    return last === last_no && format ? true : false;
}