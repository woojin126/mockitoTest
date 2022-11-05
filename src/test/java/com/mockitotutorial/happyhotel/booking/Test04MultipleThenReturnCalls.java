package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
@Mock 은 실제 객체인것처럼 행동
 */
class Test04MultipleThenReturnCalls {

    private BookingService bookingService;
    private PaymentService paymentServiceMock;
    private RoomService roomServiceMock;
    private BookingDAO bookingDAOMock;
    private MailSender mailSenderMock;

    @BeforeEach
    void setUp() {
        this.paymentServiceMock = mock(PaymentService.class);
        this.roomServiceMock = mock(RoomService.class);
        this.bookingDAOMock = mock(BookingDAO.class);
        this.mailSenderMock = mock(MailSender.class);
        this.bookingService = new BookingService(paymentServiceMock,roomServiceMock,bookingDAOMock,mailSenderMock);
        /*일반적으로 서비스단 테스트를 위해서 4개의 의존성을 가진다. 현재 MailSender 내부를보면 구현조차 되어있지않아 MailSender의 메서드를 사용하게되면 excepion이 발생한다.
          우리는 테스트 예약 서비스 자체에만 집중하고싶다. 그래서우린 모의 객체를 만든다.
         */
    }

    @Test
    void should_CountAvailablePlaces_When_CalledMultipleTimes() {
        when(this.roomServiceMock.getAvailableRooms())
                .thenReturn(Collections.singletonList(new Room("Room 1",5)))
                .thenReturn(Collections.emptyList());
        int expectedFirstCall = 5;
        int expectedSecondCall = 0;

        //when
        int actualFirst = bookingService.getAvailablePlaceCount();
        int actualSecond = bookingService.getAvailablePlaceCount();

        //then
        Assertions.assertAll(() -> Assertions.assertEquals(expectedFirstCall, actualFirst),
                () -> Assertions.assertEquals(expectedSecondCall,actualSecond));
    }
}
