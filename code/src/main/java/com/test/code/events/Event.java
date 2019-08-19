package com.test.code.events;

import java.time.LocalDateTime;

//@Builder @AllArgsConstructor @NoArgsConstructor
//@Getter @Setter @EqualsAndHashCode(of ="id")
//@Entity
public class Event {
// local mod dto
// modify Dto
//    @Id
//    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
//    @Enumerated(EnumType.STRING) @Builder.Default
//    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        if(this.basePrice ==0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        if("".equals(this.location) || this.location == null) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
