import React, {useState, useContext} from 'react';
import { Badge, ListGroup, ListGroupItem } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { RootState } from '../reducer/reducers';
import { Room } from '../reducer/Room';

const RoomList = () => {
    const rooms = useSelector((state: RootState) => state.roomReducer?.rooms);
    const activeRoomId = useSelector((state: RootState) => state.chatReducer?.activeRoomId);

    console.log(rooms);
    console.log(activeRoomId);

    const onClick = () => {
        alert("키키");
    }

    return (
        <ListGroup as="ul">
            {
                rooms && rooms.map((room: Room) => {
                    return (
                        <ListGroupItem as="li" 
                        className="d-flex justify-content-between align-items-start"
                        action
                        onClick={onClick}
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