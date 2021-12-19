import React, { useRef } from 'react';
import { useDispatch } from 'react-redux';
import { receive } from '../reducer/actions'
import { Payload, PayloadCommand } from '../reducer/ChatMessage';

const WebSocketContext = React.createContext<any>(null);
export { WebSocketContext };

const WebSocketProvider = ({children}: {children: React.ReactNode}) => {
    const webSocketUrl = 'ws://localhost:8000/ws';
    let ws = useRef<WebSocket | null>(null);

    const dispatch = useDispatch();

    if (!ws.current) {
        ws.current = new WebSocket(webSocketUrl);
        ws.current.onopen = () => {
            let sendMessage: Payload = {
                command: PayloadCommand.HELLO, 
                body: null
            };
            
            ws.current?.send(JSON.stringify(sendMessage));
            console.log('connected to ' + webSocketUrl);

            let sendMessage2: Payload = {
                command: PayloadCommand.RELOAD_ROOM_LIST, 
                body: null
            };
            
            ws.current?.send(JSON.stringify(sendMessage2));
        }
        ws.current.onclose = error => {
            console.log('disconnect from ' + webSocketUrl);
        }
        ws.current.onerror = error => {
            console.log('connection error' + webSocketUrl);
        }
        ws.current.onmessage = (evt: MessageEvent) => {
            const data = JSON.parse(evt.data);
            dispatch(receive(data));
        }
    }

    return (
        <WebSocketContext.Provider value={ws}>
            {children}
        </WebSocketContext.Provider>
    );
};

export default WebSocketProvider;