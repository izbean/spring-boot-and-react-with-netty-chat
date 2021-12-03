import React, {useContext, useState} from 'react';
import { WebSocketContext } from '../websocket/WebSocketProvider';
import { Container, Row, Col } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'

interface ChatMessage {
    command: string,
    nickname: string,
    body: string
}

const ChatRoom = () => {
    const ws = useContext(WebSocketContext);
    const [items, setItems] = useState<ChatMessage[]>([]);
    const [attendees, setAttendees] = useState<ChatMessage[]>([]);

    const addItem = (item: ChatMessage) => {
        setItems([
            ...items,
            item
        ]);
    };

    const addAttendies = (attendee: ChatMessage) => {
        setAttendees([
            ...attendees,
            attendee
        ]);
    }

    ws.current.onmessage = (evt: MessageEvent) => {
        const data = JSON.parse(evt.data);
        console.log(data);
        switch (data.command) {
            case "JOIN":
            case "HAVE":
                addAttendies(data);
                break;
            case "FROM":
                addItem(data);

                let chatBody = document.getElementById('chats');

                if (chatBody) chatBody.scrollTop = chatBody?.scrollHeight;

                break;
            case "HELO":
                addAttendies(data);
                break;
        }
    }

    return (
        <Container>
            <Row>
                <Col id="chats" style={{height: "90vh", overflowY: "scroll"}} sm={8}>
                    {
                        items.map((message) => {
                            return (
                                <div style={{textAlign: "left"}}><cite style={{marginRight: "10px"}}>{message.nickname}</cite>{message.body}</div>
                            )
                        })
                    }
                </Col>
                <Col style={{height: "30vh", overflowY: "scroll"}} sm={4}>
                    <div style={{textAlign: "center", borderBottom: "1px black solid"}}>참여자 목록</div>
                    {
                        attendees.map((attendee) => {
                            return (
                                <div>{attendee.nickname}&nbsp;
                                     {attendee.command == "HELO" ? <FontAwesomeIcon icon={faUser} /> : null}
                                </div>
                                
                            )
                        })
                    }
                </Col>
            </Row>
        </Container>
    );
}

export default ChatRoom;