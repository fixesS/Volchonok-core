package com.eduard.volchonokcore.web.models;

import com.eduard.volchonokcore.database.entities.User;
import lombok.*;
import org.springframework.lang.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewModel {
    private Integer review_id;
    private String firstname;
    private String surname;
    private String middlename;
    private String avatar;

    @NonNull
    private String text;
}
