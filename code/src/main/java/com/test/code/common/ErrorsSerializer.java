package com.test.code.common;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent  // 오브젝트 매퍼에 이 시리얼 라이즈 등록
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartArray();// errors 안에 에러 여러개 담을 배열로 생성
        // 에러의 두 종료 필드에러, 글로벌 에러
        // validator에서 .reject~ 글로벌에러에 포함됨. .rejectValue~ 필드에러에 포함됨.
        // 따라서 여기서 필드에러와 글로벌 에러 둘다 담아줌
        errors.getFieldErrors().forEach(e -> {
           try {
               gen.writeStartObject();
               gen.writeStringField("field", e.getField());
               gen.writeStringField("objectName", e.getObjectName());
               gen.writeStringField("code", e.getCode());
               gen.writeStringField("defaultMessage", e.getDefaultMessage());
               Object rejectedValue = e.getRejectedValue();
               if(rejectedValue != null) {
                   gen.writeStringField("rejectedValue", rejectedValue.toString());
               }
               gen.writeEndObject();
           } catch (IOException ioe) {
                ioe.printStackTrace();
           }
        });

        errors.getGlobalErrors().forEach(e -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });

        gen.writeEndArray();
    }
}
