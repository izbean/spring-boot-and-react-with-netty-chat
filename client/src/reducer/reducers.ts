import { combineReducers } from 'redux';
import { ChatMessageCommand } from './ChatMessage';

const initialState = {
    chats: Array(),
    attendees: Array(),
    nickname: null
}

const chatReducer = (state = initialState, action: any) => {
    let newChats = state.chats.slice();
    let newAttendees = state.attendees.slice();
    let payload = action.payload;

    switch(action.type) {
        case ChatMessageCommand.FROM:
            newChats.push(payload);
            return {...state, chats: newChats};
        case ChatMessageCommand.HELLO:
            newChats.push(payload);
            newAttendees.push(payload.nickname);
            return {...state, chats: newChats, attendees: newAttendees, nickname: payload.nickname};
        case ChatMessageCommand.JOIN:
            newChats.push(payload);
            newAttendees.push(payload.nickname);
            return {...state, chats: newChats, attendees: newAttendees};
        case ChatMessageCommand.HAVE:
            newAttendees.push(payload.nickname);
            return {...state, chats: newChats, attendees: newAttendees};
        case ChatMessageCommand.NICK:
            newChats.push(payload);
            newAttendees.splice(newAttendees.indexOf(payload.nickname), 1, payload.body);

            if (state.nickname == payload.nickname)
                return {...state, chats: newChats, attendees: newAttendees, nickname: payload.body};
            else
                return {...state, chats: newChats, attendees: newAttendees};
        case ChatMessageCommand.LEFT:
            newChats.push(payload);
            newAttendees.splice(newAttendees.indexOf(payload.nickname), 1);
            return {...state, chats: newChats, attendees: newAttendees};
        default:
            return state;
    }
}

const rootReducer = combineReducers({chatReducer});

export default rootReducer;

export type RootState = ReturnType<typeof rootReducer>;