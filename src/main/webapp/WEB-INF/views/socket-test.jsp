<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>Chat WebSocket</title>
    <script src="<%request.getContextPath();%>/verdaflow/resources/js/sockjs-0.3.4.js"></script>
    <script src="<%request.getContextPath();%>/verdaflow/resources/js/stomp.js"></script>
    <script type="text/javascript">
      var stompClient = null;

      function setConnected(connected) {
        document.getElementById("connect").disabled = connected;
        document.getElementById("disconnect").disabled = !connected;
        document.getElementById("conversationDiv").style.visibility = connected
          ? "visible"
          : "hidden";
        document.getElementById("response").innerHTML = "";
      }

      function setConnected1(connected) {
        document.getElementById("connect1").disabled = connected;
        document.getElementById("disconnect").disabled = !connected;
        document.getElementById("conversationDiv1").style.visibility = connected
          ? "visible"
          : "hidden";
        document.getElementById("response").innerHTML = "";
      }

      function setConnected2(connected) {
        document.getElementById("connect2").disabled = connected;
        document.getElementById("disconnect").disabled = !connected;
        document.getElementById("conversationDiv2").style.visibility = connected
          ? "visible"
          : "hidden";
        document.getElementById("response").innerHTML = "";
      }

      function connect() {
        var socket = new SockJS("/verdaflow/chat");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
          setConnected(true);
          console.log("Connected: " + frame);
          stompClient.subscribe("/topic/messages", function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
          });
        });
      }

      function connectRoom1() {
        var socket = new SockJS("/verdaflow/chat");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
          setConnected1(true);
          console.log("Connected: " + frame);
          stompClient.subscribe("/topic/room/101", function (messageOutput) {
            showMessageOutputRoom1(JSON.parse(messageOutput.body));
          });
        });
      }

      function connectRoom2() {
        var socket = new SockJS("/verdaflow/chat");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
          setConnected2(true);
          console.log("Connected: " + frame);
          stompClient.subscribe("/topic/room/102", function (messageOutput) {
            showMessageOutputRoom2(JSON.parse(messageOutput.body));
          });
        });
      }

      function disconnect() {
        if (stompClient != null) {
          stompClient.disconnect();
        }
        setConnected(false);
        setConnected1(false);
        setConnected2(false);
        console.log("Disconnected");
      }

      function sendMessage() {
        var from = document.getElementById("from").value;
        var text = document.getElementById("text").value;
        stompClient.send(
          "/app/chat",
          {},
          JSON.stringify({
            from: from,
            text: text,
          }),
        );
      }

      function sendMessageRoom1() {
        var from = document.getElementById("from").value;
        var text = document.getElementById("text1").value;
        stompClient.send(
          "/app/room/greeting/101",
          {},
          JSON.stringify({
            from: from,
            text: text,
          }),
        );
      }
      function sendMessageRoom2() {
        var from = document.getElementById("from").value;
        var text = document.getElementById("text2").value;
        stompClient.send(
          "/app/room/greeting/102",
          {},
          JSON.stringify({
            from: from,
            text: text,
          }),
        );
      }

      function showMessageOutput(messageOutput) {
        var response = document.getElementById("response");
        var p = document.createElement("p");
        p.style.wordWrap = "break-word";
        p.appendChild(
          document.createTextNode(
            messageOutput.from +
              ": " +
              messageOutput.text +
              " (" +
              messageOutput.time +
              ")",
          ),
        );
        response.appendChild(p);
      }

      function showMessageOutputRoom1(messageOutput) {
        var response = document.getElementById("response1");
        var p = document.createElement("p");
        p.style.wordWrap = "break-word";
        p.appendChild(
          document.createTextNode(
            messageOutput.from +
              ": " +
              messageOutput.text +
              " (" +
              messageOutput.time +
              ")",
          ),
        );
        response.appendChild(p);
      }

      function showMessageOutputRoom2(messageOutput) {
        var response = document.getElementById("response2");
        var p = document.createElement("p");
        p.style.wordWrap = "break-word";
        p.appendChild(
          document.createTextNode(
            messageOutput.from +
              ": " +
              messageOutput.text +
              " (" +
              messageOutput.time +
              ")",
          ),
        );
        response.appendChild(p);
      }
    </script>
  </head>
  <body onload="disconnect()">
    <div>
      <div>
        <input type="text" id="from" placeholder="Choose a nickname" />
      </div>
      <br />
      <div>
        <button id="connect" onclick="connect()">Connect</button>

        <button id="connect1" onclick="connectRoom1()">Connect Room1</button>

        <button id="connect2" onclick="connectRoom2()">Connect Room2</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect()">
          Disconnect
        </button>
      </div>
      <br />
      <div id="conversationDiv">
        <input type="text" id="text" placeholder="Write a message..." />
        <button id="sendMessage" onclick="sendMessage()">Send</button>
        <p id="response"></p>
      </div>

      <div id="conversationDiv1">
        <input
          type="text"
          id="text1"
          placeholder="Write a message to room1 ."
        />
        <button id="sendMessage1" onclick="sendMessageRoom1()">Send</button>
        <p id="response1"></p>
      </div>

      <div id="conversationDiv2">
        <input
          type="text"
          id="text2"
          placeholder="Write a message to room2..."
        />
        <button id="sendMessage2" onclick="sendMessageRoom2()">Send</button>
        <p id="response2"></p>
      </div>
    </div>
  </body>
</html>
