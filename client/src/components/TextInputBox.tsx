import React, {useState, useContext} from 'react';
import { WebSocketContext } from '../websocket/WebSocketProvider';
import { Container, Row, Col, Button, InputGroup, FormControl } from 'react-bootstrap';

const TextInputBox = () => {
    const [message, setMessage] = useState('');
    const [nickname, setNickname] = useState('');

    const ws = useContext(WebSocketContext);

    const handleChangeText = (e: any) => {
        setMessage(e.target.value);
    }

    const handleClickSubmit = () => {
        ws.current.send(JSON.stringify({
           command: 'SEND',
           nickname: null,
           body: message
        }));
        setMessage('');
    }

    const handleNicknameChangeText = (e: any) => {
        setNickname(e.target.value);
    }

    return (
        <Container>
            <InputGroup className="sm-12">
                <FormControl value={nickname} onChange={handleNicknameChangeText}/>
                <FormControl value={message} onChange={handleChangeText}/>
                <Button variant='primary'onClick={handleClickSubmit}>전송</Button>
            </InputGroup>
        </Container>
    )
}

export default TextInputBox;