package org.chatting.common.message;

import java.io.Serializable;

public enum MessageType implements Serializable {
    LOGIN, LOGIN_RESULT,
    SIGN_UP, SIGN_UP_RESULT,
    USER_DISCONNECT,
    USER_LIST,
    USER_SEND_MESSAGE, CHAT_MESSAGE,
    USER_STATISTICS
}
