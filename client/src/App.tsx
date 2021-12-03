import './App.css';
import WebSocketProvider from './websocket/WebSocketProvider';
import ChatRoom from './components/ChatRoom';
import TextInputBox from './components/TextInputBox';

const App =  () => {
  return (
    <div className="App">
      <WebSocketProvider>
        <ChatRoom/>
        <br/>
        <TextInputBox/>
      </WebSocketProvider>
    </div>
  );
}

export default App;
