import React, {useState, useContext} from 'react';
import { WebSocketContext } from '../websocket/WebSocketProvider';
import { Container, Row, Col, Button, InputGroup, FormControl } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { RootState } from '../reducer/reducers';
import { ChatMessage, ChatMessageCommand } from '../reducer/ChatMessage';

const TextInputBox = () => {
    const [message, setMessage] = useState('');
    
    const ws = useContext(WebSocketContext);

    const handleChangeText = (e: any) => {
        setMessage(e.target.value);
    }

    const handleChangeNickname = (e: any) => {
        setNickname(e.target.value);
    }

    const handleKeyPressNickname = (e: any) => {
        if (e.code === 'Enter')
            changeNickname();
    }

    const changeNickname = () => {
        let sendMessage: ChatMessage = {
            command: ChatMessageCommand.NICK, 
            nickname: null, 
            body: viewNickname
        };

        ws.current.send(JSON.stringify(sendMessage));
    }

    const handleClickSubmit = () => {
        sendMessage();
    }

    const handleKeyPress = (e: any) => {
        if (e.code === 'Enter')
            sendMessage();
    }

    const sendMessage = () => {
        let sendMessage: ChatMessage = {
            command: ChatMessageCommand.SEND, 
            nickname: null, 
            body: message
        };

        ws.current.send(JSON.stringify(sendMessage));
        setMessage('');
    }

    const nickname = useSelector((state: RootState) => state.chatReducer.nickname);
    const [viewNickname, setNickname] = useState('');

    return (
        <Container>
                <Row>
                    <Col sm={2}>
                        <FormControl value={!viewNickname ? nickname : viewNickname} onChange={handleChangeNickname} onKeyPress={handleKeyPressNickname}/>
                        <FormControl type="hidden" value={nickname}/>
                    </Col>
                    <Col sm={10}>
                        <InputGroup>
                            <FormControl value={message} onChange={handleChangeText} onKeyPress={handleKeyPress}/>
                            <Button variant='primary'onClick={handleClickSubmit}>전송</Button>
                        </InputGroup>
                    </Col>
                </Row>
        </Container>
    )
}

export default TextInputBox;