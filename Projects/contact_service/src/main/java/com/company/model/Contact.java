package com.company.model;

import lombok.*;

@AllArgsConstructor
@ToString(includeFieldNames = false)
@EqualsAndHashCode(of = "phoneNumber")
@Getter
public class Contact {
    private @NonNull String fullName;
    private @NonNull String phoneNumber;
}
