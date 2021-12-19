import './App.css';
import WebSocketProvider from './websocket/WebSocketProvider';
import ChatRoom from './components/ChatRoom';
import RoomList from './components/RoomList';

const App = () => {
  return (
      <div className="App"> 
        <WebSocketProvider>
          <RoomList/>
          <br/>
          <ChatRoom/>
        </WebSocketProvider>
      </div>
  );
}

export default App;
