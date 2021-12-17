package com.izbean.springbootnettychat.config.socket.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -4541829837849037842L;

    private String message;

}
