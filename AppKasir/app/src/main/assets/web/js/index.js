/* 

    Nothing strengthens authority so much as silence.
    
    - Leonardo da Vinci

*/

ws = new WebSocket("ws://"+window.location.host+"/live");
ws.onopen = function() {
	console.log("WS Open");
}
ws.onmessage = function(e) {
    var data=JSON.parse(e.data);
	console.log(data.n+"---"+data.p);
	addItem(data.n, data.s, data.q, data.p, data.no);
}
ws.onclose = function() {
    alert("Closed");
    ws = null;
}
function writemsg(){
	var msg = prompt("Pesan kamu");
	if(msg!=null){
		ws.send(msg);
	}
}

function addItem(nama, s, q, p, no){
  var e=document.getElementById(no);
  if(e!=null){
    var ju=new Number(e.getAttribute("unit"))+new Number(q);
    e.setAttribute("unit", ju)
    console.log(ju);
    var h=new Number(p).toFixed();
    e.getElementsByTagName("span")[1].innerHTML=ju;
    e.getElementsByTagName("span")[2].innerHTML="Rp"+(h*ju).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1.");
    updateTotal();
    return;
  }
  var f=document.createElement("figure");
  var h=new Number(p)*new Number(q);
  f.setAttribute("id", no);
  f.setAttribute("harga", new Number(p));
  f.setAttribute("unit", new Number(q))
  f.innerHTML="<span><strong>"+nama+"</strong> ("+s+")</span><span>"+q+"</span><span>Rp"+h.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1.")+"</span>";
  document.getElementById("product").appendChild(f);
  updateTotal();
}

function updateTotal(){
  var tt=document.getElementsByTagName("main")[0].getElementsByTagName("section")[2].getElementsByTagName("span")[1];
  var p=document.getElementById("product");
  //console.log("hello "+p.childElementCount)
  var jm=0;
  for(var i=0;i<p.childElementCount;i++){
    var c=p.children[i];
    var h=new Number(c.getAttribute("harga"));
    var ju=new Number(c.getAttribute("unit"));
    jm=jm+(h*ju)
    //console.log(h+"==="+ju)
  }
  //console.log("Total "+jm)
  tt.innerHTML=("Rp"+jm.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1."))
}
