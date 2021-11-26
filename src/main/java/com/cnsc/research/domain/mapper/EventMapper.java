package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Event;
import com.cnsc.research.domain.transaction.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventMapper extends GeneralMapper<Event, EventDto> {
    @Override
    public Event toDomain(EventDto transactionsData) throws Exception {
        Event event = new Event();
        event.setId(transactionsData.getEventId());
        event.setEventName(transactionsData.getEventName());
        event.setEventType(transactionsData.getEventType());
        event.setEventDatetime(transactionsData.getEventDatetime());
        return event;
    }

    @Override
    public EventDto toTransaction(Event domainData) {
        return EventDto.builder()
                .eventId(domainData.getId())
                .eventName(domainData.getEventName())
                .eventType(domainData.getEventType())
                .eventDatetime(domainData.getEventDatetime())
                .build();
    }
}
