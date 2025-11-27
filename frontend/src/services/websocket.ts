import {Client, type Message} from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient: Client | null = null;

export const connectWebSocket = (
    onMessageReceived: (msg: string) =>
        void) => {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = new Client({
        webSocketFactory: () => socket as never,
        reconnectDelay: 5000,
        debug: (str) => console.log(str),
    });

    stompClient.onConnect = () => {
        console.log('Connected to WebSocket');

        stompClient?.subscribe('/topic/messages', (message: Message) => {
            onMessageReceived(message.body);
        });
    };
    stompClient.activate();
};

export const sendMessage = (msg: string) => {
    if(stompClient && stompClient.connected)  {
        stompClient.publish({
            destination: '/app/chat', body: msg,
        });
    }
};