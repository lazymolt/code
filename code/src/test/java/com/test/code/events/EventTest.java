package com.test.code.events;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Inflearn Spring RestAPI")
                .description("JJJJJ")
                .build();
        assertThat(event);
    }

    @Test
    public void javaBean(){
        // given
        String name = "Event";
        String desc = "jjjj";

        // when
        Event event = new Event();
        event.setName(name);
        event.setDescription(desc);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(desc);
    }

    @Test
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        event.update();
        assertThat(event.isFree()).isEqualTo(isFree);
    }
    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[]{0,0,true},
                new Object[]{100,0,false},
                new Object[]{0,100,false},
                new Object[]{100,200,false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline){
        Event event = Event.builder()
                .location(location)
                .build();

        event.update();

        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline(){
        return new Object[]{
                new Object[]{"강남",true},
                new Object[]{null,false}
        };
    }
}