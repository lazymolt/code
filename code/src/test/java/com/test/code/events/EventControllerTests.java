package com.test.code.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.code.common.RestDocsConfiguration;
import com.test.code.common.TestDescription;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    /**
     * 정상적으로 이벤트를 생성하는 테스트
     */
    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,7,17,10,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,7,18,10,0,0))
                .beginEventDateTime(LocalDateTime.of(2019,7,19,10,0,0))
                .endEventDateTime(LocalDateTime.of(2019,7,20,10,0,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events/")
                    // 요청 생성
                    .contentType(MediaType.APPLICATION_JSON_UTF8) // 본문에 json 담아서 보내고 있음.
                    .accept(MediaTypes.HAL_JSON)    // hal json 응답을 원한다.(accept 헤더 지정)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
// 링크에 관한 부분은 아래서 하기때문에 제외
//                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_links.query-events").exists())
//                .andExpect(jsonPath("_links.update-event").exists())
//                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("create-event", // 생성하는 문서의 이름이 됨.
                        // 스니팻 생성
                        links(
                                linkWithRel("self").description("self")
                                , linkWithRel("query-events").description("query-events")
                                , linkWithRel("update-event").description("update-event")
                                , linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("content_type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name")
                                , fieldWithPath("description").description("description")
                                , fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime")
                                , fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime")
                                , fieldWithPath("beginEventDateTime").description("beginEventDateTime")
                                , fieldWithPath("endEventDateTime").description("endEventDateTime")
                                , fieldWithPath("location").description("location")
                                , fieldWithPath("basePrice").description("basePrice")
                                , fieldWithPath("maxPrice").description("maxPrice")
                                , fieldWithPath("limitOfEnrollment").description("limitOfEnrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("accept")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("content_type")
                        ),
                        //relaxed 추가 해주면 부분만 테스트 가능, 링크 정보 빠졌다고 에러남
                        responseFields(
                                fieldWithPath("id").description("id")
                                , fieldWithPath("name").description("name")
                                , fieldWithPath("description").description("description")
                                , fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime")
                                , fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime")
                                , fieldWithPath("beginEventDateTime").description("beginEventDateTime")
                                , fieldWithPath("endEventDateTime").description("endEventDateTime")
                                , fieldWithPath("location").description("location")
                                , fieldWithPath("basePrice").description("basePrice")
                                , fieldWithPath("maxPrice").description("maxPrice")
                                , fieldWithPath("limitOfEnrollment").description("limitOfEnrollment")
                                , fieldWithPath("free").description("free")
                                , fieldWithPath("offline").description("offline")
                                , fieldWithPath("eventStatus").description("eventStatus")
                                , fieldWithPath("_links.self.href").description("self")
                                , fieldWithPath("_links.query-events.href").description("query-events")
                                , fieldWithPath("_links.update-event.href").description("update-event")
                                , fieldWithPath("_links.profile.href").description("profile")
                        )));
    }

    /**
     * 입력받을 수 없는 값을 사용한 경우 이벤트를 생성하는 테스트
     */
    @Test
    @TestDescription("입력받을 수 없는 값을 사용한 경우 이벤트를 생성하는 테스트")
    public void createEvent_bad_request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,7,17,10,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,7,18,10,0,0))
                .beginEventDateTime(LocalDateTime.of(2019,7,19,10,0,0))
                .endEventDateTime(LocalDateTime.of(2019,7,20,10,0,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                // 요청 생성
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 본문에 json 담아서 보내고 있음.
                .accept(MediaTypes.HAL_JSON)    // hal json 응답을 원한다.(accept 헤더 지정)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    /**
     * 입력값이 빈 경우 에러를 발생시키는 테스트
     */
    @Test
    @TestDescription("입력값이 빈 경우 에러를 발생시키는 테스트")
    public void createEvent_bad_request_empty_input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
        ;
    }

    /**
     * 입력값이 잘못된 경우 에러가 발생하는 테스트
     */
    @Test
    @TestDescription("입력값이 잘못된 경우 에러가 발생하는 테스트")
    public void createEvent_bad_request_weird_input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,7,20,10,0,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,7,19,10,0,0))
                .beginEventDateTime(LocalDateTime.of(2019,7,18,10,0,0))
                .endEventDateTime(LocalDateTime.of(2019,7,17,10,0,0))
                .basePrice(200)
                .maxPrice(100)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();
        // 어노테이션 검증이 어려움
        this.mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("content[0].field").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists()) // 인덱스 페이지 링크가 있길 바람
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0,30).forEach(i ->{
            this.generateEvent(i);
        });

        // when
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name, DESC")
                )

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
        ;
    }

    private void generateEvent(int index) {
        Event event = Event.builder()
                .name("event"+index)
                .description("test event")
                .build();

        this.eventRepository.save(event);


    }
}
