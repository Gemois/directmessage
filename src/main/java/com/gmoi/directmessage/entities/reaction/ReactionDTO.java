package com.gmoi.directmessage.entities.reaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionDTO {

    private String emoji;
    private int count;
    private String url;
}
