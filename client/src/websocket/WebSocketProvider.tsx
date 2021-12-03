import React, { useRef } from 'react';

const WebSocketContext = React.createContext<any>(null);
export { WebSocketContext };

const WebSocketProvider = ({children}: {children: React.ReactNode}) => {
    const webSocketUrl = 'ws://localhost:8000/ws';
    let ws = useRef<WebSocket | null>(null);

    if (!ws.current) {
        ws.current = new WebSocket(webSocketUrl);
        ws.current.onopen = () => {
            ws.current?.send(JSON.stringify({
                command: 'HELO',
                nickname: null,
                body: null
             }));
            console.log('connected to ' + webSocketUrl);
        }
        ws.current.onclose = error => {
            console.log('disconnect from ' + webSocketUrl);
        }
        ws.current.onerror = error => {
            console.log('connection error' + webSocketUrl);
        }
    }

    return (
        <WebSocketContext.Provider value={ws}>
            {children}
        </WebSocketContext.Provider>
    );
};

export default WebSocketProvider;