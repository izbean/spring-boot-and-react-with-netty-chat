import React, {useState, useContext} from 'react';
import { WebSocketContext } from '../websocket/WebSocketProvider';
import { Container, Row, Col, Button, InputGroup, FormControl } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { RootState } from '../reducer/reducers';
import { Payload, PayloadCommand, Message } from '../reducer/ChatMessage';

const TextInputBox = () => {
    const [message, setMessage] = useState('');
    const activeRoomId = useSelector((state: RootState) => state.chatReducer.activeRoomId);
    
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
        let sendMessage: Payload = {
            command: PayloadCommand.NICK,
            body: {
                nickname: null,
                message: viewNickname
            }
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
        let sendMessage: Payload = {
            command: PayloadCommand.SEND, 
            body: {
                roomId: activeRoomId,
                nickname: null,
                message: message
            }
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