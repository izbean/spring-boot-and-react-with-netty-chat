import { Container, Row, Col } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser } from '@fortawesome/free-solid-svg-icons'
import { useSelector } from 'react-redux';
import { RootState } from '../reducer/reducers';
import { Payload, PayloadCommand, Message } from '../reducer/ChatMessage';
import TextInputBox from './TextInputBox';

import './ChatRoom.css';

const ChatRoom = () => {
    const items = useSelector((state: RootState) => state.chatReducer?.chats);
    const attendees = useSelector((state: RootState) => state.chatReducer?.attendees);
    const nickname = useSelector((state: RootState) => state.chatReducer?.nickname);

    return (
        <Container>
            <Row>
                <Col className="view" sm={8}>
                    { 
                        items && items.map((payload: Payload) => {
                            let message = payload.body as Message;
                            switch (payload.command) {
                                case PayloadCommand.HELLO:
                                    return <div className="message-info">{message.nickname}님 어서오세요!</div>;
                                case PayloadCommand.JOIN:
                                    return <div className="message-info">{message.nickname}님이 입장했어요!</div>;
                                case PayloadCommand.LEFT:
                                    return <div className="message-info">{message.nickname}님이 떠나셨어요.</div>;
                                case PayloadCommand.NICK:
                                    return <div className="message-info">{message.nickname}님이 닉네임을 변경했어요! [{message.message}]</div>;
                                case PayloadCommand.FROM:
                                    return <div className="message"><cite>{message.nickname}</cite>{message.message}</div>;
                            }
                        })
                    }
                </Col>
                <Col className="view" sm={4}>
                    <div className="attendee title">참여자 목록</div>
                    {   
                        attendees && attendees.map((attendee: string) => {
                            return (
                                <div>
                                     {attendee}&nbsp;
                                     {attendee == nickname && <FontAwesomeIcon icon={faUser}/>}
                                </div>
                                
                            )
                        })
                    }
                </Col>
            </Row>
            <Row>
                <TextInputBox/>
            </Row>
        </Container>
    );
}

export default ChatRoom;