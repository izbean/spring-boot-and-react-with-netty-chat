package com.izbean.springbootnettychat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class JoinRoomDto implements Serializable {
    private static final long serialVersionUID = 4157810518336078346L;

    private String id;

    private String name;

    private List<String> attendees;

}
