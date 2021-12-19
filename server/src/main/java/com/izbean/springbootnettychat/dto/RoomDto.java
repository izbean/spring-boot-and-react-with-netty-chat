package com.izbean.springbootnettychat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class RoomDto implements Serializable {

    private static final long serialVersionUID = -4639807474940021454L;

    private String id;

    private String name;

    private Integer attendeeCount;

}
