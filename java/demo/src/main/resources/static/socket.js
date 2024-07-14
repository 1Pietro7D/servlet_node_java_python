// Create a new WebSocket instance connecting to the server at the specified URL
let socket = new WebSocket("ws://localhost:5003/websocket");

// Event handler for when the connection is successfully opened
socket.onopen = function (e) {
    document.getElementById("response").textContent += "Connection established\n";
};
// Connection opened
// socket.addEventListener("open", (event) => {
//     socket.send("Hello Server!");
//   });

// Event handler for when a message is received from the server
socket.onmessage = function (event) {
    // Display the received message in the <pre> element
    document.getElementById("response").textContent += "Received: " + event.data + "\n";
};

// Event handler for when the connection is closed
socket.onclose = function (event) {
    if (event.wasClean) {
        // Display a clean close message
        document.getElementById("response").textContent += `Connection closed cleanly, code=${event.code} reason=${event.reason}\n`;
    } else {
        // Display a message indicating the connection was closed abruptly
        document.getElementById("response").textContent += 'Connection died\n';
    }
};

// Event handler for when an error occurs with the WebSocket
socket.onerror = function (error) {
    // Display the error message
    document.getElementById("response").textContent += `Error: ${error.message}\n`;
};

// Function to send a message to the WebSocket server
function sendMessage() {
    // Retrieve the message from the input field
    let message = document.getElementById("message").value;

    // Send the message to the WebSocket server
    socket.send(message);

    // Display the sent message in the <pre> element
    document.getElementById("response").textContent += "Sent: " + message + "\n";
}