import { combineReducers } from 'redux';
import { PayloadCommand, Message, Payload } from './ChatMessage';
import { EnterRoom } from './EnterRoom';
import { Room } from './Room';

const chatInitialState = {
    chats: Array(),
    attendees: Array(),
    nickname: null,
    activeRoomId: null
}

const roomInitialState = {
    rooms: Array()
}

const chatReducer = (state = chatInitialState, action: any) => {
    let newChats = state.chats.slice();
    let newAttendees = state.attendees.slice();

    let payload = {
        command: action.type,
        body: action.body
    } as Payload;

    let message = action.body;

    switch(action.type) {
        case PayloadCommand.ENTER_ROOM:
            return {...state, chats: Array(), attendees: message.attendees, activeRoomId: message.id}
        case PayloadCommand.FROM:
            newChats.push(payload);
            return {...state, chats: newChats};
        case PayloadCommand.HELLO:
            newChats.push(payload);
            newAttendees.push(message.nickname);
            return {...state, chats: newChats, attendees: newAttendees, nickname: message.nickname};
        case PayloadCommand.JOIN:
            newChats.push(payload);
            newAttendees.push(message.nickname);
            return {...state, chats: newChats, attendees: newAttendees};
        case PayloadCommand.HAVE:
            newAttendees.push(message.nickname);
            return {...state, chats: newChats, attendees: newAttendees};
        case PayloadCommand.NICK:
            newChats.push(payload);
            newAttendees.splice(newAttendees.indexOf(message.nickname), 1, message.message);

            if (state.nickname == message.nickname)
                return {...state, chats: newChats, attendees: newAttendees, nickname: message.message};
            else
                return {...state, chats: newChats, attendees: newAttendees};
        case PayloadCommand.LEFT:
            newChats.push(payload);
            newAttendees.splice(newAttendees.indexOf(message.nickname), 1);
            return {...state, chats: newChats, attendees: newAttendees};
        default:
            return {...state};
    }
}

const roomReducer = (state = roomInitialState, action: any) => {
    let newRooms = state.rooms.slice();

    let payload = {
        command: action.type,
        body: action.body
    } as Payload;

    switch(payload.command) {
        case PayloadCommand.CREATE_ROOM:
            newRooms.push(payload.body);
            return {...state, rooms : newRooms};
        case PayloadCommand.RELOAD_ROOM_LIST:
            newRooms = Array();

            for (let room of payload.body)
                newRooms.push(room);
                
            return {...state, rooms : newRooms};
        case PayloadCommand.RELOAD_ROOM_ATTENDEE_COUNT:
            newRooms = newRooms.filter(room => payload.body.id != room.id);
            console.log(newRooms);
            console.log(payload.body);
            newRooms.push(payload.body);
            return {...state, rooms : newRooms};
        default:
            return {...state};
    }
}

const rootReducer = combineReducers({chatReducer, roomReducer});

export default rootReducer;

export type RootState = ReturnType<typeof rootReducer>;