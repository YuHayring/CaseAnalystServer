function Queue() {
    this.dataStore=[];
    this.enqueue=enqueue;
    this.dequeue=dequeue;
    this.dequeue2=dequeue2;//优先级队列
    this.front=front;
    this.back=back;
    this.toString=toString;
    this.toStringObj=toStringObj;//打印元素是对象的队列
    this.empty=empty;
    this.count=count;
}

function enqueue(element) {
    this.dataStore.push(element);

}
function dequeue() {
    return this.dataStore.shift();
}
function dequeue2() {
    var priority=0;
    for(var i=1;i<this.dataStore.length;i++){
        if(this.dataStore[i].code<this.dataStore[priority].code){
            priority=i;
        }
    }
    return this.dataStore.splice(priority,1);
}
function front() {
    return this.dataStore[0];
}
function back() {
    return this.dataStore[this.dataStore.length-1];
}
function toString() {
    var retStr="";
    for(var i=0;i<this.dataStore.length;++i){
        retStr+=this.dataStore[i]+"\n";
    }
    return retStr;
}
function toStringObj() {
    var retStr="";
    for(var i=0;i<this.dataStore.length;i++){
        retStr+="name:"+this.dataStore[i].name+",code:"+this.dataStore[i].code+"\n";
    }
    return retStr;
}
function count() {
    return this.dataStore.length;
}

function empty() {
    if(this.dataStore.length===0){
        return true;
    }else{
        return false;
    }
}
