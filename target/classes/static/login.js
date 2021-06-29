function login() {
    var url = "investigator/session";
    // var data = "username="+document.getElementById("investigatorId").value+
    //     "&password="+document.getElementById("password").value;

    var data = new FormData();
    data.append("username", document.getElementById("investigatorId").value);
    data.append("password", document.getElementById("password").value);
    request(url, data, loginSuccess, "post");
}

function loginSuccess() {
    switch (xmlhttp.status) {
        case 204: {
            alert("登录成功");
            console.log(xmlhttp.responseText);
            // var response = JSON.parse(xmlhttp.responseText);
            // sessionStorage.setItem("key_token", "Bearer" + response.token);
            // sessionStorage.setItem("id", response.id);
            window.location.href = "./index.html";

        }
            break;
        case 401: {
            alert("用户名或密码错误");
        }
            break;
        case 500: {
            alert("服务器错误")
        }
            break;
        default: {
            alert("错误！状态码：" + xmlhttp.status);
        }

    }
}

function logout() {
    var url = "investigator/session";
    // var data = "username="+document.getElementById("investigatorId").value+
    //     "&password="+document.getElementById("password").value;
    request(url, null, logoutSuccess, "delete");
}

function logoutSuccess() {
    switch (xmlhttp.status) {
        case 204: {
            window.location.href = "./login.html";
        }break;
        case 500: {
            alert("服务器错误")
        }
            break;
        default: {
            alert("错误！状态码：" + xmlhttp.status);
        }

    }
}