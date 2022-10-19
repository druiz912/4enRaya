const HEADERS = {
    'Content-Type': 'application/json'
  }
const url = 'http://localhost:8081/game';
let stompClient;
let boardId;
let playerId;


function connectToSocket(boardId) {

    console.log("Conectando al juego");
    let socket = new SockJS(url + "/move");
    stompClient = Stomp.over(socket);
    console.log(stompClient);

    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + boardId, function (response) {
            let data = JSON.parse(response.body);
            showResponse();
            console.log(data);
        })
    })

}

async function crearPlayer1(){
    const inputPlayer1 = document.getElementById("player").value;
    const form = document.getElementById("formTable")
    const btn = document.getElementById("btn")
    const formTable = document.getElementById("formTable");

       const res = await fetch('http://localhost:8081/game/createGame', {
            method:'POST',
            headers: HEADERS,
            body:JSON.stringify({
                userPlayer:inputPlayer1
            })
        })
        console.log(res.status)


        const data = await res.json();

        if(res.status===200){
            connectToSocket(data.id)
                 if(formTable.style.display === "none"){
                        formTable.style.display = "block";
                 if(btn.textContent==="Crear") btn.textContent = "Creado";

                 if(data){
                     function socket(data) {
                                         boardId = data.id;
                                         playerId = playerId;
                                         connectToSocket(data.id);
                                         alert("Your created a game. Game id is: " + data.id);
                                         gameOn = true;
                                     }
                            socket(data);
                         }
                    }
                }

}

async function crearPlayer2(){
    const inputPlayer2 = document.getElementById("player").value;
    const boardId = document.getElementById("boardId").value;
    parseInt(boardId);
    const btnJoin = document.getElementById("btnJoin")

       const res = await fetch('http://localhost:8081/game/connect/{boardId}', {
            method:'POST',
            headers: HEADERS,
            body:JSON.stringify({
                userPlayer:inputPlayer2
            })
        })

        const data = await res.json();
        if(res.status===200){
           if(btnJoin.textContent==="Conectar") btnJoin.textContent = "Conectado";
            alert("Congrats you're playing on board: " + data.id);

            if(data){
                     function socket(data) {
                                      boardId = data.id;
                                      playerType = 'RED';
                                      connectToSocket(data.id);
                                      console.log(data)
                                       gameOn = true;
                                   }
                                       socket(data);
                                    }
            }

}

async function moveUser(){



    const res = await fetch('http://localhost:8081/game/move', {
                method:'POST',
                headers: HEADERS,
                body:JSON.stringify({
                    idPlayer:playerId,
                    boardId:"0",
                    column:"55"
                })
            })
    const data = await res.json();

    if(data){
    showResponse();
    }
    console.log(data)

}

function showResponse(){
console.log("Clickeado")
    const circle1 = document.getElementById("1")
    const circle2 = document.getElementById("2")
    const circle3 = document.getElementById("3")
    const circle4 = document.getElementById("4")
    const circle5 = document.getElementById("5")
    const circle6 = document.getElementById("6")
    const circlesArray = [circle1,circle2,circle3,circle4,circle5,circle6]

    let counterIndex = 0;

    for(i=0; i < circlesArray.length ; i++){
            if(circlesArray[i].style.background==="white"){
                counterIndex++;
                if(circlesArray[i].style.background==="red"){
                          counterIndex--;
                          }
            }

        }

    switch(counterIndex){
        case 6:
            circle6.style.background="red";
            break;
        case 5:
             circle5.style.background="red";
             break;
        case 4:
             circle4.style.background="red";
             break;
        case 3:
             circle3.style.background="red";
             break;
        case 2:
              circle2.style.background="red";
              break;
        case 1:
              circle1.style.background="red";
              break;
        default:
               alert("Esta columna está llena");
               break;

    }
}