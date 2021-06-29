var caseName;

function loadUserName() {
    let url = "/investigator/username"
    request(url, null, loadUserNameSuccess, "get");
}

function loadUserNameSuccess() {
    if (xmlhttp.status == 200) {
        document.getElementById("user_name_label").innerText = JSON.parse(xmlhttp.responseText);
    }
    listItem();
}


function loadCaseList() {
    let url = "/case/cases"
    request(url, null, getCaseSuccessFirst, "get");
}

function getCaseSuccessFirst() {
    if (xmlhttp.status == 200) {
        nodeData = JSON.parse(xmlhttp.responseText);
        layui.use(['laypage', 'layer'], function() {
            let laypage = layui.laypage;
            laypage.render({
                elem: 'pager'
                , count: nodeData.total
                , jump: loadCasesByPage
            });
        });
        document.getElementById('cases').innerHTML = getCaseList(nodeData.list);
    }
}

function getCasesSuccess() {
    if (xmlhttp.status == 200) {
        nodeData = JSON.parse(xmlhttp.responseText);

        document.getElementById('cases').innerHTML = getCaseList(nodeData.list);
    }
}

const caseListInnerHTML = '<table class="layui-table"  id="cases"> </table>  <div id="pager"></div> <button class="layui-btn layui-btn-warm" onclick="createCase()">创建新的案件</button>'
const tableHeader = "<tr><th>案件名</th><th>时间</th></tr>";

function getCaseList(list){
    let arr = [];
    arr.push(tableHeader);
    caseName = {};
    layui.each(list, function(index, item){
        caseName[item.id] = item.name;
        arr.push('<tr onclick="onCaseClick('+item.id+')" ><td>'+item.name+'</td><td>'+item.date+'</td></tr>');
    });
    return arr.join('');
}



function loadCasesByPage(obj,first) {
    if (first) return;
    let url = "/case/cases?page=" + obj.curr;
    request(url, null, getCasesSuccess, "get");
}


function onCaseClick(id) {
    currentCaseId = id;
    document.getElementById("caseList").id = "cy";
    document.getElementById("cy").innerHTML = "";
    document.getElementById("case_name_label").innerHTML = caseName[id];
    loadCytoscape();
}

function listItem() {
    currentCaseId = null;
    document.getElementById("case_name_label").innerHTML = "请选择一个案件";
    if (document.getElementById("cy") != null) {
        document.getElementById("cy").id = "caseList";
    }
    document.getElementById("caseList").innerHTML = caseListInnerHTML;
    loadCaseList();

}

function createCase() {
    layui.use('layer', function(){
        let layer = layui.layer;
        layer.prompt({title: '填写案件名称', formType: 2}, function(text, index){
            layer.close(index);
            let url = "/case";
            let data = new FormData();
            data.append("name", text);
            request(url, data, createCaseResponse, "post");
        });
    });

}

function createCaseResponse() {
    if (xmlhttp.status === 204) {
        alert("创建成功");
        loadCaseList();
    } else if (xmlhttp.status === 403) {
        alert("案件名称重复！")
    }
}

