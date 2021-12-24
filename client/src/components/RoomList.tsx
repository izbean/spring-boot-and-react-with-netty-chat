import { useContext } from 'react';
import { Badge, ListGroup, ListGroupItem } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { Payload, PayloadCommand } from '../reducer/ChatMessage';
import { EnterRoom } from '../reducer/EnterRoom';
import { RootState } from '../reducer/reducers';
import { Room } from '../reducer/Room';
import { WebSocketContext } from '../websocket/WebSocketProvider';

const RoomList = () => {
    const rooms = useSelector((state: RootState) => state.roomReducer?.rooms);
    const activeRoomId = useSelector((state: RootState) => state.chatReducer?.activeRoomId);
    const ws = useContext(WebSocketContext);

    const onClickHandle = (e: any) => {
        const payload = {
            command: PayloadCommand.ENTER_ROOM,
            body: {
                id: e.target.getAttribute("data-room-id")
            } as EnterRoom
        } as Payload;

        ws.current.send(JSON.stringify(payload));
    }

    return (
        <ListGroup as="ul">
            {
                rooms && rooms.map((room: Room) => {
                    return (
                        <ListGroupItem as="li" 
                        className="d-flex justify-content-between align-items-start"
                        action
                        onClick={onClickHandle}
                        data-room-id={room.id}
                        {...activeRoomId == room.id && {"active": true}}>
                            {room.name}
                            <Badge pill>
                                {room.attendeeCount}
                            </Badge>
                        </ListGroupItem>
                    );
                })
            }
        </ListGroup>
    );
}


export default RoomList;