<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Chat WebSocket</title>
<script src="<%=request.getContextPath()%>/resources/js/sockjs-0.3.4.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/stomp.js"></script>
<script type="text/javascript">
	var stompClient = null;
	var connectedStatus = false;

	function connect() {
		var socket = new SockJS('<%=request.getContextPath()%>/locate');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);
			onConnected();
		});
	}

	function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		onDisconnected();
		console.log("Disconnected");
	}

	function onConnected() {
		connectedStatus = true;

		document.getElementById('connect').style.visibility = 'hidden';
		document.getElementById('disconnect').style.visibility = 'visible';

		document.getElementById('subscribeDispatcherDiv').style.visibility = 'visible';
		document.getElementById('subscribeDriverDiv').style.visibility = 'visible';
		document.getElementById('sendDriverLocationDiv').style.visibility = 'visible';

		document.getElementById('responseList').innerHTML = '';
	}

	function onDisconnected() {
		connectedStatus = false;

		document.getElementById('connect').style.visibility = 'visible';
		document.getElementById('disconnect').style.visibility = 'hidden';

		document.getElementById('subscribeDispatcherDiv').style.visibility = 'hidden';
		document.getElementById('subscribeDriverDiv').style.visibility = 'hidden';
		document.getElementById('sendDriverLocationDiv').style.visibility = 'hidden';

		document.getElementById('responseList').innerHTML = '';
	}

	function subscribeDispatcher() {
		if (!connectedStatus) {
			alert("Not connected to Socket");
			return;
		}

		var dispatcherId = document
				.getElementById("subscribeDispatcherDispatcherId").value;
		console.log('subscribeDispatcher: /publish/location/dispatcher/'
				+ dispatcherId);

		if (dispatcherId == null || dispatcherId == 0) {
			alert("Please enter valid dispatcher Id");
			return;
		}

		stompClient.subscribe('/publish/location/dispatcher/' + dispatcherId,
				function(messageOutput) {
					showLocation(JSON.parse(messageOutput.body));
				});

		onSubscribeDispatcher();
	}

	function onSubscribeDispatcher() {
		if (!connectedStatus) {
			alert("Not connected to Socket");
			return;
		}

		document.getElementById('subscribeDispatcherDiv').style.visibility = 'visible';
		document.getElementById('subscribeDriverDiv').style.visibility = 'hidden';
		document.getElementById('sendDriverLocationDiv').style.visibility = 'hidden';

		document.getElementById('responseList').innerHTML = '';
	}

	function subscribeDriver() {
		if (!connectedStatus) {
			alert("Not connected to Socket");
			return;
		}

		var dispatcherId = document
				.getElementById("subscribeDriverDispatcherId").value;
		var driverId = document.getElementById("subscribeDriverDriverId").value;

		if (dispatcherId == null || dispatcherId == 0) {
			alert("Please enter valid dispatcher Id");
			return;
		}
		if (driverId == null || driverId == 0) {
			alert("Please enter valid driver Id");
			return;
		}

		console.log('subscribeDriver: /publish/location/dispatcher/'
				+ dispatcherId + '/driver/' + driverId);
		console.log('subscribeDriver: /publish/route/dispatcher/'
				+ dispatcherId + '/driver/' + driverId);

		stompClient.subscribe('/publish/location/dispatcher/' + dispatcherId
				+ '/driver/' + driverId, function(messageOutput) {
			showLocation(JSON.parse(messageOutput.body));
		});

		stompClient.subscribe('/publish/route/dispatcher/' + dispatcherId
				+ '/driver/' + driverId, function(messageOutput) {
			showRoute(JSON.parse(messageOutput.body));
		});

		onSubscribeDriver();
	}

	function onSubscribeDriver() {
		document.getElementById('subscribeDispatcherDiv').style.visibility = 'hidden';
		document.getElementById('subscribeDriverDiv').style.visibility = 'visible';
		document.getElementById('sendDriverLocationDiv').style.visibility = 'hidden';

		document.getElementById('responseList').innerHTML = '';
	}

	function sendDriverLocation() {
		var dispatcherId = document
				.getElementById("sendDriverLocationDispatcherId").value;
		var driverId = document.getElementById("sendDriverLocationDriverId").value;

		if (dispatcherId == null || dispatcherId == 0) {
			alert("Please enter valid dispatcher Id");
			return;
		}
		if (driverId == null || driverId == 0) {
			alert("Please enter valid driver Id");
			return;
		}

		var lat = document.getElementById('lat').value;
		var lng = document.getElementById('lng').value;
		var rotation = document.getElementById('rotation').value;

		/* 	stompClient.send("/app/dispatcher/" + dispatcherId, {}, JSON
					.stringify({
						'lat' : lat,
						'lng' : lng,
						'rotation' : rotation
					})); */

		stompClient.send("/app/send/location/driver", {}, JSON.stringify({
			'dispatcherId' : dispatcherId,
			'driverId' : driverId,
			'lat' : lat,
			'lng' : lng,
			'rotation' : rotation
		}));
	}

	function showLocation(messageOutput) {
		var response = document.getElementById('responseList');
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		p.appendChild(document.createTextNode("dispatcherId:"
				+ messageOutput.dispatcherId + " driverId:"
				+ messageOutput.driverId + " ------> lat:" + messageOutput.lat
				+ ", lng:" + messageOutput.lng + ", rotation:"
				+ messageOutput.rotation));
		response.appendChild(p);
	}

	function showRoute(messageOutput) {
		var response = document.getElementById('responseList');
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		p.appendChild(document.createTextNode("dispatcherId:"
				+ messageOutput.dispatcherId + " driverId:"
				+ messageOutput.driverId + " ------> route:"
				+ messageOutput.route));
		response.appendChild(p);
	}
</script>
</head>
<body onload="disconnect()">
	<div>
		<div>
			<button id="connect" onclick="connect();" style="margin-right: 40px;">Connect</button>
			<button id="disconnect" onclick="disconnect();">Disconnect</button>
			</br> </br>
			<div id="subscribeDispatcherDiv">
				<button id="subscribeDispatcher" onclick="subscribeDispatcher();">Subscribe
					All Drivers of a Dispatcher</button>
				<input type="text" id="subscribeDispatcherDispatcherId"
					placeholder="Enter dispatcher id" style="margin-left: 20px;" /> </br> </br>
			</div>
			<div id="subscribeDriverDiv">
				<button id="subscribeDriver" onclick="subscribeDriver();">Subscribe
					Single Driver of a Dispatcher</button>
				<input type="text" id="subscribeDriverDispatcherId"
					placeholder="Enter dispatcher id" style="margin-left: 20px;" /> <input
					type="text" id="subscribeDriverDriverId"
					placeholder="Enter driver id" style="margin-left: 20px;" /> </br> </br> </br> </br>
			</div>
			<div id="sendDriverLocationDiv">
				<button id="sendDriverLocation" onclick="sendDriverLocation();">Send
					Driver Location</button>
				<input type="text" id="sendDriverLocationDispatcherId"
					placeholder="Enter dispatcher id" style="margin-left: 20px;" /> <input
					type="text" id="sendDriverLocationDriverId"
					placeholder="Enter driver id" style="margin-left: 20px;" /> <input
					type="text" id="lat" placeholder="Enter Latitude"
					style="margin-left: 20px;" /> <input type="text" id="lng"
					placeholder="Enter Longitude" style="margin-left: 20px;" /> <input
					type="text" id="rotation" placeholder="Enter Rotation"
					style="margin-left: 20px;" /> </br> </br>
			</div>

		</div>
		<p id="responseList"></p>
	</div>
</body>
</html>